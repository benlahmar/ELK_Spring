/**
 * 
 */
package com.example.demo.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.hlevel.IAcount;
import com.example.demo.model.Account;
import com.example.demo.model.Operation;
import com.example.demo.model.Personne;

/**
 * @author BEN LAHMAR
 *
 */
@RestController
public class HightRest {

		@Autowired
		IAcount am;
	
	@GetMapping("/hight/gender")
	public List<Account> getbyg(@RequestParam(name = "gdr") String g)
	{
		SearchHits<Account> res = am.findByGender(g);
		List<Account> res1 = res.getSearchHits().stream().map(x-> x.getContent()).collect(Collectors.toList());
		return res1;
	}
	
	
	@GetMapping("/hight/age")
	public SearchHits<Account> getbyagg(@RequestParam(name = "value") int  g)
	{
		SearchHits<Account> res = am.findByAgeGreaterThan(g);
		//List<Account> res1 = res.getSearchHits().stream().map(x-> x.getContent()).collect(Collectors.toList());
		return res;
	}
	
	
	@GetMapping("/hight/naccount/{num}")
	public ResponseEntity<Account> getbynumaccount(@PathVariable int  num)
	{
		
		Account res = am.findByAccountnumber(num);
		if(res!=null)
		return new ResponseEntity<Account>(res, HttpStatus.FOUND);
		else return  new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/hight/personnes")
	public List<Personne> all()
	{
		return am.all();
	}
	
	@GetMapping("/hight/personnes/adr/{nom}")
	public String getadr(@PathVariable String  nom)
	{
		return am.getadresse(nom);
	}
	
	@GetMapping("/hight/personnes/{acc}")
	public List<SearchHits<?>> getadr(@PathVariable long  acc)
	{
		List<SearchHits<?>> r = am.xx(acc);
		r.stream().map( x -> x.getSearchHits());
		return r;
	}
	
	@GetMapping("/hight/account/operation/{num}")
	public List<Operation> getopByN(@PathVariable int num ,@RequestParam String type)
	{
		return am.getoperation4acc(num, type);
	}
	
	@GetMapping("/hight/account/agg")
	public   SearchHits<Account> gg()
	{
		return am.maxcamount();
	}
	
	@GetMapping("/hight/account/agg/moy/{gender}")
	public double avgamount(@PathVariable String gender)
	{
		return am.avgamount(gender);
	}
	
	@GetMapping("/hight/account/agg/tranche/{tr}")
	public Map<Double, Long> getbytranchage(@PathVariable int tr)
	{
		return am.getbytranchage(tr);
	}
	
	
	@GetMapping("/hight/account/agg/sub/{tr}")
	public SearchHits<Account> aggbygendreage2(@PathVariable int tr)
	{
		return am.aggbygendreage2(tr);
	}
	
	@GetMapping("/hight/account/agg/comp/{tr}")
	public SearchHits<Account> aggbygendreage(@PathVariable int tr)
	{
		return am.aggbygendreage(tr);
	}
}
