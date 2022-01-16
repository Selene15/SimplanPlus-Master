package AST;

import Utils.EffectsSymTable;
import Utils.OffsetSymTable;
import Utils.SemException.EffectsException;
import Utils.SemException.IdAlreadyDefinedException;
import Utils.SemException.IdNotDefinedException;
import Utils.SemException.TypeCheckingException;
import Utils.SymTable;
import Utils.TypeExp;

public interface Node {
    // Genera il codice
    String generateCode(OffsetSymTable t) throws IdNotDefinedException;
    // Stampa l'AST
    String toPrint(String indent);
    // Typechecking
    TypeExp checkType(SymTable t) throws TypeCheckingException, IdAlreadyDefinedException, IdNotDefinedException;
    // Analisi degli effetti
    void checkEffects(EffectsSymTable t) throws IdNotDefinedException, EffectsException;
}
