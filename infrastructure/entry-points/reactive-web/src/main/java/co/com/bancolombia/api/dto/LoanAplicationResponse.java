package co.com.bancolombia.api.dto;

import java.util.Date;

public record LoanAplicationResponse(
        Integer idSolicitud,
        Double monto,
        Date plazo,
        String email,
        String Estado,
        String TipoPrestamo) {

}
