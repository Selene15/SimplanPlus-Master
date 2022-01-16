package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

import java.util.ArrayList;
import java.util.List;

public class CallNode implements Node {

    String id;
    ArrayList<ExpNode> expNodes;

    //Costruttore
    public CallNode(String id, ArrayList<ExpNode> expNodes) {
        this.id = id;
        this.expNodes = expNodes;
    }
    // Generazione del codice
    @Override
    public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
       // Creazione della stringa di output
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" sfp\n");
        stringBuilder.append(" cfp\n");
        //Generazione del codice deli parametri attuali
        for (ExpNode e: expNodes) {
            stringBuilder.append(e.generateCode(new OffsetSymTable(t)));
        }
        // Salta all'etichetta della funzione
        stringBuilder.append(" bf ").append(t.getRef(id)).append("\n");

        //Memorizzazione del return address
        stringBuilder.append(" lra\n");
        //Eliminazione dalla pila degli parametri attuali
        for (ExpNode e: expNodes) {
            stringBuilder.append(" pop\n");
        }
        //Memorizzazione del frame pointer
        stringBuilder.append(" lfp\n");

        return stringBuilder.toString();
    }

    // Stampa dell'AST
    @Override
    public String toPrint(String indent) {
        StringBuilder s = new StringBuilder();
        for (ExpNode e : expNodes)
            s.append(e.toPrint(indent + "-"));
        return indent + "- Funcall: " + id + "\n" + s;
    }
    // Type Checking
    @Override
    public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
        //Struttura dati per memorizzare il tipo dei parametri attuali
        ArrayList<TypeExp> type_l=new ArrayList<>();
        // Type Checking dei parametri attuali
        for (ExpNode e: expNodes)
            type_l.add( e.checkType(t));

       TypeExp t1 = t.getValue(id);

       //Controllo del tipo funzione
       if(! (t1.getType() == TypeValue.FUNCTION)){
           throw new TypeCheckingException("Errore nella funzione",t1 , new TypeExp(TypeValue.FUNCTION));
       }

        TypeExp type_call = new TypeExp(TypeValue.FUNCTION, type_l);

       //Controllo del tipo degli argomenti della funzione
       if(!t1.argsEquals(type_call)){
           throw new TypeCheckingException("Errore negli argomenti della funzione",t1 , new TypeExp(TypeValue.FUNCTION, type_l));
       }

        return t1.getReturnValue();
    }

    // Analisi degli effetti
    @Override
    public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {

        TypeEffects funEffects = t.getValue(id);

        //Controllo degli effetti delle variabili presenti nel codominio della funzione
        for(TypeEffects effects: funEffects.getCoDomain()){
            if(effects.getValue() == EffectsValue.TOP){
                throw new EffectsException("Errore nella chiamata di funzione",effects, new TypeEffects(EffectsValue.DELETE));
            }
        }

        EffectsSymTable sigma1 = t.copy();
        //Creazione di un nuovo ambiente per la memorizzazione degli argomenti passati per riferimento
        EffectsSymTable sigma2 = new EffectsSymTable();
        //Controllo dei parametri attuali
        for(int i = 0; i < expNodes.size(); i++){
            ExpNode e = expNodes.get(i);
            TypeEffects typeEffects = funEffects.getCoDomain().get(i);
            // Passaggio per riferimento
            if(e.lhsNode != null && e.lhsNode.id != null){
                LhsNode lhsNode = e.lhsNode;
                //Calcolo dell'effetto attraverso l'operatore seq
                TypeEffects s = t.getValue(lhsNode.id).seq(typeEffects);
                //Se l'identificatore è già presente nell'ambiente
                if(sigma2.getTableType().containsKey(lhsNode.id)){
                    //Aggiornamento dell'effetto calcolato con l'operatore par
                    sigma2.updateValue(lhsNode.id, s.par(sigma2.getValue(lhsNode.id)));
                } else {
                    // Inserimento deell'effetto s
                    sigma2.addValue(lhsNode.id,s);
                }
                //Controllo che nell'ambiente non ci siano errori
                if(sigma2.getValue(lhsNode.id).getValue() == EffectsValue.TOP) {
                    throw new EffectsException("Errore nella chiamata di funzione", sigma2.getValue(lhsNode.id), new TypeEffects(EffectsValue.DELETE));
                }
            //Passaggio per valore
            } else {
                //Analisi degli effetti del parametro attuale
                e.checkEffects(sigma1);
            }
        }

        sigma2.getTableType().forEach(((id, typeEffects) -> {
            try {
                //Aggiorno l'ambiente esterno con gli effetti precedentementi calcolati dei parametri passati per riferimento
                t.updateValue(id,typeEffects);
            } catch (IdNotDefinedException e) {
                e.printStackTrace();
            }
        }));
    }


}
