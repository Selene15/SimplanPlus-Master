package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeExp {

    TypeValue type = null;

    // Tipo puntatore
    private TypeExp ptr_type = null;

    // Lista degli argomenti delle funzioni
    private ArrayList<TypeExp> args = new ArrayList<>();

    // Valore di ritorno delle funzioni
    TypeExp returnValue = null;

    // Costruttore
    public TypeExp(TypeValue type) {
        this.type = type;
    }

    // Costruttore con argomenti
    public TypeExp(TypeValue type, ArrayList<TypeExp> args) {
        this.type = type;
        this.args = args;
    }

    // Costruttore con puntatori
    public TypeExp(TypeValue type, TypeExp ptr_type) {
        this.type = type;
        this.ptr_type = ptr_type;
    }

    // Costruttore con argomenti
    public TypeExp(List<TypeExp> args) {
        type =  TypeValue.FUNCTION;
        this.args = new ArrayList<>(args);
    }

    // Ritorna un tipo puntatore
    public TypeExp getPtr_type() {
        return ptr_type;
    }

    // Imposta il valore di ritorno della funzione
    public void setReturnValue(TypeExp returnValue) {
        this.returnValue = returnValue;
    }

    // Restituzione del valore di ritorno
    public TypeExp getReturnValue() {
        return returnValue;
    }

    // Costruzione della stringa per stampare le eccezioni
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(TypeExp t: args){
            s.append(t).append(',');
        }
        return "TypeExp{" +
                "type=" + type +
                ", ptr=''" + ptr_type + "'}" +
                ", return=''" + returnValue + "'}" +
                ", list='" + s.toString() + "'}";
    }

    // Verifica dell'uguaglianza dei tipi dei parametri attuali e formali
    public boolean argsEquals(TypeExp type1){
        if (type == type1.type && type == TypeValue.FUNCTION) {
            return args.equals(type1.args);
        }

        return false;
    }

    // Controllo dell'uguaglianza dei tipi
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeExp type1 = (TypeExp) o;
        // Se è un tipo puntatore
        if(type == type1.type && type == TypeValue.POINTER){
            return ptr_type.equals(type1.ptr_type);
        } else if (type == type1.type && type == TypeValue.FUNCTION) {
            // Se è un tipo funzione
            return args.equals(type1.args) && returnValue.equals(type1.returnValue);
        } else {
            // Se è INTEGER o BOOL
            return type == type1.type;
        }
    }
    // The hashCode method is an inbuilt method that returns the integer hashed value of the input value
    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    //Ritorna un TypeValue
    public TypeValue getType() {
        return type;
    }
}
