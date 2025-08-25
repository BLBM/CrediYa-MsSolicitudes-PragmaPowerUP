package co.com.bancolombia.model.tipoPrestamo.gateways;

import co.com.bancolombia.model.tipoPrestamo.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> findById(Integer id);
}
