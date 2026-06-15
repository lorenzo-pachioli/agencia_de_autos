package utn.programacion3.agencia_de_autos.exception;

public class VehiculoNoEncontradoException extends RuntimeException {
    public VehiculoNoEncontradoException() {
        super("Vehiculo no encontrado");
    }
}
