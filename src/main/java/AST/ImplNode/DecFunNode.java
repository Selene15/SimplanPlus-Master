package AST.ImplNode;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import AST.Node;
import Utils.*;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;

//decFun : (type | 'void') ID '(' (arg (',' arg)*)? ')' block ;
public class DecFunNode implements Node{
	 
	private final String id;
	private TypeNode type;
	private ArgNode arg;
	private final ArrayList<Node> args;
	private final BlockNode block;
	private String type_v;

	
	// Costruttore nel caso in cui f sia bool o int
	public DecFunNode( Node t, String s, List<Node> ar, Node b) {
		id = s;
		type = (TypeNode) t;
		args = new ArrayList<> (ar);
		block = (BlockNode) b;
	}

	//Stampa dell'AST
	@Override
	public String toPrint(String indent) {
		
		StringBuilder argstr= new StringBuilder();
		
		if(type_v!=null) { //void

			for(Node a:args) {
				argstr.append(a.toPrint(indent + " "));
			}

			return indent+ "DecFun:" +"\n"
				+ "Type: void " + id + " ("
				+argstr + " )" +"\n"
				+indent + block.toPrint(indent);
		} else { //int || bool || ^type
			for(Node a:args) {
				argstr.append(a.toPrint(indent + "   "));
			}

			return indent+"DecFun:" +"\n"
				+ type.toPrint(" ") +" "+ id + " (\n"
				+argstr + " )" +"\n"
				+indent + block.toPrint(indent);
		}
	}

	//Type Checking
	@Override
	public TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException {
		//Creazione della lista che contiene i valori dei return presenti nella funzione
		GeneratedLabel.retFunValue.add(new ArrayList<>());
		//Struttura dati per memorizzare i tipi dei parametri formali
		ArrayList<TypeExp> type_arg = new ArrayList<>();
		//Creazione di un nuovo ambiente
		SymTable tFun = new SymTable();
		SymTable tmp = t;
		// Inserimento delle funzioni all'interno dell'ambiente tFun
		while (tmp != null){
			tmp.getTableType().forEach(((s, typeExp) -> {
				if(typeExp.getType() == TypeValue.FUNCTION){
					tFun.addValue(s,typeExp);
				}
			}));
			tmp = tmp.getPrev();
		}

		SymTable t1 = new SymTable(tFun);

		for (Node a: args){
			ArgNode argNode = ((ArgNode) (a));
			//Type checking dei parametri formali
			TypeExp typeExp = argNode.checkType(t1);
			type_arg.add(typeExp);
			t1.addValue(argNode.getId(), typeExp);
		}

		//Memorizzazione del tipo di ritorno della funzione
		TypeExp fun_type = new TypeExp(TypeValue.FUNCTION, type_arg);
		TypeExp return_type = type.checkType(t1);
		//Controllo e salvataggio del return value
		fun_type.setReturnValue(return_type);
		t1.addValue(id, fun_type);

		//Type Checking del corpo della funzione
		block.checkType(t1);

		// Controllo dei return all'interno del blocco
		if(GeneratedLabel.retFunValue.get(GeneratedLabel.retFunValue.size() - 1).size() == 0){
			TypeExp typeExp = new TypeExp(TypeValue.VOID);
			if(!typeExp.equals(return_type))
				throw new TypeCheckingException("Incompatibilità tra tipo di ritorno della dichiarazione di funzione e tipo del corpo della funzione", typeExp, return_type );
		}
		for(TypeExp typeExp: GeneratedLabel.retFunValue.get(GeneratedLabel.retFunValue.size() - 1)){
			if(!typeExp.equals(return_type))
				throw new TypeCheckingException("Incompatibilità tra tipo di ritorno della dichiarazione di funzione e tipo del corpo della funzione", typeExp, return_type );
		}
		// Rimuovo l'ultimo inserito
		GeneratedLabel.retFunValue.remove(GeneratedLabel.retFunValue.get(GeneratedLabel.retFunValue.size() - 1));
		t.addValue(id, fun_type);

		return new TypeExp(TypeValue.VOID);
	}

	//Genera gli effeti dei parametri formali della funzione
	ArrayList<TypeEffects> generateParamsEffects(EffectsSymTable t){
		// Aggiornamento degli effetti dei parametri della funzione
		ArrayList<TypeEffects> effects = new ArrayList<>();

		for(Map.Entry<String, TypeEffects> entry: t.getTableType().entrySet()){
			String key = entry.getKey();
			TypeEffects typeEffects = entry.getValue();
			effects.add(typeEffects);
		}

		return  effects;
	}

	//Analisi delgi effetti
	@Override
	public void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException {
		// Creazione dell'ambiente in cui vengono inseriri gli effetti dei parametri della funzione
		EffectsSymTable sigma0 = new EffectsSymTable(t);
		for (Node a: args){
			ArgNode argNode = ((ArgNode) (a));
			argNode.checkEffects(sigma0);
		}
		// Costruzione dell'ambiente in cui salvo i valori precedenti
		EffectsSymTable sigma1 = sigma0.copy();
		//Inserimento di f nell'ambiente precedente con il suo codominio
		ArrayList<TypeEffects> effects1 = generateParamsEffects(sigma1);
		sigma1.addValue(id, new TypeEffects(effects1));
		//Analisi degli effetti del corpo della funzione
		block.checkEffects(sigma1);

		//Calcolo del punto fisso
		AtomicBoolean stop = new AtomicBoolean(false);
		while(!stop.get()){
			AtomicBoolean trovato = new AtomicBoolean(true);
			// Valutazione del nuovo blocco
			EffectsSymTable sigmaI = sigma0.copy();
			//Inserimento di f nell'ambiente precedente con il suo codominio
			ArrayList<TypeEffects> effects = generateParamsEffects(sigma1);
			sigmaI.addValue(id, new TypeEffects(effects));
			//Analisi degli effetti del corpo della funzione
			block.checkEffects(sigmaI);
			// Controllo che non ci siano cambi di tipo per ogni record nella nuova valutazione
			sigma1.getTableType().forEach((s, typeEffects) -> {
				try {
					if(typeEffects.getValue() != sigmaI.getValue(s).getValue()){
						trovato.set(false);
					}
				} catch (IdNotDefinedException e) {
					e.printStackTrace();
				}
			});
			if(trovato.get()){
				//Punto fisso raggiunto
				stop.set(true);
			}
			sigma1 = sigmaI;
		}

		//Inserisco di f nell'ambiente precedente con il suo codominio aggiornato
		ArrayList<TypeEffects> effects = generateParamsEffects(sigma1);
		t.addValue(id, new TypeEffects(effects));
	}


	//Generazione del codice
	@Override
	public String generateCode(OffsetSymTable t) throws IdNotDefinedException{
		//Creazione della stringa di output
		StringBuilder s = new StringBuilder();
		//Creazione della tabella per la memorizzazione degli offset
		OffsetSymTable t1 = new OffsetSymTable(t, true);

		//Inserimento nella tabella degli offset gli identificatori dei parametri
		for(Node arg:args){
			t1.addValue(((ArgNode) arg).getId());
		}
		//Creazione dell'etichetta della funzione
		String label = GeneratedLabel.generate_label(id);
		t.getTableRef().put(id, label);
		s.append(" ").append(label).append(":\n");
		s.append(" sra\n");
		//Generazione di codice per il corpo della funzione
		s.append(block.generateCode(t1));
		s.append(" sra\n");
		s.append(" js\n");
		return s.toString();

	}
}
