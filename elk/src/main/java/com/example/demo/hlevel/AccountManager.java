/**
 * 
 */
package com.example.demo.hlevel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.HistogramValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.example.demo.model.Account;
import com.example.demo.model.Operation;
import com.example.demo.model.Personne;


/**
 * @author BEN LAHMAR
 *
 */
@Service
public class AccountManager implements IAcount{

	ElasticsearchRestTemplate elasticsearchRestTemplate;
	
	

	/**
	 * @param elasticsearchRestTemplate
	 */
	public AccountManager(ElasticsearchRestTemplate elasticsearchRestTemplate) {
		super();
		this.elasticsearchRestTemplate = elasticsearchRestTemplate;
	}

	@Override
	public SearchHits<Account> findByGender(String g) {
		
		
		QueryBuilder sb=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("gender", g)) ;
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		
		return res;
	}

	@Override
	public SearchHits<Account> findByAgeGreaterThan(int age) {
		// TODO Auto-generated method stub
		QueryBuilder sb=QueryBuilders.rangeQuery("age").gte(age);
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		return res;
	}

	@Override
	public SearchHits<Account> findByBalanceBetween(double min, double max, Pageable p) {
		QueryBuilder sb=QueryBuilders.rangeQuery("balance").gte(min).lte(max);
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		
		return res;
		
		
	}

	@Override
	public SearchHits<Account> findByGenderAndAgeLessThan(String g, int age) {
		
		QueryBuilder sb=QueryBuilders.boolQuery()
				
				.must(QueryBuilders.termQuery("gender", g))
				.must(QueryBuilders.rangeQuery("age").gte(age));
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		
		return res;
	}

	@Override
	public Account findByAccountnumber(int nb) {
		QueryBuilder sb=QueryBuilders.matchQuery("account_number", nb);
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		Account c=null;
		if(!res.getSearchHits().isEmpty())
		 c = res.getSearchHits().get(0).getContent();
		return c;
	}

	@Override
	public List<Account> findByOperationsOperationDateBetween(LocalDateTime d1, LocalDateTime d2) {
		QueryBuilder sb=QueryBuilders.rangeQuery("operations.operationDate").gte(d1).lte(d2);
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		return res.stream().map(x-> x.getContent()).collect(Collectors.toList());

	}

	@Override
	public List<Personne> all() {
		QueryBuilder sb=QueryBuilders.matchAllQuery();
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Personne> res = elasticsearchRestTemplate.search(query, Personne.class);
		List<Personne> r = res.stream().map(x->x.getContent()).collect(Collectors.toList());
		return r;
	}

	@Override
	public List<Personne> byaccounrnumber(long acn) {
		QueryBuilder sb=QueryBuilders.matchQuery("account_number", acn);
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Personne> res = elasticsearchRestTemplate.search(query, Personne.class);
		List<Personne> r = res.stream().map(x->x.getContent()).collect(Collectors.toList());
		return r;
	}

	@Override
	public String getadresse(String nom) {
		QueryBuilder sb=QueryBuilders.termQuery("nom", nom);
		NativeSearchQuery query=new NativeSearchQuery(sb);
		SearchHits<Personne> res = elasticsearchRestTemplate.search(query, Personne.class);
		long nb = res.getSearchHits().get(0).getContent().getAccountnumber();
		
		QueryBuilder sb2=QueryBuilders.matchQuery("account_number", nb);
		NativeSearchQuery query2=new NativeSearchQuery(sb2);
		SearchHits<Account> res2 = elasticsearchRestTemplate.search(query2, Account.class);
		String adr="";
		if(res2.getTotalHits()>0)
			adr=res2.getSearchHits().get(0).getContent().getAddress();
		
		return adr;
	}
	
	@Override
	public List<SearchHits<?>> xx( long acc) {
		NativeSearchQueryBuilder q1 = new NativeSearchQueryBuilder();
		q1.withQuery(QueryBuilders.matchQuery("account_number", acc));
		
		NativeSearchQuery qq = q1.build();
	    List<Class<?>> ls=new ArrayList<>();
	    ls.add(Personne.class);
	    ls.add(Account.class);
	    
	    List<Query> lq=new ArrayList<Query>();
	    lq.add(qq);
	    
		IndexCoordinates index=IndexCoordinates.of(Account.INDEX_NAME, Personne.INDEX_NAME);
		List<SearchHits<?>> res = elasticsearchRestTemplate.multiSearch(lq, ls, index);
		return res;
	
}
	//cherche les op d'un compte selon le type : debit, cridit soit les deux 
	@Override
	public List<Operation> getoperation4acc(int numacc, String type) {
		
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("account_number", numacc));
					if(type.equals("debit") || type.equals("cridit"))
				bos.filter(QueryBuilders.termQuery("operations.type", type));
		
		NativeSearchQuery query=new NativeSearchQuery(bos);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		List<Operation> ops = null;
		if(!res.getSearchHits().isEmpty())
		{
			 ops = res.getSearchHits().get(0).getContent().getOperations();
			 if(type.equals("debit") || type.equals("cridit"))
					ops=ops.stream().filter(x-> x.getType().equals(type)).collect(Collectors.toList()) ;
		}
		return ops;
	}

	@Override
	public List<Operation> gethistory(LocalDate dd, LocalDate df) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double maxamount() {
		
		MaxAggregationBuilder agg = AggregationBuilders.max("max4").field("operations.amount");
		HistogramAggregationBuilder dt = AggregationBuilders.histogram("byage").field("age").interval(10);
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchAllQuery());	
		
		NativeSearchQuery query=new NativeSearchQuery(bos);
		
		query.addAggregation(agg);
		query.addAggregation(dt);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		 Max a = res.getAggregations().get("max4");
		 
		return a.getValue();
	}

	//la moyenne des amount des femmes
	
	
	
	@Override
	public SearchHits<Account>  maxcamount() {
		MaxAggregationBuilder agg = AggregationBuilders.max("max4").field("operations.amount");
		HistogramAggregationBuilder dt = AggregationBuilders.histogram("byage").field("age").interval(10);
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchAllQuery());

		NativeSearchQuery query=new NativeSearchQuery(bos);
		query.addAggregation(agg);
		query.addAggregation(dt);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		
		 return res;
		
	}

	@Override
	public double avgamount(String gender) {
		AvgAggregationBuilder agg = AggregationBuilders.avg("avg").field("operations.amount");
		
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("gender", gender));	
		
		NativeSearchQuery query=new NativeSearchQuery(bos);
		
		query.addAggregation(agg);
		
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		 Avg a = res.getAggregations().get("avg");
		 
		return a.getValue();
		
	}

	@Override
	public Map<Double, Long> getbytranchage(int tranche) {
		HistogramAggregationBuilder agg = AggregationBuilders.histogram("byage").field("age").interval(tranche);
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchAllQuery());
		
		NativeSearchQuery query=new NativeSearchQuery(bos);
		query.addAggregation(agg);
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		Histogram h = res.getAggregations().get("byage");
		Map<Double, Long> resultat=new HashMap<>();
		List<? extends Bucket> ps = h.getBuckets();
		for (Bucket b : ps) {
			resultat.put((Double) b.getKey(), b.getDocCount());
			
			
		}
		
		return resultat;
	}

	@Override
	public SearchHits<Account> aggbygendreage(int tranche) {
		List<CompositeValuesSourceBuilder<?>> sources = new ArrayList<>();

	
		
		TermsValuesSourceBuilder t=new TermsValuesSourceBuilder("alpha").field("gender.keyword");
		HistogramValuesSourceBuilder d=new HistogramValuesSourceBuilder("beta").field("age").interval(tranche);
		sources.add(t );
		sources.add(d );
		
		
		CompositeAggregationBuilder agg = AggregationBuilders.composite("byagegender", sources);
		
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchAllQuery());
		
		NativeSearchQuery query=new NativeSearchQuery(bos);
		query.addAggregation(agg);
		
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		CompositeAggregation xx = res.getAggregations().get("byagegender");
		
		System.out.println("*****************"+xx.getType());
		return res;
	}

	@Override
	public SearchHits<Account> aggbygendreage2(int tranche) {
		
		TermsAggregationBuilder alpha = AggregationBuilders.terms("alpha").field("gender.keyword");
		HistogramAggregationBuilder beta = AggregationBuilders.histogram("beta").field("age").interval(tranche);
		alpha.subAggregation(beta);
		
		BoolQueryBuilder bos = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchAllQuery());
		
		NativeSearchQuery query=new NativeSearchQuery(bos);
		query.addAggregation(alpha);
		
		SearchHits<Account> res = elasticsearchRestTemplate.search(query, Account.class);
		
		Terms ag = res.getAggregations().get("alpha");
		ag.getBucketByKey("male").getAggregations().get("beta");
		
		System.out.println("*****************"+ag.getType());
		
		return res;
	}
	
	
}
