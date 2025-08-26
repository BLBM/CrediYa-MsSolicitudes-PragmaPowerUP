package co.com.bancolombia.r2dbc.status_repository;

import co.com.bancolombia.model.status.Estado;
import co.com.bancolombia.model.status.gateways.EstadoRepository;
import co.com.bancolombia.r2dbc.entity.StatusEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Estado/* change for domain model */,
    StatusEntity/* change for adapter model */,
    Integer,
    StatusReactiveRepository
>implements EstadoRepository {
    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Estado.class/* change for domain model */));
    }

}
