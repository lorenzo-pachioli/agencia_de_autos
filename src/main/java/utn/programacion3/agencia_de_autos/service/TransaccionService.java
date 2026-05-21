package utn.programacion3.agencia_de_autos.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.TransaccionMapper;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.repository.TransaccionRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;
    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;


    private final BigDecimal comision_vendedores = new BigDecimal("0.05");   // 5%
/*    vehiculoService.buscarPorId
    vehiculoService.actualizarEstado
    EstadoVehiculo*/

    //me falta listar transacciones con filtros, actualizar transaccion,



    @Transactional
    public TransaccionResponseDTO crear(TransaccionRequestDTO dto){

        Usuario cliente = usuarioService.buscarPorId(dto.getCliente_id());
        Usuario vendedor = usuarioService.buscarPorId(dto.getVendedor_id());
        Vehiculo vehiculo = vehiculoService.buscarPorId(dto.getVehiculo_id());

        //if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) throw new {excepcio} COMPLETAR CUANDO ESTE VEHICULO

        Transaccion transaccion = transaccionMapper.toEntity(dto);
        transaccion.setCliente(cliente);
        transaccion.setVehiculo(vehiculo);
        transaccion.setVendedor(vendedor);

        // vehiculoService.actualizarEstado(dto.getVehiculo_id(), EstadoVehiculo.RESERVADO);  COMPLETAR CUANDO ESTE VEHICULO

        return transaccionMapper.toResponseDTO(transaccionRepository.save(transaccion));
    }

    @Transactional
    public TransaccionResponseDTO cambiarEstado(Long id, EstadoTransaccion estadoTransaccion){

        Transaccion transaccion = buscarEntityPorId(id);

        asignacionCambioDeEstado(transaccion, estadoTransaccion);

        return transaccionMapper.toResponseDTO(
                transaccionRepository.save(transaccion)
        );
    }

    @Transactional(readOnly = true)
    public TransaccionResponseDTO buscarPorId(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrado con el ID: " + id));
        return transaccionMapper.toResponseDTO(transaccion);
    }

    @Transactional(readOnly = true)
    public Transaccion buscarEntityPorId(Long id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrado con el ID: " + id));
    }

    private void asignacionCambioDeEstado(Transaccion transaccion, EstadoTransaccion nuevoEstado){
        // esto tendria que ser un metodo privado aparte
        if (nuevoEstado == EstadoTransaccion.VENDIDO){
            transaccion.setComision_calculada(
                    transaccion.getPrecio_final().multiply(this.comision_vendedores));           // Calcula automaticamente la comision del vendedor cuando se concreta la venta
            //vehiculoService.actualizarEstado(dto.getVehiculo_id(), EstadoVehiculo.VENDIDO);  COMPLETAR CUANDO ESTE VEHICULO

        } else if (nuevoEstado == EstadoTransaccion.CANCELADO) {
            // ACTUALIZAR ESTADO VEHICULO CUANDO EXISTA EL SERVICIO
            //vehiculoService.actualizarEstado(dto.getVehiculo_id(), EstadoVehiculo.DISPONIBLE); COMPLETAR CUANDO ESTE VEHICULO

        }else {
            //vehiculoService.actualizarEstado(dto.getVehiculo_id(), EstadoVehiculo.RESERVADO);  COMPLETAR CUANDO ESTE VEHICULO
        }

        transaccion.setEstadoTransaccion(nuevoEstado);
    }

}
