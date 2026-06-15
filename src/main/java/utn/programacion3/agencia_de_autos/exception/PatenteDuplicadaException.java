package utn.programacion3.agencia_de_autos.exception;

public class PatenteDuplicadaException extends RuntimeException {
    public PatenteDuplicadaException() {
        super("La patente ya existe");
    }
}
