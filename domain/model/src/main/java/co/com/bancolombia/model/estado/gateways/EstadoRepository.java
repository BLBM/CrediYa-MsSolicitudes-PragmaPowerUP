package co.com.bancolombia.model.estado.gateways;


import co.com.bancolombia.model.estado.Estado;
import reactor.core.publisher.Mono;

public  interface EstadoRepository
{
    Mono<Estado> findById(Integer id);
}