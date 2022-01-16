package Utils;

import Utils.SemException.IdNotDefinedException;

import java.util.*;

public class EffectsSymTable {

    private final LinkedHashMap<String, TypeEffects> tableType = new LinkedHashMap<>();
    private EffectsSymTable prev = null;
    private String scope = null;
    // Costruttore
    public EffectsSymTable() {
        prev = null;
    }
    // Costruttore
    public EffectsSymTable(EffectsSymTable table) {
        prev = table;
    }
    // Costruttore
    public EffectsSymTable(EffectsSymTable table, String scope) {
        prev = table;
        this.scope = scope;
    }
    // Permette di ottenere la symTable precedente
    public EffectsSymTable getPrev() {
        return prev;
    }
    // Permette di ottenere l'attuale HashMap
    public LinkedHashMap<String, TypeEffects> getTableType() {
        return tableType;
    }
    // Aggiunge un nuovo valore nella symTable
    public void addValue(String id, TypeEffects value) {
        // Il controllo degli identificatori lo faccio a tempo di TypeChecking
        // Qui non mi serve farlo
        tableType.put(id, value);
    }
    // Aggiorna il valore di id nella tabella
    public void updateValue(String id, TypeEffects value) throws IdNotDefinedException {
        EffectsSymTable tmp = this;
        while (tmp != null){
            boolean trovato = tmp.getTableType().containsKey(id);
            if(trovato){
                tmp.getTableType().put(id, value);
                return;
            }
            tmp = tmp.getPrev();
        }
        throw new IdNotDefinedException(id);
    }
    // Ottengo il valore dell'id
    public TypeEffects getValue(String id) throws IdNotDefinedException {
        EffectsSymTable tmp = this;
        while (tmp != null){
            boolean trovato = tmp.getTableType().containsKey(id);
            if(trovato){
                return tmp.getTableType().get(id);
            }
            tmp = tmp.getPrev();
        }
        throw new IdNotDefinedException(id);
    }
    // Crea una copia della symTable
    public EffectsSymTable copy(){
        ArrayList<EffectsSymTable> effectsSymTables = new ArrayList<>();
        EffectsSymTable tmp = this;
        while (tmp != null){
            effectsSymTables.add(tmp);
            tmp = tmp.getPrev();
        }
        Collections.reverse(effectsSymTables);
        EffectsSymTable t = null;
        for (EffectsSymTable e: effectsSymTables) {
            EffectsSymTable t1 = new EffectsSymTable(t);
            e.getTableType().forEach(t1::addValue);
            t = t1;
        }
        return t;
    }
}
