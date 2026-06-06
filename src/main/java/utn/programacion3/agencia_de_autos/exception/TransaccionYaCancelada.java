package utn.programacion3.agencia_de_autos.exception;

public class TransaccionYaCancelada extends RuntimeException {
    public TransaccionYaCancelada(String message) {
        super("La transaccion " + message + " ya esta cancelada");
    }
}
