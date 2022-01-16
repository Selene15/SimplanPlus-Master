package AST.ImplNode;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class RetNode implements Node{
	private final ExpNode exp;
	// Costruttore
	public RetNode(Node e) {
		exp=(ExpNode)e;
	}
	// Genera il codice
	@Override
	public String generateCode(OffsetSymTable t) throws IdNotDefinedException{
		StringBuilder s = new StringBuilder();
		// Se una espressione è settata allora genero il suo codice
		if(exp != null){
			s.append(exp.generateCode(t));
		} else {
			s.append(" ").append("push 34");
		}
		// Imposta il RV
		s.append(" ").append("lrv\n");
		// Elimino gli AR fino alla funzione più vicina (o alla chiusura del programma)
		OffsetSymTable tmp = t;
		while (tmp.getPrev() != null && !tmp.isFunction()){
			s.append(" lra\n").append(" pop\n".repeat(tmp.getTableType().size())).append(" lfp\n");
			tmp = tmp.getPrev();
		}
		// Se il return è dentro una funzione
		if(tmp.isFunction()){
			s.append(" sra\n").append(" js\n");
		}
		// Se il return è globale
		if(tmp.getPrev() == null){
			s.append(" srv\n").append(" halt\n");
		}

		return s.toString();
	}
	// Stampa l'AST
	@Override
	public String toPrint(String indent) {
		if(exp==null) {
			return indent + " Return \n";
		}
		else {
			return indent + " Return: \n" + exp.toPrint(indent + "-");
		}
	}
	// Type checking e aggiunge alla lista relativa all'ultima funzione dichiarata il tipo del return
	@Override
	public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
		TypeExp returnValue = exp.checkType(t);
		if(GeneratedLabel.retFunValue.size() > 0){
			GeneratedLabel.retFunValue.get(GeneratedLabel.retFunValue.size() - 1).add(returnValue);
		}
		return returnValue;
	}
	// Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
		if(exp != null)
			exp.checkEffects(t);
	}
}
