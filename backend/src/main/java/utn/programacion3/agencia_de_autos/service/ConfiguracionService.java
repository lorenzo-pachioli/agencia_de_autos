package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utn.programacion3.agencia_de_autos.dto.request.ConfiguracionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ConfiguracionResponseDTO;
import utn.programacion3.agencia_de_autos.model.Configuracion;
import utn.programacion3.agencia_de_autos.repository.ConfiguracionRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ConfiguracionService {

    public static final BigDecimal PORCENTAJE_MAXIMO_RESERVA = new BigDecimal("50");
    private static final BigDecimal PORCENTAJE_SENIA_POR_DEFECTO = new BigDecimal("10");

    private final ConfiguracionRepository configuracionRepository;

    @Transactional(readOnly = true)
    public ConfiguracionResponseDTO obtener() {
        return toResponseDTO(obtenerPorcentajeSenia());
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerPorcentajeSenia() {
        return configuracionRepository.findById(Configuracion.ID_UNICO)
                .map(Configuracion::getPorcentaje_senia)
                .orElse(PORCENTAJE_SENIA_POR_DEFECTO);
    }

    @Transactional
    public ConfiguracionResponseDTO actualizar(ConfiguracionRequestDTO dto) {
        Configuracion configuracion = configuracionRepository.save(
                new Configuracion(Configuracion.ID_UNICO, dto.getPorcentaje_senia()));
        return toResponseDTO(configuracion.getPorcentaje_senia());
    }

    private ConfiguracionResponseDTO toResponseDTO(BigDecimal porcentajeSenia) {
        return new ConfiguracionResponseDTO(porcentajeSenia, PORCENTAJE_MAXIMO_RESERVA);
    }
}
