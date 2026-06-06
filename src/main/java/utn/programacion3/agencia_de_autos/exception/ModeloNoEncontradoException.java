package utn.programacion3.agencia_de_autos.exception;

public class ModeloNoEncontradoException extends RuntimeException {
    public ModeloNoEncontradoException() {
        super("Modelo no encontrado");
    }
}
