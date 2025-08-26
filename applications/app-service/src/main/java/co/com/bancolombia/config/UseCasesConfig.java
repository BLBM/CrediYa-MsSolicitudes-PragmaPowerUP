package co.com.bancolombia.config;

import co.com.bancolombia.model.status.gateways.EstadoRepository;
import co.com.bancolombia.model.loan_application.gateways.SolicitudRepository;
import co.com.bancolombia.model.loan_type.gateways.TipoPrestamoRepository;
import co.com.bancolombia.usecase.created_loan_application_use_case.GuardarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public GuardarSolicitudUseCase guardarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            TipoPrestamoRepository tipoPrestamoRepository,
            EstadoRepository estadoRepository
    ) {
        return new GuardarSolicitudUseCase(
                solicitudRepository,
                tipoPrestamoRepository,
                estadoRepository
        );
    }
}
