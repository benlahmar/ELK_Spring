/**
 * 
 */
package com.example.demo.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Account;

import com.example.demo.repo.IAccountRepo;

/**
 * @author BEN LAHMAR
 *
 */
@RestController
public class AccountRest {

	@Autowired
	IAccountRepo ia;
	
	@GetMapping(path = "/chercher/{id}")
	public ResponseEntity<Account> chercherparid(@PathVariable String id)
	{
		ResponseEntity<Account> rep; 
		 Optional<Account> r = ia.findById(id);
		if(r.isPresent())
		{
			rep=new ResponseEntity<Account>(r.get(), HttpStatus.FOUND);
			return rep;
		}
		else
			return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
			
	}
	
	@GetMapping(path = "/chercher")
	public List<Account> all()
	{
		List<Account> cs=new ArrayList<>();
		Iterable<Account> res = ia.findAll();
		res.forEach(x-> cs.add(x));
		return cs;
	}
	
	
	@GetMapping(path = "/chercher/gendre")
	public List<Account> bygendre(@RequestParam(name = "gdr") String g)
	{
		return ia.findByGender(g);
	}
		
	
	@GetMapping(path = "/chercher/age/{ag}")
	public Page<Account> byage(@PathVariable(name = "ag") int age, @RequestParam int page)
	{		
		Pageable p=PageRequest.of(page, 5);
		Page<Account> res = ia.findByAgeGreaterThan(age, p);
		return res;
	}
	
	@GetMapping(path = "/chercher/balance")
	public Page<Account> byabalance(@RequestParam(name = "min") double min, @RequestParam(name = "max") double max, @RequestParam int page)
	{		
		Pageable p=PageRequest.of(page, 5);
		Page<Account> res = ia.findByBalanceBetween(min, max,p);
		return res;
	}
	
	@GetMapping(path = "/chercher/date/{i}/{j}")
	public List<Account> byaccountnb(@PathVariable(name = "i") String d1, @PathVariable(name = "j") String d2)
	{
		LocalDateTime dd1 = LocalDateTime.parse(d1);
		LocalDateTime dd2 = LocalDateTime.parse(d2);
		List<Account> res = ia.findByOperationsOperationDateBetween(dd1,dd2);
		
		return res;
	}
	
	@GetMapping(path = "/chercher/x/{a}")
	public Streamable<Account> query(@PathVariable int a)
	{
		Streamable<Account> r = ia.findx(a);
		
		return r;
	}
	
	
	@GetMapping(path = "/chercher/y/{a}")
	public List<Account> getmaxamount(@PathVariable  double a)
	{
		List<Account> acs = ia.findByOperationsAmountGreaterThan(a);
		
		List<Account> res = acs.stream()
				.filter(x-> x.getOperations()
						.stream().allMatch(o-> o.getAmount() >a))
				.collect(Collectors.toList());
		return res;
	}
}
