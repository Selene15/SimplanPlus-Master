package Utils;

import Utils.SemException.IdNotDefinedException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class OffsetSymTable {

    private final HashMap<String, Integer> tableType = new HashMap<>();
    private final HashMap<String, String> tableRef = new HashMap<>();
    private final OffsetSymTable prev;
    private boolean isFunction = false;
    // Costruttore
    public OffsetSymTable() {
        prev = null;
    }
    // Costruttore
    public OffsetSymTable(OffsetSymTable table) {
        prev = table;
    }
    // Costruttore
    public OffsetSymTable(OffsetSymTable table, boolean isFunction) {
        prev = table;
        this.isFunction = true;
    }
    // Costruttore
    public OffsetSymTable(OffsetSymTable table, String scope) {
        prev = table;
    }
    // Predicato che controlla se questa symTable fa riferimento a una funzione
    public boolean isFunction() {
        return isFunction;
    }
    // Permette di ottenere la SymTable precedente
    public OffsetSymTable getPrev() {
        return prev;
    }
    // Permette di ottenere l'hashMap attuale
    public HashMap<String, Integer> getTableType() {
        return tableType;
    }
    // Inserisce un nuovo valore
    public void addValue(String id) {
        try {
            // Controllo che la variabile non sià già dichiarata
            getValueTop(id);
        }catch (IdNotDefinedException e){
            int counter = 1;
            for(Map.Entry<String, Integer> set : tableType.entrySet()){
                counter++;
            }
            tableType.put(id, counter);
        }
    }
    // Controlla se una variabile è già stata dichiarata
    public Integer getValueTop(String id) throws IdNotDefinedException{
        OffsetSymTable tmp = this;

        boolean trovato = tmp.getTableType().containsKey(id);
        if(trovato){
            return tmp.getTableType().get(id);
        }
        throw new IdNotDefinedException(id);
    }
    // Permette di ottenere l'offset di una variabile
    public Integer getOffset(String id) throws IdNotDefinedException {
        OffsetSymTable tmp = this;
        while (tmp != null){
            boolean trovato = tmp.getTableType().containsKey(id);
            if(trovato){
                return tmp.getTableType().get(id);
            }
            tmp = tmp.getPrev();
        }
        throw new IdNotDefinedException(id);
    }
    // Permette di ottenere la label di una funzione
    public String getRef(String id) {
        OffsetSymTable tmp = this;
        while (tmp != null){
            if(tmp.getTableRef().containsKey(id)){
                return tmp.tableRef.get(id);
            }
            tmp = tmp.getPrev();
        }
        return null;
    }
    // Permette di ottenere l'hashmap con le label alle funzioni
    public HashMap<String, String> getTableRef() {
        return tableRef;
    }
}
