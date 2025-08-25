package co.com.bancolombia.api.dto;

import java.util.Date;

public record LoanAplicationRequest(
        Integer idSolicitud,
        Double monto,
        Date plazo,
        String email,
        Integer idEstado,
        Integer idTipoPrestamo) {
}
