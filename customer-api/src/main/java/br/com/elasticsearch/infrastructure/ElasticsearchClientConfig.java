package br.com.elasticsearch.infrastructure;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages
        = "br.com.elasticsearch.repository")
@ComponentScan(basePackages = { "br.com.elasticsearch." })
public class ElasticsearchClientConfig extends
        AbstractElasticsearchConfiguration {

    @Value("${spring.data.rest.uris}")
    private String elasticsearchRestUri;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration =
                ClientConfiguration
                        .builder()
                        .connectedTo(elasticsearchRestUri)
                        .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
