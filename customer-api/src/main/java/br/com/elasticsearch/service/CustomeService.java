package br.com.elasticsearch.service;

import br.com.elasticsearch.domain.Customer;
import br.com.elasticsearch.mapper.CustomerMapper;
import br.com.elasticsearch.repository.CustomerRepository;
import br.com.elasticsearch.repository.entity.CustomerEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomeService {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private static final String CUSTOMER_INDEX = "customerindex";

    public Customer registerCustomerWithRepository(Customer customer) {
        final CustomerEntity customerEntity = customerMapper.toCustomerEntity(customer);
        final CustomerEntity entity = repository.save(customerEntity);
        return customerMapper.toCustomer(entity);
    }

    public String registerCustomerWithElasticseartRestTemplate(Customer customer) {
        final CustomerEntity customerEntity = customerMapper.toCustomerEntity(customer);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(customerEntity.getId())
                .withObject(customerEntity).build();
        return elasticsearchOperations.index(indexQuery, IndexCoordinates.of(CUSTOMER_INDEX));
    }


    public void registerCustomerIndexBulkWithRepository() {
        List<Customer> respose = new ArrayList<>();
        String line;
        try {
            final File file = new ClassPathResource("Employees50K.json").getFile();
            try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
                while ((line = bfr.readLine()) != null) {
                    final Customer customer = objectMapper.readValue(line, Customer.class);
                    respose.add(customer);
                }
            }
            repository.saveAll(customerMapper.toCustomerEntityList(respose));
        } catch (Exception e) {
            log.error("Deu ruim: {}", e);
        }
    }

    public void registerCustomerIndexBulkWithElasticseartRestTemplate() {
        List<IndexQuery> indexQueries = new ArrayList<>();
        String line;
        try {
            final File file = resourceLoader.getResource("classpath:/Employees50K.json").getFile();
            try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
                while ((line = bfr.readLine()) != null) {
                    final Customer customer = objectMapper.readValue(line, Customer.class);
                    final CustomerEntity customerEntity = customerMapper.toCustomerEntity(customer);
                    final IndexQuery indexQuery = new IndexQueryBuilder().withId(customerEntity.getId()).withObject(customerEntity).build();
                    indexQueries.add(indexQuery);
                }
            }
            elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(CUSTOMER_INDEX));
        } catch (Exception e) {
            log.error("Deu ruim: {}", e);
        }
    }


    public List<Customer> findCustomerByDesignation(String designation) {
        final List<CustomerEntity> entities = repository.findByDesignation(designation);
        return customerMapper.toCustomerList(entities);
    }

    public List<Customer> findCustomerByLastName(String lastName) {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("lastName", lastName);
        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder).build();
        final SearchHits<CustomerEntity> searchHits = elasticsearchOperations.search(searchQuery, CustomerEntity.class, IndexCoordinates.of(CUSTOMER_INDEX));
        return searchHits
                .stream().map(s -> customerMapper.toCustomer(s.getContent()))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomerByAddress(String address) {
        Query searchQuery = new StringQuery(
                "{\"match\":{\"address\":{\"query\":\"" + address + "\"}}}\"");
        final SearchHits<CustomerEntity> searchHits = elasticsearchOperations
                .search(searchQuery, CustomerEntity.class, IndexCoordinates.of(CUSTOMER_INDEX));
        return searchHits.stream()
                .map(s -> customerMapper.toCustomer(s.getContent()))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomerByRangeSalary(Double startSalary, Double endSalary) {
        Criteria criteria = new Criteria("salary")
                .between(startSalary, endSalary);
        Query searchQuery = new CriteriaQuery(criteria);
        final SearchHits<CustomerEntity> searchHits = elasticsearchOperations
                .search(searchQuery, CustomerEntity.class, IndexCoordinates.of(CUSTOMER_INDEX));
        return searchHits
                .stream()
                .map(s -> customerMapper.toCustomer(s.getContent())).collect(Collectors.toList());
    }

    public List<Customer> processSearchName(String nameOrLastName) {
        // 1. Create query on multiple fields enabling fuzzy search
        QueryBuilder queryBuilder = QueryBuilders
                .multiMatchQuery(nameOrLastName, "firstName", "lastName")
                .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();
        // 2. Execute search
        final SearchHits<CustomerEntity> searchHits = elasticsearchOperations
                .search(searchQuery, CustomerEntity.class, IndexCoordinates.of(CUSTOMER_INDEX));

        // 3. Map searchHits to customer list
        return searchHits
                .stream()
                .map(s -> customerMapper.toCustomer(s.getContent())).collect(Collectors.toList());
    }

    public List<String> processSearchNameSuggestions(String query) {

        final BoolQueryBuilder should = QueryBuilders.boolQuery()
                .should(new WildcardQueryBuilder("firstName", query + "*")).boost(2f)
                .should(new WildcardQueryBuilder("lastName", query + "*"));


        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(should)

                .withPageable(PageRequest.of(0, 5))
                .build();

        SearchHits<CustomerEntity> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        CustomerEntity.class,
                        IndexCoordinates.of(CUSTOMER_INDEX));

        return searchSuggestions.getSearchHits()
                .stream()
                .map(s -> s.getContent().getFirstName()+" "+ s.getContent().getLastName())
                .collect(Collectors.toList());
    }

}

