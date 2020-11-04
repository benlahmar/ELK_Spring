/**
 * 
 */
package com.example.demo.hlevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.util.Streamable;

import com.example.demo.model.Account;
import com.example.demo.model.Operation;
import com.example.demo.model.Personne;

/**
 * @author BEN LAHMAR
 *
 */
public interface IAcount {

	public SearchHits<Account> findByGender(String g);
	public SearchHits<Account> findByAgeGreaterThan(int age);
	public SearchHits<Account> findByBalanceBetween(double min, double max, Pageable p);
	public SearchHits<Account> findByGenderAndAgeLessThan(String g, int age);
	
	public Account findByAccountnumber(int nb);
	public List<Account> findByOperationsOperationDateBetween(LocalDateTime d1,LocalDateTime d2 );
	
	public List<Personne> all();
	public List<Personne> byaccounrnumber(long acn );
	
	
	public String getadresse(String nom);
	
	public List<SearchHits<?>> xx( long acc);
	
	public List<Operation> getoperation4acc(int numacc, String type);
	public  List<Operation> gethistory(LocalDate dd, LocalDate df);
	
	public double maxamount();
	public SearchHits<Account>  maxcamount();
	
	public double avgamount(String gender) ;
	
	public Map<Double, Long> getbytranchage(int tranche);
	
	
	public SearchHits<Account> aggbygendreage(int tranche);
	
	public SearchHits<Account> aggbygendreage2(int tranche);
}
