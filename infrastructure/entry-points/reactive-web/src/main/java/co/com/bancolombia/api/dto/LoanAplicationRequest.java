package co.com.bancolombia.api.dto;


import java.util.Date;

public record LoanAplicationRequest(
        Double monto,
        Date plazo,
        String email,
        Integer idTipoPrestamo) {
}
