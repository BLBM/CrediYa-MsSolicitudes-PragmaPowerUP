package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record LoanAplicationResponse(
        Integer idSolicitud,
        Double monto,
        Date plazo,
        String email,
        @Schema(defaultValue = "1", description = "estado de revision")
        String Estado,
        @Schema(defaultValue = "2", description = "prestamo solicitado")
        String TipoPrestamo) {

}
