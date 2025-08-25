package co.com.bancolombia.r2dbc.loanTypeRepository;

import co.com.bancolombia.model.tipoPrestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoPrestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    TipoPrestamo/* change for domain model */,
    LoanTypeEntity/* change for adapter model */,
    Integer,
    LoanTypeReactiveRepository
> implements TipoPrestamoRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, TipoPrestamo.class/* change for domain model */));
    }
}
