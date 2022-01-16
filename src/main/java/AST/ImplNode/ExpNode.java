package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class ExpNode implements Node {
    // Tipi di espressioni
    public enum Type{
        baseExp,
        negExp,
        notExp,
        derExp,
        newExp,
        binExp,
        callExp,
        boolExp,
        valExp,
        voidExp
    }

    Integer value;
    CallNode cn = null;
    Boolean aBoolean;
    String op = null;
    LhsNode lhsNode = null;
    Type node_type;
    Node lft = null;
    Node rgt = null;
    TypeExp typeExp;


    // Costruttore
    public ExpNode(Type node_type) {
        this.node_type = node_type;
    }

    // Costruttore per booleani
    public ExpNode(Type node_type, Boolean aBoolean) {
        this.aBoolean = aBoolean;
        this.node_type = node_type;
    }

    // Costruttore per variabili e puntatori
    public ExpNode(Type node_type, LhsNode lhsNode) {
        this.lhsNode = lhsNode;
        this.node_type = node_type;
    }

    // Costruttore per valori interi
    public ExpNode(Type node_type, Integer value) {
        this.value = value;
        this.node_type = node_type;
    }

    // Costruttore per chiamata di funzione
    public ExpNode(Type node_type, CallNode cn) {
        this.cn = cn;
        this.node_type = node_type;
    }

    // Costruttore per espressioni a sinistra
    public ExpNode(Type node_type, Node lft) {
        this.node_type = node_type;
        this.lft = lft;
    }

    // Costruttore per espressioni di tipi
    public ExpNode(Type node_type, TypeExp typeExp) {
        this.node_type = node_type;
        this.typeExp = typeExp;
    }

    // Costruttore per espressioni con operatori binari
    public ExpNode(Type node_type, Node lft, Node rgt, String op) {
        this.node_type = node_type;
        this.lft = lft;
        this.rgt = rgt;
        this.op = op;
    }

    // Generazione del codice
    @Override
    public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
        StringBuilder stringBuilder = new StringBuilder();
        switch (node_type) {
            //(exp)
            case baseExp -> stringBuilder.append(lft.generateCode(t));
            //-exp
            case negExp -> {
                stringBuilder.append(" ").append(" push 0\n");
                stringBuilder.append(lft.generateCode(t));
                stringBuilder.append(" ").append("sub\n");
            }
            //!exp
            case notExp -> stringBuilder.append(" ").append("not\n");
            //variabili e puntatori
            case derExp -> stringBuilder.append(lhsNode.generateCode(t));
            //new
            case newExp -> stringBuilder.append(" lhp\n");
            // exp op exp
            case binExp -> {
                stringBuilder.append(lft.generateCode(t));
                stringBuilder.append(rgt.generateCode(t));
                switch (op) {
                    case "==" -> stringBuilder.append(" eq\n");
                    case "!=" -> stringBuilder.append(" neq\n");
                    case "*" -> stringBuilder.append(" mult\n");
                    case "/" -> stringBuilder.append(" div\n");
                    case "+" -> stringBuilder.append(" add\n");
                    case "-" -> stringBuilder.append(" sub\n");
                    case "<" -> stringBuilder.append(" less\n");
                    case ">" -> stringBuilder.append(" more\n");
                    case "<=" -> stringBuilder.append(" lesseq\n");
                    case ">=" -> stringBuilder.append(" moreq\n");
                    case "&&" -> stringBuilder.append(" and\n");
                    case "||" -> stringBuilder.append(" or\n");
                }
            }
            // Chiamata di funzione
            case callExp -> {
                stringBuilder.append(cn.generateCode(t));
                stringBuilder.append(" srv\n");
            }
            // Bool
            case boolExp -> {
                stringBuilder.append(" ").append("push");
                if(aBoolean){
                    stringBuilder.append(" ").append(String.valueOf(1));
                    stringBuilder.append("\n");
                } else {
                    stringBuilder.append(" ").append(String.valueOf(0));
                    stringBuilder.append("\n");
                }
            }
            // Integer
            case valExp -> {
                stringBuilder.append(" ").append("push");
                stringBuilder.append(" ").append(String.valueOf(value));
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    // Stampa dell'AST
    @Override
    public String toPrint(String indent) {
        switch (node_type){
            //(exp)
            case baseExp -> {
                return indent + " Exp: \n" + lft.toPrint(indent + "-");
            }
            //-exp
            case negExp -> {
                return indent + " Exp Neg: \n" + lft.toPrint(indent + "-");
            }
            //!exp
            case notExp -> {
                return indent + " Exp Not: \n" + lft.toPrint(indent + "-");
            }
            //variabili e puntatori
            case derExp -> {
                return indent + " Exp LHS: \n" + lhsNode.toPrint(indent + "-");
            }
            //new
            case newExp -> {
                return indent + " Exp New: new\n";
            }
            // exp op exp
            case binExp -> {
                return indent + " Exp Bin:\n" +indent + "- Op: " + op+ " \n" + lft.toPrint(indent + "-") + rgt.toPrint(indent + "-");
            }
            // Chiamata di funzione
            case callExp -> {
                return indent + " Exp Call: \n" + cn.toPrint(indent + "-");
            }
            // Bool
            case boolExp -> {
                return indent + " Exp Bool: " + aBoolean + "\n";
            }
            // Integer
            case valExp -> {
                return indent + " Exp Integer: " + value + "\n";
            }
        }

        return null;
    }

    //Type Checking
    @Override
    public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
        // Poi faccio il return di questa var in fondo
        // E' giusto una cosa estetica
        TypeExp type = null;
        switch (node_type){
            //(exp)
            case baseExp -> {
                type = lft.checkType(t);
            }
            //-exp
            case negExp -> {
                type = lft.checkType(t);
                if(type.getType() == TypeValue.INTEGER){
                    type = new TypeExp(TypeValue.INTEGER);
                } else {
                    throw new TypeCheckingException("Errore con " + op +" operatore", type, new TypeExp(TypeValue.INTEGER));
                }
            }
            //!exp
            case notExp -> {
                type = lft.checkType(t);
                if(type.getType() == TypeValue.BOOL){
                    type = new TypeExp(TypeValue.BOOL);
                } else {
                    throw new TypeCheckingException("Errore con " + op +" operatore", type, new TypeExp(TypeValue.BOOL));
                }
            }
            // variabili e puntatori
            case derExp -> {
                type = lhsNode.checkType(t);
            }
            //new
            case newExp -> {
                type = new TypeExp(TypeValue.POINTER, typeExp);
            }
            // exp op e
            case binExp -> {
                switch (op) {
                    case "&&", "||" -> {
                        TypeExp t1 = lft.checkType(t);
                        TypeExp t2 = rgt.checkType(t);

                        if (t1.equals(t2) && t1.getType().equals(TypeValue.BOOL)) {
                            type = new TypeExp(TypeValue.BOOL);
                        } else {
                            throw new TypeCheckingException("Errore con " + op + " operatore", t1, t2);
                        }
                    }
                    case "+", "-", "*", "/" -> {
                        TypeExp t1 = lft.checkType(t);
                        TypeExp t2 = rgt.checkType(t);
                        if (t1.equals(t2) && t1.getType() == TypeValue.INTEGER) {
                            type = new TypeExp(TypeValue.INTEGER);
                        } else {
                            throw new TypeCheckingException("Errore con " + op + " operatore", t1, t2);
                        }
                    }
                    default -> {
                        TypeExp t1 = lft.checkType(t);
                        TypeExp t2 = rgt.checkType(t);
                        if (t1.equals(t2)) {
                            type = new TypeExp(TypeValue.BOOL);
                        } else {
                            throw new TypeCheckingException("Errore con " + op + " operatore", t1, t2);
                        }
                    }
                }
            }
            // Chiamata di funzione
            case callExp -> type = cn.checkType(t);
            // Bool
            case boolExp -> type = new TypeExp(TypeValue.BOOL);
            // Integer
            case valExp -> type = new TypeExp(TypeValue.INTEGER);
            // Void
            case voidExp -> type = new TypeExp(TypeValue.VOID);
        }
        return type;
    }

    // Analisi degli effetti
    @Override
    public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
        // Espressione a sinistra
        if(lft != null){
            lft.checkEffects(t);
        }
        // Espressione a destra
        if(rgt != null){
            rgt.checkEffects(t);
        }
        // CallNode
        if(cn != null){
            cn.checkEffects(t);
        }
        // LhsNode
        if(lhsNode != null){
            lhsNode.checkEffects(t);
        }
    }


}
