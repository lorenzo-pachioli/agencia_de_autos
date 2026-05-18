package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.TransaccionMapper;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.repository.TransaccionRepository;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;
    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;

    public TransaccionResponseDTO crear(TransaccionRequestDTO dto){

        Usuario cliente = usuarioService.buscarPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.buscarPorId(dto.getVehiculo_id());

        //if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) throw new {excepcio} COMPLETAR CUANDO ESTE VEHICULO
        // AGREGAR MAS VALIDACIONES SI HACE FALTA

        Transaccion transaccion = transaccionMapper.toEntity(dto);
        transaccion.setCliente(cliente);
        transaccion.setVehiculo(vehiculo);
        transaccion.setVendedor(vendedor);

        return transaccionMapper.toDto(transaccionRepository.save(transaccion));
    }

}
