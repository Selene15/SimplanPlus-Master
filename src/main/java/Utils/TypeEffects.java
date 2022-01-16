package Utils;

import java.util.ArrayList;

public class TypeEffects {
    // Valore di un effetto
    private final EffectsValue v;
    // Codominio dell'effetto di una funzione
    private ArrayList<TypeEffects> coDomain;
    // Costruttore
    public TypeEffects(EffectsValue v) {
        this.v = v;
        coDomain = null;
    }
    // Costruttore
    public TypeEffects(ArrayList<TypeEffects> coDomain) {
        v = EffectsValue.BOTTOM;
        this.coDomain = coDomain;
    }
    // Calcolo se due EV sono sottotipi
    public boolean subTyping(EffectsValue v1){
        if(v1 == EffectsValue.BOTTOM && v == EffectsValue.BOTTOM){
            return true;
        }

        if(v1 == EffectsValue.RW && (v == EffectsValue.RW  || v == EffectsValue.BOTTOM)){
            return true;
        }

        if(v1 == EffectsValue.DELETE && ( v == EffectsValue.BOTTOM || v == EffectsValue.RW  || v == EffectsValue.DELETE )){
            return true;
        }

        return v1 == EffectsValue.TOP && (v == EffectsValue.BOTTOM || v == EffectsValue.RW  || v == EffectsValue.DELETE || v == EffectsValue.TOP);
    }
    // Max tra due TypeEffects
    public EffectsValue max(TypeEffects b){
        EffectsValue effectsValue;
        if(this.subTyping(b.getValue())){
            effectsValue = b.getValue();
        } else {
            effectsValue = this.v;
        }
        return  effectsValue;
    }
    // Seq tra due TypeEffects
    public TypeEffects seq(TypeEffects b){
        EffectsValue effectsValue = this.max(b);

        if(effectsValue == EffectsValue.RW || effectsValue == EffectsValue.BOTTOM){
            return new TypeEffects(effectsValue);
        }

        if((this.v == EffectsValue.RW || this.v == EffectsValue.BOTTOM) && b.getValue() == EffectsValue.DELETE){
            return  new TypeEffects(EffectsValue.DELETE);
        }

        if(this.v == EffectsValue.DELETE && b.getValue() == EffectsValue.BOTTOM){
            return new TypeEffects(EffectsValue.DELETE);
        }

        return new TypeEffects(EffectsValue.TOP);
    }
    // Par tra due TypeEffects
    public TypeEffects par(TypeEffects b){
        if(v == EffectsValue.BOTTOM){
            return new TypeEffects(b.getValue());
        }

        if(b.getValue() == EffectsValue.BOTTOM){
            return  new TypeEffects(v);
        }

        if(v == EffectsValue.RW && b.getValue() == EffectsValue.RW){
            return new TypeEffects(EffectsValue.RW);
        }

        return new TypeEffects(EffectsValue.TOP);
    }
    // Restituisce il valore dell'effetto
    public EffectsValue getValue() {
        return v;
    }
    // Stampa dell'effetto come stringa
    @Override
    public String toString() {
        switch (v){

            case BOTTOM -> {
                return "BOTTOM";
            }
            case RW -> {
                return "RW";
            }
            case DELETE -> {
                return "DELETE";
            }
            case TOP -> {
                return "TOP";
            }
        }
        return super.toString();
    }
    // Restituisce il codominio
    public ArrayList<TypeEffects> getCoDomain() {
        return coDomain;
    }
    // Imposta il codominio
    public void setCoDomain(ArrayList<TypeEffects> list){
        this.coDomain = list;
    }

}
