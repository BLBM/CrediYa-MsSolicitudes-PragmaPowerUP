package co.com.bancolombia.model.solicitud;
import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.tipoPrestamo.TipoPrestamo;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {

    private Integer idSolicitud;
    private Double monto;
    private Date plazo;
    private String email;
    private Estado estado;
    private TipoPrestamo tipoPrestamo;
}
