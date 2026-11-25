package utn.programacion3.agencia_de_autos.exception;

public class TransaccionNoModificableException extends RuntimeException {
    public TransaccionNoModificableException(String message) {
        super(message);
    }
}
