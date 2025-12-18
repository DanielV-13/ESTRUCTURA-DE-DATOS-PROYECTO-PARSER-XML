import java.util.List;

public class Main {
    public static void main(String[] args) {
/*
        //XML PARA PROBAR EN EL MAIN
        //<fruits>
        //    <fruit>apple</fruit>
        //    <fruit>strawberry</fruit>
        //    <fruit>blueberry</fruit>
        //    <fruit>framberry</fruit>
        //</fruits>

        // CREAR NODOS MANUALMENTE

        // Nodo raíz <fruits>
        NodoXML raiz = new NodoXML("fruits");

        // Nodo <fruit>apple</fruit>
        NodoXML n1 = new NodoXML("fruit");
        n1.setTexto("apple");

        // Nodo <fruit>strawberry</fruit>
        NodoXML n2 = new NodoXML("fruit");
        n2.setTexto("strawberry");

        // Nodo <fruit>blueberry</fruit>
        NodoXML n3 = new NodoXML("fruit");
        n3.setTexto("blueberry");

        // Nodo <fruit>framberry</fruit>
        NodoXML n4 = new NodoXML("fruit");
        n4.setTexto("framberry");

        // Agregar hijos al nodo raíz
        raiz.addHijo(n1);
        raiz.addHijo(n2);
        raiz.addHijo(n3);
        raiz.addHijo(n4);

        //--------------- CREAR ARBOL---------------
        ArbolXML arbol = new ArbolXML();
        arbol.setRaiz(raiz);

        // ------- PROBAR RECORRIDOS --------
        System.out.println("\n--- RECORRIDO PREORDEN ---");
        arbol.preOrden(arbol.getRaiz());

        System.out.println("\n--- RECORRIDO ENORDEN ---");
        arbol.enOrden(arbol.getRaiz()); //recorre desde la raiz

        System.out.println("\n--- RECORRIDO POSTORDEN ---");
        arbol.PostOrden(arbol.getRaiz());

        // ------ PROBAR BUSQUEDA POR ETIQUETA --------------
        System.out.println("\n--- BUSQUEDA ORDENADA: 'fruit' ---");
        List<String> valoresOrdenados = arbol.buscarValoresOrdenados("fruit");

        for (String valor : valoresOrdenados) {
            System.out.println(valor);
        }
        // ------- PROBAR ORDENAMIENTO POR FRAGMENTO ---------
        System.out.println("\n--- ORDENAR POR FRAGMENTO 'berry' ---");
        List<NodoXML> ordenados = arbol.ordenarPorFragmento("fruit", "berry");

        for (NodoXML nodo : ordenados) {
            System.out.println(nodo);
        }
*/



        System.out.println("======= XML PARSER TEST =======");

        //Crear el arbol
        ArbolXML arbol = new ArbolXML();

        // Archivo de prueba
        String ruta = "src/archivito.xml";

        // 1. Cargar XML
        arbol.cargarXML(ruta);
        System.out.println("\n[OK] Archivo XML cargado.\n");

        // 2. Recorrido PREORDEN
        System.out.println("=== PREORDEN ===");
        arbol.imprimir(arbol.preOrdenFinal());


        // 4. Recorrido POSTORDEN
        System.out.println("\n=== POSTORDEN ===");
        arbol.imprimir(arbol.postOrdenFinal());

        // 5. Buscar nodos por etiqueta
        System.out.println("\n=== BUSCAR ETIQUETA 'name' ===");
        List<NodoXML> listaName = arbol.buscarPorEtiqueta("name");
        for (NodoXML nodo : listaName) {
            System.out.println(nodo);
        }


        // 6. Buscar SOLO VALORES ordenados
        System.out.println("\n=== VALORES ORDENADOS (Etiqueta: 'name') ===");
        List<String> nombresOrdenados = arbol.buscarValoresOrdenados("name");
        for (String s : nombresOrdenados) {
            System.out.println(s);
        }


        // 7. Ordenar nodos por fragmento usando tu HEAP
        System.out.println("\n=== ORDENAR NODOS QUE CONTIENEN 'wire' ===");
        List<NodoXML> listaWire = arbol.ordenarPorFragmento("name", "wire");
        for (NodoXML nodo : listaWire) {
            System.out.println(nodo);
        }


        System.out.println("\n======= FIN DE PRUEBAS =======");

/*
        System.out.println("======= XML PARSER TEST 2 de archivito =======");

        //Crear el arbol
        ArbolXML arbol = new ArbolXML();

        // Archivo de prueba
        String ruta = "src/archivito.xml";

        // 1. Cargar XML
        arbol.cargarXML(ruta);
        System.out.println("\n[OK] Archivo XML cargado.\n");


        // 2. Recorrido PREORDEN
        System.out.println("=== PREORDEN ===");
        arbol.preOrden(arbol.getRaiz());


        // 3. Recorrido ENORDEN
        System.out.println("\n=== ENORDEN ===");
        arbol.enOrden(arbol.getRaiz());


        // 4. Recorrido POSTORDEN
        System.out.println("\n=== POSTORDEN ===");
        arbol.PostOrden(arbol.getRaiz());


        // 5. Buscar nodos por etiqueta
        System.out.println("\n=== BUSCAR ETIQUETA 'name' ===");
        List<NodoXML> listaName = arbol.buscarPorEtiqueta("color");
        for (NodoXML nodo : listaName) {
            System.out.println(nodo);
        }


        // 6. Buscar SOLO VALORES ordenados
        System.out.println("\n=== VALORES ORDENADOS (Etiqueta: 'name') ===");
        List<String> nombresOrdenados = arbol.buscarValoresOrdenados("color");
        for (String s : nombresOrdenados) {
            System.out.println(s);
        }


        // 7. Ordenar nodos por fragmento usando tu HEAP
        System.out.println("\n=== ORDENAR NODOS QUE CONTIENEN 'wire' ===");
        List<NodoXML> listaWire = arbol.ordenarPorFragmento("name", "op");
        for (NodoXML nodo : listaWire) {
            System.out.println(nodo);
        }


        System.out.println("\n======= FIN DE PRUEBAS =======");

*/


            }
        }

