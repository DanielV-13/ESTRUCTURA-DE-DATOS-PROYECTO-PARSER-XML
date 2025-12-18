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

    //PreOrden (raiz - izq - derecha)
    public void preOrden(NodoXML nodo) {
        //Caso Base
        if (nodo == null){ return;} //Si esta vacio el nodo, sale del metodo

        //else
        System.out.println(nodo);  //Imprime el nombre de la raiz (usando el toString de Nodo)

        //Recorre todos los hijos (izq y derecha)
        for (NodoXML hijo : nodo.getHijos()) {  //Recorre la lista de hijos de ese nodo
            preOrden(hijo);
        }
    }


//EnOrden (izq - raiz - derecha)  / SE REQUIERE SEPARLO EN 2 MITADES (para saber donde imprimir la raiz)
    public void enOrden(NodoXML nodo) {
        //Caso Base
        if (nodo == null) {
            return;}  //Si esta vacio el nodo, sale del metodo


        List<NodoXML> sons = nodo.getHijos(); //Obtengo el arreglo de hijos
        int cantHijos = sons.size();  //Cantindad de hijos de ese nodo
        int mitad = cantHijos / 2; //Calculo la mitad del arreglo


        //Mitad izquierda de los nodos
        for (int i = 0; i < mitad; i++) {
            NodoXML hijoN = sons.get(i);
            //Aplicar recursivamente el metodo EnOrden a cada nodo
            enOrden(hijoN);
        }

        System.out.println(nodo); //Imprimir el nodo actual (SIEMPRE SE IMPRIME EL NODO ACTUAL)

        //Mitad Derecha de los Nodos
        for (int i = mitad; i < cantHijos; i++) {   //Imprmir la mitad derecha
            NodoXML hijoN = sons.get(i);
            enOrden(hijoN);
        }
    }


    //PostOrden (izq - derecha - raiz)
    public void PostOrden(NodoXML nodo) {
        //Caso Base
        if (nodo == null){ return;} //Si esta vacio el nodo, sale del metodo

        //else
        //RECORRO TODOS LOS HIJOS (izq y derecha)
        for (NodoXML hijo : nodo.getHijos()) {  //Recorre la lista de hijos de ese nodo
            PostOrden(hijo);
        }

        System.out.println(nodo);  //Imprime el nombre de la raiz (usando el toString de Nodo)

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

    //----------------------METODOS PARA LECTURA DEL ARCHIVO XML-----------

    //METODOS AUXILARES - devuelven boolean

    // Detectar etiqueta mixta
       //  <tag>texto</tag>

    private boolean esEtiquetaMixta(String linea) {
        return linea.contains("</") && linea.indexOf("</") > linea.indexOf(">");  //Deteca el indice del primer ">"
    }

    //Detectar etiqueta de apertura <tag ...>
    private boolean esEtiquetaApertura(String linea) {
        return linea.startsWith("<") && !linea.startsWith("</") && linea.endsWith(">");
    }

    //Detectar etiqueta de cierre </tag>
    private boolean esEtiquetaCierre(String linea) {
        return linea.startsWith("</") && linea.endsWith(">");
    }



    //Extraer nombre de etiqueta en apertura
    private String extraerNombreApertura(String linea) {

        // Quitar < > de la etiqueta de apertura (el primer y ultimo caracter)
        String contenido = linea.substring(1, linea.length() - 1).trim();

        //El  "Nombre" es la primera palabra antes de un espacio
        //<tag ...>
        String[] partes = contenido.split(" ");

        return partes[0];  //Devuelve el nombre
    }



    //Extraer nombre en cierre  </tag>
    private String extraerNombreCierre(String linea) {

        // Quitar </ > --- se quitan los 2 primeros caracteres y el ultimo
        return linea.substring(2, linea.length() - 1).trim();
    }


    //Extraer texto de etiqueta mixta <tag>texto</tag>
    private String extraerTextoMixto(String linea) {

        int ini = linea.indexOf(">") + 1;  //El texto empieza despues del primer ">"
        int fin = linea.indexOf("</");  //El texto termina antes del "</"

        if (fin > ini) { //Si primero esta ">" y luego "</"
            return linea.substring(ini, fin).trim();  //Devuelve lo que esta en medio
        }

        return "";  //Si no, NO devuelve nada porque no tiene texto
    }

    //Validación muy simple de nombre
    private boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isEmpty() && !nombre.contains(" ");  //No debe ser nulo, no debe estar vacio,
    }

//--------------METODO cargarXML usando los anteriores metodos auxiliares------------
/*public void cargarXML(String rutaArchivo) {

    Stack<NodoXML> pila = new Stack<>();

    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

        String linea;

        while ((linea = br.readLine()) != null) {  //Mientas hayas lineas en el XML siga leyendo

            linea = linea.trim();  //Quito espacios en blanco

            if (linea.isEmpty()) continue;  //Si esta vacia la linea sigue a la otra


            // ----------------------------------------------------
            // CASO 1: ETIQUETA TIPO <tag>texto</tag>
            // ---------------------------------------------------
            if (esEtiquetaMixta(linea)) {

                // 1. Obtener el nombre de la etiqueta de apertura
                int ini = linea.indexOf("<") + 1;
                int fin = linea.indexOf(">");
                String nombre = linea.substring(ini, fin).trim();

                if (!nombreValido(nombre)) {
                    System.out.println("ERROR XML: etiqueta de apertura mal formada → " + linea);
                    continue;
                }

                // 2. Crear nodo
                NodoXML nuevo = new NodoXML(nombre);

                // 3. Extraer texto interno
                String texto = extraerTextoMixto(linea);
                nuevo.setTexto(texto);

                // 4. Insertar nodo en el árbol
                if (pila.isEmpty()) this.raiz = nuevo;
                else pila.peek().addHijo(nuevo);

                continue; // No se apila porque se cierra inmediatamente
            }


            // ============================================================
            // CASO 2: ETIQUETA DE CIERRE </tag>
            // ============================================================
            if (esEtiquetaCierre(linea)) {

                String nombre = extraerNombreCierre(linea);

                if (pila.isEmpty()) {
                    System.out.println("ERROR XML: cierre sin apertura → " + linea);
                    continue;
                }

                if (!pila.peek().getNombreEtiqueta().equals(nombre)) {
                    System.out.println("ERROR XML: cierre inesperado → " + linea +
                            " (se esperaba </" + pila.peek().getNombreEtiqueta() + ">)");
                    continue;
                }

                pila.pop();
                continue;
            }


            // ============================================================
            // CASO 3: ETIQUETA DE APERTURA <tag>
            // ============================================================
            if (esEtiquetaApertura(linea)) {

                String nombreEtiqueta = extraerNombreApertura(linea);

                if (!nombreValido(nombreEtiqueta)) {
                    System.out.println("ERROR XML: etiqueta mal formada → " + linea);
                    continue;
                }

                NodoXML nuevoNodo = new NodoXML(nombreEtiqueta);

                // Procesar atributos simples
                String contenido = linea.substring(1, linea.length() - 1).trim();
                String[] partes = contenido.split(" ");

                for (int i = 1; i < partes.length; i++) {
                    if (partes[i].contains("=")) {
                        String[] kv = partes[i].split("=");
                        if (kv.length == 2) {
                            nuevoNodo.addAtributo(
                                    kv[0],
                                    kv[1].replace("\"", "")
                            );
                        }
                    }
                }

                if (pila.isEmpty()) this.raiz = nuevoNodo;
                else pila.peek().addHijo(nuevoNodo);

                pila.push(nuevoNodo);
                continue;
            }


            // ============================================================
            // CASO 4: TEXTO PURO ENTRE ETIQUETAS
            // ============================================================
            if (!linea.startsWith("<") && !pila.isEmpty()) {
                pila.peek().setTexto(linea);
            }
        }

    } catch (Exception e) {
        System.out.println("ERROR LEYENDO XML: " + e.getMessage());
    }


    // ============================================================
    // VALIDACIÓN FINAL: PILA DEBE QUEDAR VACÍA
    // ============================================================
    if (!pila.isEmpty()) {
        System.out.println("ERROR XML: faltan etiquetas de cierre. Última abierta → <"
                + pila.peek().getNombreEtiqueta() + ">");
    }
}


*/






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


















































