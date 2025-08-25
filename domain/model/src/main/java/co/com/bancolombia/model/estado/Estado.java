package co.com.bancolombia.model.estado;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Estado {

    private Integer idEstado;
    private String nombre;
    private String descripcion;
}
