package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("tipo_prestamo")
public class LoanTypeEntity {

    @Id
    @Column("id_tipo_prestamo")
    private Integer idTipoPrestamo;
    private String nombre;
    @Column("monto_minimo")
    private Double montoMinimo;
    @Column("monto_maximo")
    private Double montoMaximo;
    @Column("tasa_interes")
    private Double tasaInteres;
    @Column("validacion_automatica")
    private Boolean validacionAutomatica;
}
