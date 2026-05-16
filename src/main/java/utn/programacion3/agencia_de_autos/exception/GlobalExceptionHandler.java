package utn.programacion3.agencia_de_autos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import utn.programacion3.agencia_de_autos.dto.response.ErrorResponseDto;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailExists(EmailAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Conflicto de datos", ex.getMessage(), request, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "No encontrado", ex.getMessage(), request, null);
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
