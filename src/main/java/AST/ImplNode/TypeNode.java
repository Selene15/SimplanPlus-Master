package AST.ImplNode;

import AST.Node;
import Utils.EffectsSymTable;
import Utils.OffsetSymTable;
import Utils.SymTable;
import Utils.TypeExp;

public class TypeNode implements Node{

	//private TypeNode ptr_type;
	private final TypeExp ptr_type;
	// Costruttore, prende in input il tipo del nodo
	public TypeNode(TypeExp t){
		ptr_type = (TypeExp) t;
	}
	// Stampa l'AST
	public String toPrint(String indent) {
		return indent+"Type: " + ptr_type +"\n";
	}
	// Type checking
	@Override
	public TypeExp checkType(SymTable t) {
		return ptr_type;
	}
	// Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) {
		// Non fa nulla
	}
	// Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) {
		// Ritorna vuoto
		return "";
	}
	// Getter del tipo
	public TypeExp getPtr_type() {
		return ptr_type;
	}
}
