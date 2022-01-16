package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;

public class ArgNode implements Node{

	private final Node type;
	private final String id;
	
	public ArgNode(Node t,String s) {
		type=t;
		id=s;
	}

	// Stampa dell'AST
	public String toPrint(String indent) {
		  return indent+"Arg:" + id +"\n"
				 +type.toPrint(indent) ; 
	}

	// Type checking
	@Override
	public TypeExp checkType(SymTable t) throws IdAlreadyDefinedException {
		return ((TypeNode)type).checkType(t);
	}

	// Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
		t.addValue(id, new TypeEffects(EffectsValue.BOTTOM));
	}

	@Override
	// Generazione del codice
	public String generateCode(OffsetSymTable t) {
		return "";
	}

	// Restituisce l'identificatore
	public String getId() {
		return id;
	}
}
