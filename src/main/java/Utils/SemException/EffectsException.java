package Utils.SemException;

import Utils.TypeEffects;

public class EffectsException extends SemanticException{

    public EffectsException(String msg, TypeEffects actual, TypeEffects expected) {
        super(msg + "\nValore precedente: "+ expected + "\nValore successivo: "+ actual);
    }

    public EffectsException(String msg) {
        super(msg);
    }
}
