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

    //BUSCAR VALORES ORDENADOS
    public List<String> buscarValoresOrdenados(String etiqueta) {

        // 1. Buscar nodos con esa etiqueta
        List<NodoXML> encontrados = buscarPorEtiqueta(etiqueta);

        // 2. Crear un MIN-HEAP (padre es el menor)
        Heap<String> heap = new Heap<>(100, false, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                if (a == null) a = "";
                if (b == null) b = "";
                return a.compareToIgnoreCase(b);
            }
        });

        // 3. Meter los textos en el heap
        for (NodoXML n : encontrados) {
            heap.encolar(n.getTexto());
        }

        // 4. Sacar valores ORDENADOS
        List<String> ordenados = new ArrayList<>();

        while (!heap.estaVacio()) {
            ordenados.add(heap.desencolar());
        }

        return ordenados;
    }









}





























