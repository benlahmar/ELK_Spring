/**
 * 
 */
package com.example.demo.test;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BEN LAHMAR
 *
 */
@RestController
public class Test1 {

	@Autowired
	RestHighLevelClient client;
	
	@GetMapping("/abc")
	public String  mm()
	{
		String r = "";
		GetRequest req=new GetRequest("bankoperation2", "100");
		
		SearchRequest rr=new SearchRequest();
		SearchSourceBuilder sb=new SearchSourceBuilder();
		sb.query(QueryBuilders.rangeQuery("age").gte(20));
		
		try {
			GetResponse res = client.get(req, RequestOptions.DEFAULT);
			System.out.println(res.getSourceAsString());
			r=res.getSourceAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	
}
