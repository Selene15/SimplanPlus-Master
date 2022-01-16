package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class IteNode implements Node {

    ExpNode cond;
    StatementNode stm1;
    StatementNode stm2;
    // Costruttore
    public IteNode(Node cond, Node stm1, Node stm2) {
        this.cond = (ExpNode) cond;
        this.stm1 = (StatementNode) stm1;
        this.stm2 = (StatementNode) stm2;
    }
    // Generazione del codice
    @Override
    public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
        StringBuilder s = new StringBuilder();
        String l1 = GeneratedLabel.generate_label(null);
        // Se è presente il ramo else
        if(stm2 != null){
            String l2 = GeneratedLabel.generate_label(null);
            return cond.generateCode(t)+
                    "push 1\n"+
                    "beq "+ l1 +"\n"+
                    stm2.generateCode(t)+
                    "b " + l2 + "\n" +
                    l1 + ":\n"+
                    stm1.generateCode(t)+
                    l2 + ":\n";
        } else {
            // Se non è presente il ramo else
            return cond.generateCode(t)+
                    "push 1\n"+
                    "bneq "+ l1 +"\n"+
                    stm1.generateCode(t)+
                    l1 + ":\n";
        }
    }
    // Stampo l'AST
    @Override
    public String toPrint(String indent) {
        return indent + "Ite: \n" + indent+ "- Cond: \n" + cond.toPrint(indent + "-") + indent + "- Stm1: \n" + stm1.toPrint(indent + "-") + indent + "- Stm2: \n" + stm1.toPrint(indent + "-");
    }
    // Type Checking
    @Override
    public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
        // Controllo che la condizione abbia tipo BOOL
        if(!(cond.checkType(t).getType() == TypeValue.BOOL)) {
            throw new TypeCheckingException("Errore nella condizione dell'If", cond.checkType(t), new TypeExp(TypeValue.BOOL));
        }

        // Controllo se è settatto il ramo else e se i tipi coincidono
        if (stm2 != null && !stm1.checkType(t).equals(stm2.checkType(t)) ){
            throw new TypeCheckingException("Errore nei rami dell'If", stm1.checkType(t), stm2.checkType(t));
        }

        return new TypeExp(TypeValue.VOID);
    }

    // Analisi degli effetti
    @Override
    public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
        EffectsSymTable tmp = t;
        // Controllo gli effetti nella condizione e nei due blocci
        cond.checkEffects(t);
        // Costruisco \Sigma1
        EffectsSymTable s1 = t.copy();
        // Costruisco \Sigma2
        EffectsSymTable s2 = t.copy();
        stm1.checkEffects(s1);
        if(stm2 != null){
            stm2.checkEffects(s2);
        }
        // Calcolo il max degli ambienti e aggiorno t
        while (tmp != null){
            tmp.getTableType().forEach((s, typeEffects) -> {
                try {
                    TypeEffects effect;
                    if(s1.getValue(s).getCoDomain() != null){
                        effect = new TypeEffects(s1.getValue(s).getCoDomain());
                    } else {
                        effect = new TypeEffects(s1.getValue(s).max(s2.getValue(s)));
                    }
                    if(effect.getValue() == EffectsValue.TOP){
                        throw new EffectsException("Errrore nell'if", effect, new TypeEffects(EffectsValue.DELETE));
                    }
                    t.updateValue(s, effect);
                } catch (IdNotDefinedException | EffectsException e) {
                    e.printStackTrace();
                }
            });
            tmp = tmp.getPrev();
        }
    }


}
