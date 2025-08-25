package co.com.bancolombia.config;

import co.com.bancolombia.model.estado.gateways.EstadoRepository;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoPrestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.usecase.guardarsolicitud.GuardarSolicitudUseCase;
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
