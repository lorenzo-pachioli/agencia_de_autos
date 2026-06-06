package utn.programacion3.agencia_de_autos.exception;

public class VehiculoNoDisponible extends RuntimeException {
    public VehiculoNoDisponible(String message) {
        super(message);
    }
}

// La cree para el TransactionService porque no vi si en la rama de Emi habia alguno.