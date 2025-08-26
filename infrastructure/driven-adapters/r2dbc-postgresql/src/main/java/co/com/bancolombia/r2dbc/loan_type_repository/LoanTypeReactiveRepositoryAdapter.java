package co.com.bancolombia.r2dbc.loan_type_repository;


import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.loan_type.gateways.LoanTypeRepository;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanTypeReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    LoanType/* change for domain model */,
    LoanTypeEntity/* change for adapter model */,
    Integer,
    LoanTypeReactiveRepository
> implements LoanTypeRepository {
    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class/* change for domain model */));
    }
}
