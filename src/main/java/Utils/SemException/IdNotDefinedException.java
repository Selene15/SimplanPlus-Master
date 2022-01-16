package Utils.SemException;

public class IdNotDefinedException extends SemanticException{
    public IdNotDefinedException(String id) {
        super("La variabile '" + id + "' non è stata definita oppure la dichiarazione si trova dopo uno statement!");
    }
}
