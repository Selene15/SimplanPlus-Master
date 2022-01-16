package Utils;


import AST.ImplNode.ArgNode;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;

import java.util.ArrayList;
import java.util.HashMap;

public class SymTable {

    private final HashMap<String, TypeExp> tableType = new HashMap<>();
    private SymTable prev = null;
    private String scope = null;
    public SymTable() {
        prev = null;
    }

    public SymTable(SymTable table) {
        prev = table;
    }

    public SymTable(SymTable table, String scope) {
        prev = table;
        this.scope = scope;
    }

    public SymTable getPrev() {
        return prev;
    }

    public HashMap<String, TypeExp> getTableType() {
        return tableType;
    }

    public void addValue(String id, TypeExp value) {
        try {
            // Controllo che la variabile non sià già dichiarata
            getValueTop(id);
        }catch (IdNotDefinedException e){
            // Se non lo è la aggiungo
            tableType.put(id, value);
        }
    }

    public TypeExp getValueTop(String id) throws IdNotDefinedException{
        SymTable tmp = this;

        boolean trovato = tmp.getTableType().containsKey(id);
        if(trovato){
            return tmp.getTableType().get(id);
        }
        throw new IdNotDefinedException(id);
    }

    public TypeExp getValue(String id) throws IdNotDefinedException {
        SymTable tmp = this;
        while (tmp != null){
            boolean trovato = tmp.getTableType().containsKey(id);
            if(trovato){
                return tmp.getTableType().get(id);
            }
            tmp = tmp.getPrev();
        }
        throw new IdNotDefinedException(id);
    }

    public String getId(String id){
        SymTable tmp = this;
        while (tmp != null){
            if(tmp.getTableType().containsKey(id)){
                return tmp.scope + "_" + id;
            }
            tmp = tmp.getPrev();
        }
        return null;
    }

    public boolean removeId(String id){
        SymTable tmp = this;
        while (tmp != null){
            if(tmp.getTableType().containsKey(id)){
                tmp.getTableType().remove(id);
                return true;
            }
            tmp = tmp.getPrev();
        }
        return false;
    }

    public String getScope() {
        return scope;
    }
}
