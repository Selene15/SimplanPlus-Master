package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;


public class DecNode implements Node{
	
	 public enum Type{
	        decFun,
	        decVar
	 }
	private DecFunNode decFun;
	private DecVarNode decVar;
	private final Type t_node;

	//Costruttore
	public DecNode(Node dec,  Type node_type){
		t_node=node_type;
		if(t_node.toString().equals("decFun")) {
			decFun=(DecFunNode)dec;
		}
		else {
			decVar=(DecVarNode)dec;
		}
	}

	// Stampa dell'AST
	@Override
	public String toPrint(String indent) {
		 switch (t_node){
         case decFun: {
        	 return indent+"Declaration:" +"\n"
      		  	   +decFun.toPrint(indent+"  ");
         }
         case decVar: {
			 return indent+"Declaration:" +"\n"
					 +decVar.toPrint(indent+"  ");
         }
		}
		 return null;
	}

	// Type Checking
	@Override
	public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
		switch (t_node){
			case decFun -> {
				// Type Checking delle dichiarazioni di funzioni
				decFun.checkType(t);
			}
			case decVar -> {
				// Type Checking delle dichiarazioni di variabilie
				TypeExp checkType = decVar.checkType(t);
			}
		}
		return new TypeExp(TypeValue.VOID);
	}

	//Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
		switch (t_node) {
			case decFun -> {
				//Analisi degli effetti delle dichiarazione delle funzioni
				decFun.checkEffects(t);
			}
			case decVar -> {
				//Analisi degli effetti delle dichiarazioni di variabili
				 decVar.checkEffects(t);
			}
		}
	}

	//Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
		StringBuilder s = new StringBuilder();
		//Creazione della stringa di output
		switch (t_node){
			case decFun:
				s.append(decFun.generateCode(t));
				break;
			case decVar:
				s.append(decVar.generateCode(t));
				break;
		}
		return s.toString();
	}

	//Restituisce il nodo relativo alla dichiarazione di funzione
	public DecFunNode getDecFun() {
		return decFun;
	}

	//Restituisce il nodo relativo alla dichiarazione divariabile
	public DecVarNode getDecVar() {
		return decVar;
	}
}
