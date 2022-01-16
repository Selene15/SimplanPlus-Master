package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

//deletion: 'delete' ID; 
public class DeletionNode implements Node{

	private final String id;

	//  Costruttore
	public DeletionNode (String s) {
		id=s;
	}

	// Stampa dell'AST
	@Override
	public String toPrint(String indent) {
		return indent+"Delete: "+ id;
	}

	// Type Checking
	@Override
	public TypeExp checkType(SymTable t) throws IdNotDefinedException, TypeCheckingException {
		if(t.getValue(id).getType() != TypeValue.POINTER){
			throw new TypeCheckingException("Errore nella delete", t.getValue(id), new TypeExp(TypeValue.POINTER));
		}
		return new TypeExp(TypeValue.VOID);
	}

	// Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException{
		if(!(t.getValue(id).subTyping(EffectsValue.RW))){
			throw new EffectsException("Errore nell'analisi degli effetti", t.getValue(id), new TypeEffects(EffectsValue.DELETE));
		}
		t.updateValue(id, new TypeEffects(EffectsValue.DELETE));

	}

	// Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) {
		// Non fa nulla
		return "";
	}
}
