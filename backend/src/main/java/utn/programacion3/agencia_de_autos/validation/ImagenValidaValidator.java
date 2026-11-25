package utn.programacion3.agencia_de_autos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImagenValidaValidator implements ConstraintValidator<ImagenValida, String> {

    private static final Pattern CABECERA = Pattern.compile("^data:image/(jpeg|png|webp);base64,");

    // Primeros bytes con los que empieza cada formato
    private static final byte[] FIRMA_JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] FIRMA_PNG = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
    private static final byte[] FIRMA_RIFF = {'R', 'I', 'F', 'F'};
    private static final byte[] FIRMA_WEBP = {'W', 'E', 'B', 'P'};
    private static final int POSICION_FIRMA_WEBP = 8;

    @Override
    public boolean isValid(String valor, ConstraintValidatorContext context) {
        if (valor == null || valor.isBlank()) {
            return true; // de esto se ocupa @NotBlank
        }

        Matcher cabecera = CABECERA.matcher(valor);
        if (!cabecera.find()) {
            return false;
        }

        try {
            // Se mira el contenido: la cabecera sola se puede falsear renombrando un archivo
            return esFormatoPermitido(Base64.getDecoder().decode(valor.substring(cabecera.end())));
        } catch (IllegalArgumentException e) {
            return false; // base64 mal formado
        }
    }

    private boolean esFormatoPermitido(byte[] contenido) {
        return tieneFirma(contenido, 0, FIRMA_JPEG)
                || tieneFirma(contenido, 0, FIRMA_PNG)
                || (tieneFirma(contenido, 0, FIRMA_RIFF) && tieneFirma(contenido, POSICION_FIRMA_WEBP, FIRMA_WEBP));
    }

    private boolean tieneFirma(byte[] contenido, int desde, byte[] firma) {
        int hasta = desde + firma.length;
        return contenido.length >= hasta && Arrays.equals(contenido, desde, hasta, firma, 0, firma.length);
    }
}
