package AST.ImplNode;


import java.util.ArrayList;
import java.util.List;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

public class BlockNode implements Node{
	
	private final ArrayList<Node> declist;
	private final ArrayList<Node> cmdlist;
	
	//Costruttore
	public BlockNode (List<Node> decl, List<Node> cmdl) {
		declist=new ArrayList<> (decl);
		cmdlist=new ArrayList<> (cmdl);
	}

	// Stampa dell'AST
	public String toPrint(String indent) {
		StringBuilder s1= new StringBuilder();
		StringBuilder s2= new StringBuilder();

		for(Node dec:declist)
			s1.append(dec.toPrint(indent + " "));

		for(Node cmd:cmdlist)
			s2.append(cmd.toPrint(indent + " "));

		return indent + "Block:" + "\n" + s1 + "\n" + s2;
	}

	// Type Checking
	@Override
	public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
		SymTable s1 = new SymTable(t);
		// Type checking  sulle dichiarazioni
		for(Node n: declist){
			n.checkType(s1);
		}
		// Type checking sui comandi
		for(Node n:cmdlist){
			n.checkType(s1);

		}
		// Restituisce VOID
		return new TypeExp(TypeValue.VOID);
	}

	//  Analisi degli effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
		EffectsSymTable t1 = new EffectsSymTable(t);

		// Analisi degli effetti sulle dichiarazioni
		for(Node n:declist){
			n.checkEffects(t1);
		}

		// Analisi degli effetti sui comandi
		for(Node n:cmdlist){
			n.checkEffects(t1);
		}

	}

	// Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) throws IdNotDefinedException {
		OffsetSymTable t1 = new OffsetSymTable(t);
		// Creazione stringa di output
		StringBuilder s = new StringBuilder();
		// Nuova label
		String label1 = GeneratedLabel.generate_label(null);

		// Creazione dell' AR
		s.append(" sfp\n");
		s.append(" cfp\n");
		// Generazione codice delle dichiarazioni di variabili
		for(Node dec:declist){
			DecNode decNode = (DecNode) dec;
			if(decNode.getDecVar() != null){
				s.append(dec.generateCode(t1));
			}
		}
		//
		s.append(" sra\n");
		// Salto al corpo del blocco
		s.append(" b ").append(label1).append("\n");
		// Generazione del codice delle dichiarazioni
		for(Node dec:declist){
			DecNode decNode = (DecNode) dec;
			if(decNode.getDecFun() != null){
				s.append(dec.generateCode(t1));
			}
		}
		// Label con il corpo della funzione
		s.append(" ").append(label1).append(":\n");
		for(Node cmd:cmdlist)
			s.append(cmd.generateCode(t1));
		s.append(" lra\n");
		// Pulisco lo stack
		s.append(" pop\n".repeat(t1.getTableType().size()));
		s.append(" lfp\n");

		return s.toString();
	}
	
}
