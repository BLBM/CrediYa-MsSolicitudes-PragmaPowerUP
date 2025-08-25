package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("solicitud")
public class LoanAplicationEntity {

    @Id
    @Column("id_solicitud")
    private Integer idSolicitud;

    private Double monto;

    private Date plazo;

    private String email;

    @Column("id_estado")
    private Integer estado;

    @Column("id_tipo_prestamo")
    private Integer tipoPrestamo;
}
