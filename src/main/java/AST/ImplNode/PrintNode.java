package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class PrintNode implements Node{
	private final ExpNode exp;
	// Costruttore
	public PrintNode(Node e) {
		exp=(ExpNode)e;
	}
	// Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
		return exp.generateCode(t) +" print\n pop\n";
	}
	// Stampa l'AST
	@Override
	public String toPrint(String indent) {
		return indent+" Print: \n"+ exp.toPrint(indent + "-");
	}
	// Type Checking
	@Override
	public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
		TypeExp type = exp.checkType(t);
		if(type.getType() == TypeValue.INTEGER || type.getType() == TypeValue.BOOL){
			return new TypeExp(TypeValue.VOID);
		} else throw new TypeCheckingException("Il valore nella print non è valido", type, new TypeExp(TypeValue.INTEGER));
	}
	// Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
		exp.checkEffects(t);
	}

}
