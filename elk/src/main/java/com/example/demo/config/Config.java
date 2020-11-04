/**
 * 
 */
package com.example.demo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * @author BEN LAHMAR
 *
 */
@Configuration
public class Config extends AbstractElasticsearchConfiguration{

	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		
		ClientConfiguration cf= ClientConfiguration.builder().connectedTo("localhost:9200").build();
		RestHighLevelClient client = RestClients.create(cf).rest();
		
		return client;
	}
	
	
	
		@Bean
	    ElasticsearchRestTemplate elasticsearchRestTemplate() {
	        return new ElasticsearchRestTemplate(elasticsearchClient());
	    }
	
}
