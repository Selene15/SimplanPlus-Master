package Utils.SemException;

public class SemanticException extends Exception{

    String msg;

    public SemanticException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
