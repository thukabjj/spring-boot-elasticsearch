package br.com.elasticsearch.repository;

import br.com.elasticsearch.repository.entity.CustomerEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends ElasticsearchRepository<CustomerEntity, String> {
    List<CustomerEntity> findByFirstName(String firstName);
    List<CustomerEntity> findByLastName(String lastName);
    List<CustomerEntity> findByDesignation(String designation);
    List<CustomerEntity> findByAgeAndSalary(Integer age, Double salary);
}
