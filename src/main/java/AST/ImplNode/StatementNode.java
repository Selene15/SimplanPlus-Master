package AST.ImplNode;

import AST.Node;
import Utils.EffectsSymTable;
import Utils.OffsetSymTable;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;
import Utils.SymTable;
import Utils.TypeExp;

public class StatementNode implements Node {
    // Tipi di statement possibili
    public enum Type{
        assignment,
        deletion,
        print,
        ret,
        ite,
        call,
        block
    }
    // Nodo attuale
    Node subtree;
    // Tipo del nodo
    Type node_type;
    // Costruttore
    public StatementNode(Node subtree, Type node_type) {
        this.subtree = subtree;
        this.node_type = node_type;
    }
    // Generazione del codice
    @Override
    public String generateCode(OffsetSymTable t) throws IdNotDefinedException{
        StringBuilder s = new StringBuilder();
        switch (node_type) {
            case block -> {
                s.append(subtree.generateCode(t));
            }
            case assignment, deletion, print, ret, ite, call -> s.append(subtree.generateCode(t));
        }
        return s.toString();
    }
    // Stampa l'AST
    @Override
    public String toPrint(String indent) {
        switch (node_type) {
            case assignment, deletion, print, ret, ite, call, block -> {
                return indent + "Statement: \n" + subtree.toPrint(indent + "-");
            }
        }
        return "";
    }
    // Type checking
    @Override
    public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
        TypeExp type = null;
        switch (node_type){
            case assignment, deletion, print, ret, ite, call, block ->  type = subtree.checkType(t);
        }
        return type;
    }
    // Analisi degli effetti
    @Override
    public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
        switch (node_type) {
            case assignment, deletion, print, ret, ite, call, block -> subtree.checkEffects(t);
        }
    }
}
