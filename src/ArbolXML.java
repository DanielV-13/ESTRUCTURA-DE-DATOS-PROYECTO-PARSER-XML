import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

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

    //Metodo para imprimir un arbol
    public void imprimir(NodoXML nodo, int nivel) {
        if (nodo == null) return;

        String indentacion = "  ".repeat(nivel);
        System.out.println(indentacion + nodo);

        for (NodoXML hijo : nodo.getHijos()) {
            imprimir(hijo, nivel + 1);
        }
    }

    //Metodo para imprimir una lista respetando el orden del recorrido
    public void imprimir(List<NodoNivel> lista) {
        for (NodoNivel elem : lista) {
            String indentacion = "  ".repeat(elem.nivel);
            System.out.println(indentacion + elem.nodo);
        }
    }

    //==================== PREORDEN ====================

    //Metodo que retorna una lista con el recorrido PREORDEN
    public List<NodoNivel> preOrdenFinal() {
        List<NodoNivel> lista = new ArrayList<>();
        preOrdenRecursivo(raiz, 0, lista);
        return lista;
    }

    //Metodo recursivo auxiliar para PREORDEN
    private void preOrdenRecursivo(NodoXML nodo, int nivel, List<NodoNivel> lista) {
        if (nodo == null) return;

        lista.add(new NodoNivel(nodo, nivel));

        //Recorro todos los hijos (izq y derecha)
        for (NodoXML hijo : nodo.getHijos()) {
            preOrdenRecursivo(hijo, nivel + 1, lista);
        }
    }

    //==================== POSTORDEN ====================

    //Metodo que retorna una lista con el recorrido POSTORDEN
    public List<NodoNivel> postOrdenFinal() {
        List<NodoNivel> lista = new ArrayList<>();
        postOrdenRecursivo(raiz, 0, lista);
        return lista;
    }

    //Metodo recursivo auxiliar para POSTORDEN
    private void postOrdenRecursivo(NodoXML nodo, int nivel, List<NodoNivel> lista) {
        if (nodo == null) return;
        //RECORRO TODOS LOS HIJOS (izq y derecha)
        for (NodoXML hijo : nodo.getHijos()) {
            postOrdenRecursivo(hijo, nivel + 1, lista);
        }
        lista.add(new NodoNivel(nodo, nivel));
    }

    //==================== ENORDEN ====================

    //Metodo que retorna una lista con el recorrido ENORDEN
    public List<NodoNivel> enOrdenFinal() {
        List<NodoNivel> lista = new ArrayList<>();
        enOrdenRecursivo(raiz, 0, lista);
        return lista;
    }

    //Metodo recursivo auxiliar para ENORDEN
    private void enOrdenRecursivo(NodoXML nodo, int nivel, List<NodoNivel> lista) {
        if (nodo == null) return;
        List<NodoXML> sons = nodo.getHijos(); //Obtengo el arreglo de hijos
        int cantHijos = sons.size();          //Cantidad de hijos de ese nodo
        int mitad = cantHijos / 2;            //Calculo la mitad del arreglo
        //Mitad izquierda de los nodos
        for (int i = 0; i < mitad; i++) {
            enOrdenRecursivo(sons.get(i), nivel + 1, lista);
        }
        lista.add(new NodoNivel(nodo, nivel));
        //Mitad derecha de los nodos
        for (int i = mitad; i < cantHijos; i++) {
            enOrdenRecursivo(sons.get(i), nivel + 1, lista);
        }
    }


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

//----------------------METODO PARA LECTURA DEL XML------------------------
public void cargarXML(String rutaArchivo) {

    Stack<NodoXML> pila = new Stack<>();

    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

        String linea;

        while ((linea = br.readLine()) != null) {

            linea = linea.trim();

            if (linea.isEmpty()) continue;


            // ============================================================
            // CASO 1: ETIQUETA TIPO <tag>texto</tag>
            // ============================================================
            if (linea.contains("</") && linea.indexOf("</") > linea.indexOf(">")) {

                // Ejemplo: <name>Wireless Mouse</name>

                // 1. Extraer nombre de etiqueta de apertura
                int ini = linea.indexOf("<") + 1;
                int fin = linea.indexOf(">");
                String nombre = linea.substring(ini, fin);

                // 2. Crear nodo
                NodoXML nuevo = new NodoXML(nombre);

                // 3. Extraer texto
                int iniTxt = fin + 1;
                int finTxt = linea.indexOf("</");
                String texto = linea.substring(iniTxt, finTxt).trim();
                nuevo.setTexto(texto);

                // 4. Insertarlo en el árbol
                if (pila.isEmpty()) this.raiz = nuevo;
                else pila.peek().addHijo(nuevo);

                continue;
            }


            // ============================================================
            // CASO 2: ETIQUETA DE CIERRE </tag>
            // ============================================================
            if (linea.startsWith("</")) {

                String nombre = linea.substring(2, linea.length() - 1).trim();

                if (!pila.isEmpty() && pila.peek().getNombreEtiqueta().equals(nombre)) {
                    pila.pop();
                } else {
                    System.out.println("ERROR XML: etiqueta de cierre inesperada: " + linea);
                }

                continue;
            }


            // ============================================================
            // CASO 3: ETIQUETA DE APERTURA <tag>
            // ============================================================
            if (linea.startsWith("<") && linea.endsWith(">")) {

                // Si tiene un espacio → tiene atributos
                String contenido = linea.substring(1, linea.length() - 1).trim();
                String[] partes = contenido.split(" ");

                String nombreEtiqueta = partes[0];
                NodoXML nuevoNodo = new NodoXML(nombreEtiqueta);

                // Procesar atributos
                for (int i = 1; i < partes.length; i++) {
                    if (partes[i].contains("=")) {
                        String[] kv = partes[i].split("=");
                        nuevoNodo.addAtributo(kv[0], kv[1].replace("\"", ""));
                    }
                }

                if (pila.isEmpty()) this.raiz = nuevoNodo;
                else pila.peek().addHijo(nuevoNodo);

                pila.push(nuevoNodo);
                continue;
            }


            // ============================================================
            // CASO 4: TEXTO PURO EN LÍNEA SEPARADA
            // ============================================================
            if (!linea.startsWith("<") && !pila.isEmpty()) {
                pila.peek().setTexto(linea);
            }
        }

    } catch (Exception e) {
        System.out.println("ERROR LEYENDO XML: " + e.getMessage());
    }
}



}


















































