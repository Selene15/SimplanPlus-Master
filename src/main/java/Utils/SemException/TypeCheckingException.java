package Utils.SemException;


import Utils.TypeExp;

public class TypeCheckingException extends SemanticException {
    public TypeCheckingException(String msg, TypeExp actual, TypeExp expected) {
        super(msg + "\nTipo previsto: "+ expected + "\nTipo trovato: "+ actual);
    }

    public TypeCheckingException(String msg) {
        super(msg);
    }
}
