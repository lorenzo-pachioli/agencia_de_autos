package utn.programacion3.agencia_de_autos.mapper;

import utn.programacion3.agencia_de_autos.dto.request.MarcaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.MarcaResponseDTO;
import utn.programacion3.agencia_de_autos.model.Marca;

public interface MarcaMapper {
    Marca toEntity(MarcaRequestDTO dto);

    MarcaResponseDTO toResponse(Marca marca);
}
