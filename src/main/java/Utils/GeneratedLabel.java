package Utils;

import java.util.ArrayList;

public class GeneratedLabel {
    // Lista con tutti i tipi di return di tutte le funzioni
    public static ArrayList<ArrayList<TypeExp>> retFunValue = new ArrayList<>();
    // Id unico per le label
    static int counter = 0;
    // Generazione di una nuova label
    public static String generate_label(String fun_name){
        String salt = "";
        if(fun_name != null){
            salt = fun_name;
            counter++;
            return "label" + salt + counter;
        } else {
            return "label" + counter++;
        }
    }
}
