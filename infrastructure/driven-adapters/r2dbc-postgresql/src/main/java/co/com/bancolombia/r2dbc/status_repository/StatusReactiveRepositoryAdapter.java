package co.com.bancolombia.r2dbc.status_repository;


import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import co.com.bancolombia.r2dbc.entity.StatusEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StatusReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Status/* change for domain model */,
    StatusEntity/* change for adapter model */,
    Integer,
    StatusReactiveRepository
>implements StatusRepository {
    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Status.class/* change for domain model */));
    }

}
