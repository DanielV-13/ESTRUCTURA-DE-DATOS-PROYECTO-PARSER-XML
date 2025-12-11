//Imports de las estructuras de datos para que el arbol funcione
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodoXML {

    //Atributos
    private String nombreEtiqueta;
    private Map<String, String> atributos;      //Se guardan los atributos como clave - valor
    private String texto;                       // contenido textual del nodo
    private NodoXML padre;                      // referencia al Padre de ese Nodo
    private List<NodoXML> hijos;                // defino una lista de hijos (porque es ARBOL multihijos)

    //Constructor
    public NodoXML(String nombreEtiqueta) {
        this.nombreEtiqueta = nombreEtiqueta;
        this.atributos = new HashMap<>(); //Creo un hashmap para los atributos
        this.texto = "";  //Texto vacio
        this.hijos = new ArrayList<>(); //Creo un array list para los hijos de un nodo
    }

    // Métodos para agregar hijos y atributos
    public void addHijo(NodoXML son) {
        son.padre = this;  //Asigno el nodo actual como PADRE del nuevo nodo
        hijos.add(son); //Añado el nuevo nodo a la lista de hijos del nodo actual
    }

    public void addAtributo(String clave, String valor) {
        atributos.put(clave, valor); //Agrego un atributo
    }


    // GETTERS y SETTERS
    public void setNombreEtiqueta(String nombreEtiqueta){
        this.nombreEtiqueta=nombreEtiqueta;
    }

    public String getNombreEtiqueta(){
        return nombreEtiqueta;
    }

    public void setTexto(String texto){
        this.texto=texto;
    }

    public String getTexto(){
        return texto;
    }

    public NodoXML getPadre(){
        return padre;
    }

    public List<NodoXML> getHijos(){
        return hijos;
    }


    //---------------OTROS METODOS---------

    //Metodo toString para Nodos
    @Override
    public String toString() {
        return "<" + nombreEtiqueta + "> " + texto;
    }


    //Metodo para obtener hijo por NOMBRE
    public List<NodoXML> getHijosPorEtiqueta(String etiqueta) {
        List<NodoXML> resultado = new ArrayList<>();
        //Para cada "elemento" en el arreglo "hijos"
        for (NodoXML i : hijos) {   //Recorro la lista de hijos
            if (i.getNombreEtiqueta().equals(etiqueta)) {
                resultado.add(i);
            }
        }
        return resultado;  //Retorna la lista de nodos que coincidan con la etiqueta
    }

}
