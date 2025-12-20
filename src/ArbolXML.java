import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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


    //BUSCAR VALORES POR ETIQUETA (SIN ORDEN)

    public List<String> buscarValoresPorEtiqueta(String etiqueta) {

        List<String> resultado = new ArrayList<>();

        buscarValoresRec(this.raiz, etiqueta, resultado);

        return resultado;
    }


    // Método auxiliar recursivo - PARA BUSAR VALORES
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

    //BUSCAR PERO ORDENADO (USANDO EL MIN HEAP)
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


    //-----------RECORRIDOS----------------
    //IMPRIMIR PREORDEN

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

    //-----IMPRIMIR POSTORDEN----
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

    //----FUNCIONALIDAD EXTRA-------
    // ==========================================================
// BUSCAR NODOS COMPLETOS POR ETIQUETA (para insertar hijos)
// ==========================================================
    public List<NodoXML> buscarPorEtiqueta(String etiqueta) {

        List<NodoXML> resultado = new ArrayList<>();

        buscarNodosRec(this.raiz, etiqueta, resultado);

        return resultado;
    }

    // Método auxiliar recursivo
    private void buscarNodosRec(
            NodoXML nodo,
            String etiqueta,
            List<NodoXML> resultado
    ) {
        if (nodo == null) return;

        if (nodo.getNombreEtiqueta().equals(etiqueta)) {
            resultado.add(nodo);
        }

        for (NodoXML hijo : nodo.getHijos()) {
            buscarNodosRec(hijo, etiqueta, resultado);
        }
    }

    // ==========================================================
// NUEVA FUNCIONALIDAD: AGREGAR NODO AL ÁRBOL
// ==========================================================
    public boolean agregarNodo(
            String etiquetaPadre,
            String nuevaEtiqueta,
            String texto
    ) {

        // 1. Buscar nodos con la etiqueta padre
        List<NodoXML> padres = buscarPorEtiqueta(etiquetaPadre);

        if (padres.isEmpty()) {
            return false; // No existe el padre
        }

        // 2. Tomar el PRIMER padre encontrado
        NodoXML padre = padres.get(0);

        // 3. Crear nuevo nodo
        NodoXML nuevo = new NodoXML(nuevaEtiqueta);
        nuevo.setTexto(texto);

        // 4. Agregar como hijo
        padre.addHijo(nuevo);

        return true;
    }

    // ==========================================================
// GUARDAR EL ÁRBOL XML ACTUAL EN ARCHIVO
// ==========================================================
    public boolean guardarXML(String rutaArchivo) {

        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            fw.write(generarXML());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ==========================================================
// ESCRIBIR UN NODO XML (RECURSIVO)
// ==========================================================
    private void escribirNodoXML(
            NodoXML nodo,
            java.io.PrintWriter pw,
            int nivel
    ) {
        if (nodo == null) return;

        String indent = "    ".repeat(nivel);

        // 1. Abrir etiqueta + atributos
        pw.print(indent + "<" + nodo.getNombreEtiqueta());

        for (Map.Entry<String, String> attr : nodo.getAtributos().entrySet()) {
            pw.print(" " + attr.getKey() + "=\"" + attr.getValue() + "\"");
        }

        // 2. Caso: nodo sin hijos y sin texto
        if (nodo.getHijos().isEmpty() &&
                (nodo.getTexto() == null || nodo.getTexto().isEmpty())) {

            pw.println(" />");
            return;
        }

        pw.print(">");

        // 3. Texto
        if (nodo.getTexto() != null && !nodo.getTexto().isEmpty()) {
            pw.print(nodo.getTexto());
        }

        // 4. Hijos
        if (!nodo.getHijos().isEmpty()) {
            pw.println();

            for (NodoXML hijo : nodo.getHijos()) {
                escribirNodoXML(hijo, pw, nivel + 1);
            }

            pw.print(indent);
        }

        // 5. Cerrar etiqueta
        pw.println("</" + nodo.getNombreEtiqueta() + ">");
    }

    // ==========================================================
// EXPORTAR EL ÁRBOL A TEXTO XML
// ==========================================================
    public String generarXML() {

        if (raiz == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        generarXMLRec(raiz, sb, 0);
        return sb.toString();
    }

    private void generarXMLRec(NodoXML nodo, StringBuilder sb, int nivel) {

        if (nodo == null) return;

        String indent = "    ".repeat(nivel);

        // Apertura
        sb.append(indent).append("<").append(nodo.getNombreEtiqueta()).append(">");

        // Texto
        if (nodo.getTexto() != null && !nodo.getTexto().isEmpty()) {
            sb.append(nodo.getTexto());
        }

        // Hijos
        if (!nodo.getHijos().isEmpty()) {
            sb.append("\n");
            for (NodoXML hijo : nodo.getHijos()) {
                generarXMLRec(hijo, sb, nivel + 1);
            }
            sb.append(indent);
        }

        // Cierre
        sb.append("</").append(nodo.getNombreEtiqueta()).append(">\n");
    }











}


















































