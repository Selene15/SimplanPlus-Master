package Utils.SemException;

public class IdAlreadyDefinedException extends SemanticException {
    public IdAlreadyDefinedException(String id) {
        super("La variabile " + id + " è già stata definita!");
    }

}
