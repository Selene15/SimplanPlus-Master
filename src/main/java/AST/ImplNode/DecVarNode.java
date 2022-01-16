package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class DecVarNode implements Node {

	  private final String id;
	  private final TypeNode type;
	  private Node exp;
	  // Costruttore dichiarazione con espressione
	  public DecVarNode (String i, Node t, Node v) {
	    id=i;
	    type=(TypeNode)t;
	    exp=v;
	  }
	  // Costruttore dichiarazione senza espressione
	  public DecVarNode (String i, Node t) {
		    id=i;
		    type=(TypeNode)t;
	  }
	  // Stampa l'AST
	  public String toPrint(String indent) {
		  
		  if(exp!=null) {
			return indent+"Var:" + id +"\n"
			  	   +type.toPrint(indent+"  ")  
		           +exp.toPrint(indent+"  "); 
		  }
		  
		  return indent+"Var:" + id +"\n"
	  	   +type.toPrint(indent+"  "); 
	  }
	// Type Checking
	@Override
	public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
	  	// Se l'espressione è impostata
	  	if(exp != null){
			TypeExp t1 = exp.checkType(t);
			if(t1.equals(type.getPtr_type())){
				t.addValue(id, type.getPtr_type());
			} else {
				throw new TypeCheckingException("Errore nella dichiarazione", t1, type.getPtr_type());
			}
		} else {
	  		// Se l'espressione non lo è
			t.addValue(id, type.getPtr_type());
		}
	  	return new TypeExp(TypeValue.VOID);
	}
	// Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
	  	if(exp != null){
			exp.checkEffects(t);
			t.addValue(id, new TypeEffects(EffectsValue.RW));
			return;
		}
		t.addValue(id, new TypeEffects(EffectsValue.BOTTOM));
	}
	// Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
		StringBuilder s = new StringBuilder();
		// Aggiungo l'offset
		t.addValue(id);
		if(exp!=null) {
			s.append(exp.generateCode(t));
		} else {
			s.append(" ").append("push 34\n");
		}
		return s.toString();
	}
}
