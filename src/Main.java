import java.util.List;

public class Main {
    public static void main(String[] args) {

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
        System.out.println("\n--- BUSQUEDA: 'fruit' ---");
        for (NodoXML nodo : arbol.buscarPorEtiqueta("fruit")) {
            System.out.println(nodo);
        }

        // ------- PROBAR ORDENAMIENTO POR FRAGMENTO ---------
        System.out.println("\n--- ORDENAR POR FRAGMENTO 'berry' ---");
        List<NodoXML> ordenados = arbol.ordenarPorFragmento("fruit", "berry");

        for (NodoXML nodo : ordenados) {
            System.out.println(nodo);
        }

    }
}