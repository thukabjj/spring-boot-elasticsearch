package br.com.elasticsearch.mapper;

import br.com.elasticsearch.domain.Customer;
import br.com.elasticsearch.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerEntity toCustomerEntity(Customer customer);
    Customer toCustomer(CustomerEntity customerEntity);

    List<CustomerEntity> toCustomerEntityList(List<Customer> customerList);
    List<Customer> toCustomerList(List<CustomerEntity> customerEntityList);
}
