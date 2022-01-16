package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class LhsNode implements Node {

    String id = null;
    LhsNode nxt_ptr;
    // Costruttore se è un id
    public LhsNode(String id) {
        this.id = id;
    }
    // Costruttore se è un puntatore
    public LhsNode(Node nxt_ptr) {
        this.nxt_ptr = (LhsNode) nxt_ptr;
    }
    //Generazione del codice
    @Override
    public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
        StringBuilder s = new StringBuilder();
        StringBuilder getAR = new StringBuilder();
        // Se è una variabile
        if (id != null){
            // Ottenimento della posizione nello stack
            OffsetSymTable tmp = t;
            while (tmp != null && !tmp.getTableType().containsKey(id)){
                getAR.append(" lw\n");
                tmp = tmp.getPrev();
            }
            s.append(" sfp\n").append(getAR).append(" push ").append(tmp.getOffset(id)).append("\n").append(" sub\n").append(" lw\n");
        } else {
            // Se è un puntatore
            s.append(nxt_ptr.generateCode(t));
            s.append(" lw\n");
        }
        return s.toString();
    }
    // Stampa l'AST
    @Override
    public String toPrint(String indent) {
        if(id != null){
            return indent + " LhsNode: " + id + "\n";
        }

        return indent + " LhsNode: \n" + nxt_ptr.toPrint(indent + "-");
    }
    // Type Checking
    @Override
    public TypeExp checkType(SymTable t) throws TypeCheckingException, IdNotDefinedException {
        // Se è un identificatore
        if(id == null){
            TypeExp t1 = nxt_ptr.checkType(t);
            // Dereferenziazione
            if(t1.getType() == TypeValue.POINTER){
                return t1.getPtr_type();
            } else {
                throw new TypeCheckingException("Dereferenziazione fallita", t1, new TypeExp(TypeValue.POINTER));
            }
        } else {
            // Restituisco il tipo della variabilie
            return t.getValue(id);
        }
    }
    // Analisi degli effetti
    @Override
    public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
        // Se è una variabile
        if(id == null){
            nxt_ptr.checkEffects(t);
        } else {
            // Se è un puntatore
            if(!(t.getValue(id).subTyping(EffectsValue.RW))){
                throw new EffectsException("Errore nell'analisi degli effetti della variabile: " + id, new TypeEffects(EffectsValue.RW), t.getValue(id));
            }
            t.updateValue(id, t.getValue(id).seq(new TypeEffects(EffectsValue.RW)));
        }
    }


}
