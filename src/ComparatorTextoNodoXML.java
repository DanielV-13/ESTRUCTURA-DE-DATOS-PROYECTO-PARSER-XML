//Este comparator se usara en el HEAP, para comparar por texto
import java.util.Comparator;

public class ComparatorTextoNodoXML implements Comparator<NodoXML> {

    @Override
    public int compare(NodoXML a, NodoXML b) {

        String ta;
        String tb;

        if (a.getTexto() == null) {
            ta = "";
        } else {
            ta = a.getTexto();}

        if (b.getTexto() == null) {
            tb = "";
        } else {
            tb = b.getTexto();
        }

        return ta.compareToIgnoreCase(tb); //Metodo para comparar Strings
    }
}