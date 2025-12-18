import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.ArrayList;
import java.util.List;


public class ArbolXML {

    //Atributos
    private NodoXML raiz;

    //Constructor - vacio
    public ArbolXML() { }

    public void setRaiz(NodoXML root) {
        this.raiz = root;
    }

    public NodoXML getRaiz() {
        return raiz;
    }


    //----------METODOS PARA IMPRIMIR LOS RECORRIDOS-------------








    //---METODO PARA BUSCAR POR ETIQUETA EN EL ARBOL---

    //Metodo que empieza buscando desde la RAIZ del arbolXML
    public List<NodoXML> buscarPorEtiqueta(String etiqueta) {
        List<NodoXML> resultado = new ArrayList<>();
        buscarRecursivo(raiz, etiqueta, resultado);
        return resultado;
    }

    //Metodo recursivo auxiliar
    private void buscarRecursivo(NodoXML nodo, String etiqueta, List<NodoXML> res) {
        //Caso Base
        if (nodo == null) return;

        if (nodo.getNombreEtiqueta().equals(etiqueta)) {
            res.add(nodo);
        }

        for (NodoXML hijo : nodo.getHijos()) { //Recorro los hijos del Nodo y aplico el metodo recursivo
            buscarRecursivo(hijo, etiqueta, res);
        }
    }

    //-----------

    // METODO PARA Ordenamiento de los nodos por contenido textual de una cierta etiqueta
    public List<NodoXML> ordenarPorFragmento(String etiqueta, String fragmento) {

        // Buscar nodos con esa etiqueta
        List<NodoXML> encontrados = buscarPorEtiqueta(etiqueta);

        // Filtrar los nodos cuyo texto contenga el fragmento
        List<NodoXML> filtrados = new ArrayList<>();
        for (NodoXML nodo : encontrados) {

            String texto = nodo.getTexto();
            if (texto != null && texto.toLowerCase().contains(fragmento.toLowerCase())) {
                filtrados.add(nodo);
            }
        }

        // Crear un MIN-HEAP usando el Comparator por texto (clase Comparator porTexto)
        Heap<NodoXML> heap = new Heap<>(100, false, new ComparatorTextoNodoXML());

        // Encolar cada nodo filtrado en el Heap
        for (NodoXML nodo : filtrados) {
            heap.encolar(nodo);
        }

        // Desencolar uno por uno para obtenerlos ORDENADOS  (los retorna en orden ALFABETICO)
        List<NodoXML> ordenados = new ArrayList<>();
        while (!heap.estaVacio()) { //Desencolar hasta que el Heap este vacio
            ordenados.add(heap.desencolar());
        }

        //  Retornar la lista ordenada
        return ordenados;
    }

//-------------
//REVISAR ESTE METODO
    //BUSCAR VALORES ORDENADOS
    public List<String> buscarValoresOrdenados(String etiqueta) {

        // 1. Buscar nodos con esa etiqueta
        List<NodoXML> encontrados = buscarPorEtiqueta(etiqueta);

        // 2. Crear un MIN-HEAP para ordenar alfabeticamente
        //Usamos el ComparatorTextoNodoXML

        Heap<NodoXML> heap = new Heap<>(100, false, new ComparatorTextoNodoXML() ) ;

        // 3. Meter los nodos en el heap  (se ordenaran por su texto)
        for (NodoXML n : encontrados) {
            heap.encolar(n);
        }

        // 4. Sacar valores (los textos) ORDENADOS
        List<String> ordenados = new ArrayList<>();

        while (!heap.estaVacio()) {
            ordenados.add(heap.desencolar().getTexto());
        }

        return ordenados;
    }
// ================== PROCESADORES DE LINEAS XML ==================

    private boolean procesarEtiquetaMixta(String linea, Stack<NodoXML> pila) {

        int ini = linea.indexOf("<") + 1;
        int fin = linea.indexOf(">");

        String nombre = linea.substring(ini, fin);
        NodoXML nuevo = new NodoXML(nombre);

        int iniTxt = fin + 1;
        int finTxt = linea.indexOf("</");
        String texto = linea.substring(iniTxt, finTxt).trim();
        nuevo.setTexto(texto);

        if (pila.isEmpty()) this.raiz = nuevo;
        else pila.peek().addHijo(nuevo);

        return true;
    }

    private boolean procesarCierre(String linea, Stack<NodoXML> pila) {

        String nombre = linea.substring(2, linea.length() - 1).trim();

        if (!pila.isEmpty() && pila.peek().getNombreEtiqueta().equals(nombre)) {
            pila.pop();
            return true;
        }

        System.out.println("ERROR XML: etiqueta de cierre inesperada: " + linea);
        return false;
    }

    private void procesarApertura(String linea, Stack<NodoXML> pila) {

        String contenido = linea.substring(1, linea.length() - 1).trim();
        String[] partes = contenido.split(" ");

        NodoXML nuevoNodo = new NodoXML(partes[0]);

        for (int i = 1; i < partes.length; i++) {
            if (partes[i].contains("=")) {
                String[] kv = partes[i].split("=");
                nuevoNodo.addAtributo(kv[0], kv[1].replace("\"", ""));
            }
        }

        if (pila.isEmpty()) this.raiz = nuevoNodo;
        else pila.peek().addHijo(nuevoNodo);

        pila.push(nuevoNodo);
    }

    private void procesarTexto(String linea, Stack<NodoXML> pila) {
        if (!pila.isEmpty()) {
            pila.peek().setTexto(linea);
        }
    }

    private boolean validarXMLFinal(Stack<NodoXML> pila) {
        if (!pila.isEmpty()) {
            System.out.println("ERROR XML: etiquetas sin cerrar");
            return false;
        }
        return true;
    }



    public boolean cargarXML(String rutaArchivo) {

        Stack<NodoXML> pila = new Stack<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // <tag>texto</tag>
                if (linea.contains("</") && linea.indexOf("</") > linea.indexOf(">")) {
                    procesarEtiquetaMixta(linea, pila);
                    continue;
                }

                // </tag>
                if (linea.startsWith("</")) {
                    if (!procesarCierre(linea, pila)) return false;
                    continue;
                }

                // <tag ...>
                if (linea.startsWith("<") && linea.endsWith(">")) {
                    procesarApertura(linea, pila);
                    continue;
                }

                // texto suelto
                procesarTexto(linea, pila);
            }

            return validarXMLFinal(pila);

        } catch (Exception e) {
            System.out.println("ERROR LEYENDO XML: " + e.getMessage());
            return false;
        }
    }



    public List<String> buscarValoresPorEtiqueta(String etiqueta) {

        List<String> resultado = new ArrayList<>();

        buscarValoresRec(this.raiz, etiqueta, resultado);

        return resultado;
    }

    // Método auxiliar recursivo
    private void buscarValoresRec(NodoXML nodo, String etiqueta, List<String> resultado) {

        if (nodo == null) return;

        // Si coincide la etiqueta, guardar SOLO el texto
        if (nodo.getNombreEtiqueta().equals(etiqueta)) {

            if (nodo.getTexto() != null && !nodo.getTexto().isEmpty()) {
                resultado.add(nodo.getTexto());
            }
        }

        // Seguir recorriendo hijos (orden natural del árbol)
        for (NodoXML hijo : nodo.getHijos()) {
            buscarValoresRec(hijo, etiqueta, resultado);
        }
    }

    public List<String> buscarValoresOrdenadosPorEtiqueta(String etiqueta) {

        // 1. Primero buscar los valores SIN ordenar
        List<String> valores = buscarValoresPorEtiqueta(etiqueta);

        // 2. Crear un MIN-HEAP para ordenar alfabéticamente
        Heap<String> heap = new Heap<>(100, false, String::compareToIgnoreCase);

        // 3. Insertar valores en el Heap
        for (String v : valores) {
            heap.encolar(v);
        }

        // 4. Extraer ordenados
        List<String> ordenados = new ArrayList<>();

        while (!heap.estaVacio()) {
            ordenados.add(heap.desencolar());
        }

        return ordenados;
    }

    public void imprimirPreordenVisual(
            NodoXML nodo,
            String prefijo,
            boolean esUltimo,
            JTextPane pane
    ) {
        if (nodo == null) return;

        StyledDocument doc = pane.getStyledDocument();

        try {
            // Dibujar ramas ASCII
            doc.insertString(doc.getLength(),
                    prefijo + (esUltimo ? "└── " : "├── "),
                    doc.getStyle("TEXTO")
            );

            // Etiqueta
            doc.insertString(doc.getLength(),
                    nodo.getNombreEtiqueta(),
                    doc.getStyle("ETIQUETA")
            );

            // Texto del nodo
            if (nodo.getTexto() != null && !nodo.getTexto().isEmpty()) {
                doc.insertString(doc.getLength(),
                        " : " + nodo.getTexto(),
                        doc.getStyle("TEXTO")
                );
            }

            doc.insertString(doc.getLength(), "\n", doc.getStyle("TEXTO"));

            // Preparar prefijo para hijos
            String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");

            for (int i = 0; i < nodo.getHijos().size(); i++) {
                boolean ultimoHijo = (i == nodo.getHijos().size() - 1);
                imprimirPreordenVisual(
                        nodo.getHijos().get(i),
                        nuevoPrefijo,
                        ultimoHijo,
                        pane
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimirPostordenVisual(
            NodoXML nodo,
            String prefijo,
            JTextPane pane
    ) {
        if (nodo == null) return;

        StyledDocument doc = pane.getStyledDocument();

        try {
            // 1. Recorrer hijos primero
            for (NodoXML hijo : nodo.getHijos()) {
                imprimirPostordenVisual(
                        hijo,
                        prefijo + "│   ",
                        pane
                );
            }

            // 2. Imprimir el nodo como cierre del bloque
            doc.insertString(
                    doc.getLength(),
                    prefijo + "└── ",
                    doc.getStyle("TEXTO")
            );

            doc.insertString(
                    doc.getLength(),
                    nodo.getNombreEtiqueta(),
                    doc.getStyle("ETIQUETA")
            );

            if (nodo.getTexto() != null && !nodo.getTexto().isEmpty()) {
                doc.insertString(
                        doc.getLength(),
                        " : " + nodo.getTexto(),
                        doc.getStyle("TEXTO")
                );
            }

            doc.insertString(doc.getLength(), "\n", doc.getStyle("TEXTO"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}


















































