import java.util.Comparator;

public class Heap <E> {
    //Atributos
    private Comparator<E> cmp;
    private E[] arreglo; //Almacena los elementos del Heap
    private int max = 100;  //Maximo numero de nodos del Heap - tamaño del arreglo
    private int efectivo; //Cuantos nodos tiene el heap en determinado momento
    private boolean isMax; //TRUE - Indica si es un Max-Heap (padre es el mayor)
    // FALSE -Es un Min-Heap (padre es el menor)


    //Constructor
    public Heap(int max, boolean isMax, Comparator<E> cmp) {
        this.max=max; //Capacidad maxima
        this.isMax=isMax; //Determina el tipo de Heap
        this.cmp=cmp; //El comparator

        //Crear el arreglo
        this.arreglo= (E[]) new Object[max]; //Crear arreglo
        this.efectivo=0; //El arreglo esta vacio al inicio
    }

//METODO CONSTRUIR HEAP
//Dado el arreglo del heap ajusta todos los nodos que NO sean hoja del ultimo al primero

    //OTRO CONSTRUCTOR QUE LE MANDE UN ARREGLO
    //AQUI SE CONSTRUYE EL HEAP Y QUE SEA ORDENADO

    public Heap( E[] arreglo) {    //Nuevo constructor
        this.arreglo= arreglo; //Recibe un arreglo
        max= arreglo.length;  //el maximo a ser el lenght del arreglo
        efectivo= max;  //Efectivo y max es el mismo

        //Metodo construir Heap
        for (int indice= efectivo-1; indice>=0; indice--){  //Va desde el ultimo nodo hasta el inicio
            ajustar(indice);  //Se aplica AJUSTAR a todos los nodos
            //Dentro de ajustar ya verifica si es o no es hoja
            //Si es hoja, no ajusta
        }

    }


    //Getters

    public E[] getArreglo(){
        return arreglo;
    }

    public int getEfectivo(){
        return efectivo;
    }


    //----METODOS----

    //estaVacio - verifica si el arreglo esta vacio
    public boolean estaVacio(){
        return efectivo==0;  //True si esta vacio
    }                       //False si no lo esta


    //Metodo esvalido - verifica si un indice es valido
    public boolean esvalido(int i){
        if(i>=0 && i<efectivo){  //Recordar que los indices empiezan en 0
            return true;
        }
        return false;
    }

    //Metodo imprimir el HEap como arreglo
    public void imprimirHeap() {
        System.out.print("[ ");
        for (int i = 0; i < efectivo; i++) {
            System.out.print(arreglo[i] + " ");
        }
        System.out.println("]");
    }



    //posIzQ
    //Recibe un indice y retorna el hijo izquierdo
    public int posIzq(int i){
        int izq=2*i+1;

        //Si cumple las reglas retorna el inidice del izq
        if(esvalido(izq)){
            return izq;
        }
        return -1;  //Si no cumple retorna -1
    }

    //posDeR
    //Recibe un indice y retorna el hijo derecho
    public int posDer(int i){
        int der=2*i+2;

        //Si cumple las reglas retorna el inidice de la der
        if(esvalido(der)){
            return der;
        }
        return -1;  //Si no cumple retorna -1
    }

    //posPADRE
    //Recibe un indice y retorna el indice del  padre de ese nodo
    public int posPadre(int i){
        if(i==0) {  //Si es la raiz
            return -1; //Retorna -1, no tiene padre
        }

        int dad=(i-1)/2;

        //Si cumple las reglas retorna el inidice del padre
        if(esvalido(dad)){
            return dad;
        }
        return -1;  //Si no cumple retorna -1
    }



    //Metodo SWAP - intercambia el indice de 2 elementos
    public void swap(int i, int j) {

        //Verificamos la validez
        if (esvalido(i) && esvalido(j)) {
            E tmp = arreglo[i];  //Guardamos el contenido del elemento en inidice i
            arreglo[i] = arreglo[j]; //Ahora [i] tiene el contenido de [j]
            arreglo[j] = tmp; //Ahora [j] tiene el contenido de [i]
        }
    }


    //Metodo quientienemayor - recibe un indice de un nodo
    // Verifica entre sus hijos(de ese elemento) y el elemento quien tiene el CONTENIDOmayor usando el comparator
    //Retorna el indice del mayor entre los 3

    //Nota: Si isMAx es TRUE (MAX-HEAP), ese metodo determinara el MAYOR entre los elementos
    //      Si isMAx es False (MIN-HEAP), ese metodo derminara el MENOR entre los elementos

    public int quientienemayor(int i){

        //HAY QUE EVITAR COMPARAR NULL
        if (arreglo[i] == null) return i;

        int der=posDer(i);
        int izq=posIzq(i);

        int obj=i; //Por defecto, asumimos que i es el objetivo a buscar (sea mayor o menor)

        //Comparar con el izquierdo (si existe)
        if(izq!=-1 && arreglo[izq] != null) {
            if ((isMax && cmp.compare(arreglo[izq], arreglo[obj]) > 0)
                    || (!isMax && cmp.compare(arreglo[izq], arreglo[obj]) < 0)) {

                obj = izq;
            }
        }

        //Comparar con el derecho (si existe)
        if(der!=-1 && arreglo[der] != null) {
            if ((isMax && cmp.compare(arreglo[der], arreglo[obj]) > 0)
                    || (!isMax && cmp.compare(arreglo[der], arreglo[obj]) < 0)) {

                obj = der;
            }
        }

        //Returna el indidce del (mayor o menor)
        return obj;

    }

    //Metodo eshoja - determina sin un elemento no tiene hjos
    public boolean esHoja(int i) {
        if (posDer(i) == -1 && posIzq(i) == -1) {
            return true;
        }
        return false;
    }



    //---METODO AJUSTAR----  //EL AJUSTE NOO SE PUEDE HACER SOBRE HOJAS - //Solo sobre PADRES
    //Recibe el indice del nodo dañado y ajusta el heap
    public void ajustar(int i) {

        int imayor; //variable

        if (!esvalido(i) || esHoja(i)) {
            return; //Sale del metodo si el indice no es valido o si es hoja
        }

        imayor = quientienemayor(i); //Determina el indice del mayor entre los posibles 3 nodos conectados al nodo dañado

        if(imayor==i){
            return; //Sale del metodo porque si esta ordenado el Heap
        }

        swap(i, imayor); //Cambia de posicion el nodo dañado con el mayor

        //Metodo recursivo
        ajustar(imayor); //vuelve a ajustar
    }




    //METODO DESENCOLAR
    //Se saca la raiz (el indice 0)
    //Se reemplaza la raiz con el ultimo elemento (indice efectivo-1)
    //Se ajusta desde la nueva raiz
    //El tamaño efectivo del heap cambia


    public E desencolar(){

        if(estaVacio()){
            return null;  //Si esta vacio no puede desencolar nada
        }

        if(efectivo==1){  //Si solo tiene un elemento
            E temp=arreglo[0];  //Guardo la raiz que voy a sacar
            arreglo[0]=null; //La raiz se va

            efectivo=efectivo-1;  //Se reduce el efectivo

            return temp;  //Retorna la raiz que se desencolo

        }

        //Caso en el que haya mas elementos en el HEAP

        E temp= arreglo[0];  //La raiz
        arreglo[0]= arreglo[efectivo-1];  //Ahora la raiz tiene el contenido del ultimo elemento
        arreglo[efectivo-1]=null;  //Vaciamos el ultimo elemento

        ajustar(0); //Ajustamos el Heap desde la nueva raiz

        efectivo=efectivo-1;  //Reducimos el tamaño efectivo del arreglo

        return temp; //Retorna la raiz que desencolamos

    }


    //REVISAAR
    //METODO ENCOLAR
    //Al añadir un nuevo elemento del Heap, se añade al final -El utlimo nivel a la derecha
    //Y de ahi se revisa la posicion ideal para ese elemento
    //Pregunta si el padre es mayor o menor
    //Si es menor el padre, hacemos un switch

    public void encolar(E nuevo) {

        // 1. Verificar espacio
        if (efectivo == max) {
            System.out.println("Heap lleno, no se puede insertar");
            return;
        }

        // 2. Insertar en la última posición disponible
        arreglo[efectivo] = nuevo;

        // 3. Guardar índice donde se insertó
        int actual = efectivo;

        // 4. Aumentar la cantidad de elementos
        efectivo++;

        // 5. HEAPIFY UP (ajustar hacia arriba)
        int padre = posPadre(actual);

        while (padre != -1) {

            // Si es MaxHeap: comparar hijo > padre
            // Si es MinHeap: comparar hijo < padre

            boolean debeSubir =
                    (isMax && cmp.compare(arreglo[actual], arreglo[padre]) > 0) ||
                            (!isMax && cmp.compare(arreglo[actual], arreglo[padre]) < 0);

            if (!debeSubir) break; // Ya está en lugar correcto

            //else
            swap(actual, padre); // Subir el nodo
            actual = padre;      // Nuevo índice
            padre = posPadre(actual);
        }
    }


}

















