package utn.programacion3.agencia_de_autos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import utn.programacion3.agencia_de_autos.dto.response.ErrorResponseDto;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailExists(EmailAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflicto de datos", ex.getMessage(), request, null);
    }

    // ATREPADOR DE EXCEPCIONES DE NOT FOUND
    @ExceptionHandler({
            ResourceNotFoundException.class,
            ModeloNoEncontradoException.class,
            VehiculoNoEncontradoException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "No encontrado",
                ex.getMessage(),
                request,
                null
        );
    }

    // ATREPADOR DE EXCEPCIONES DE LOGICA DE NEGOCIO
    @ExceptionHandler({
            NegocioException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNegocioException(NegocioException ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Error de negocio",
                ex.getMessage(),
                request,
                null
        );
    }

    // ATREPADOR DE BAD REQUEST
    @ExceptionHandler({
            PatenteDuplicadaException.class,
            TransaccionNoModificableException.class,
            VehiculoNoDisponibleException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Error de " + request.getContextPath(),
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(f -> fieldErrors.put(f.getField(), f.getDefaultMessage()));

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                "Los datos enviados no son correctos",
                request,
                fieldErrors
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidPassword(InvalidPasswordException ex, WebRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                ex.getMessage(),
                request,
                null
        );
    }

    // ATRAPADOR DE ACCESOS DENEGADOS
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("error", "Forbidden");
        response.put("message", "Permisos insuficientes para acceder a este recurso.");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // MANEJADOR GENERAL (Cualquier error inesperado en el servidor)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        System.out.println(ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, // Código 500
                "Error interno del servidor",
                "Ocurrió un error inesperado en el sistema. Por favor, intente más tarde.",
                request,
                null
        );
    }

    // ATRAPA LOS ERRORES GENERADOS POR EL DESERIALIZADOR (POR EJEMPLO UN enum MAL ESCRITO EN EL BODY)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, org.springframework.web.context.request.WebRequest request) {

        String mensajeError = "Error en el formato de la petición JSON.";


        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "BAD REQUEST: " + mensajeError,
                ex.getLocalizedMessage(),
                request,
                null
        );
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status,
                                                                String errorTitle, String message, WebRequest request, Map<String, String> validationErrors)
    {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                status.value(),
                errorTitle,
                message,
                request.getDescription(false).replace("uri=", "")
        );

        errorDto.setValidationErrors(validationErrors);

        return new ResponseEntity<>(errorDto, status);
    }
}
