package utn.programacion3.agencia_de_autos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Exige una imagen JPG, PNG o WEBP codificada como data URI en base64. */
@Documented
@Constraint(validatedBy = ImagenValidaValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImagenValida {

    String message() default "Solo se permiten imágenes JPG, PNG o WEBP";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
