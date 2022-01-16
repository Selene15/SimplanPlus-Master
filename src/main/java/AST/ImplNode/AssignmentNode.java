package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class AssignmentNode implements Node {

    LhsNode lhsNode;
    ExpNode exp;

    //  Costruttore
    public AssignmentNode(Node lhsNode, Node exp) {
        this.lhsNode = (LhsNode) lhsNode;
        this.exp = (ExpNode) exp;
    }

    // Type Checking
    @Override
    public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
        TypeExp t1 = lhsNode.checkType(t);
        if(!(exp.checkType(t).equals(t1))) {
            throw new TypeCheckingException("Errore nell'assegnamento", exp.checkType(t), t1);
        }
        return new TypeExp(TypeValue.VOID);
    }

    // Analisi degli effetti
    @Override
    public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
        exp.checkEffects(t);
        lhsNode.checkEffects(t);
    }

    // Generazione del codice
    @Override
    public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
        //Costruzione della stringa output
        StringBuilder s = new StringBuilder();
        StringBuilder getAR = new StringBuilder();
        StringBuilder pointers = new StringBuilder();
        LhsNode tmpLhs = lhsNode;
        // Costruzione della catena degli accessi in memoria ai puntatori
        s.append(exp.generateCode(t));
        while (tmpLhs.id == null){
            tmpLhs = tmpLhs.nxt_ptr;
            pointers.append(" lw\n");
        }
        // Costruzione della catena statica per gli accessi alle variabili
        OffsetSymTable tmp = t;
        while (tmp != null && !tmp.getTableType().containsKey(tmpLhs.id)){
            getAR.append(" lw\n");
            tmp = tmp.getPrev();
        }
        // Stringa di output
        s.append(" sfp\n").append(getAR).append(" push").append(" ").append(tmp.getOffset(tmpLhs.id)).append("\n").append(" sub\n").append(pointers).append(" sw\n");

        return s.toString();
    }

    // Stampa dell'AST
    @Override
    public String toPrint(String indent) {
        return indent + "Assignment: \n" + lhsNode.toPrint(indent+"-") + exp.toPrint(indent+"-");
    }
}
