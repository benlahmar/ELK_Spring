/**
 * 
 */
package com.example.demo.repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Account;

/**
 * @author BEN LAHMAR
 *
 */

public interface IAccountRepo  extends  ElasticsearchRepository<Account, String>{
	public List<Account> findByGender(String g);
	public Page<Account> findByAgeGreaterThan(int age, Pageable p);
	public Page<Account> findByBalanceBetween(double min, double max, Pageable p);
	public Streamable<Account> findByGenderAndAgeLessThan(String g, int age);
	
	public Account findByAccountnumber(int nb);
	public List<Account> findByOperationsOperationDateBetween(LocalDateTime d1,LocalDateTime d2 );   
	
	
	//Chercher les comptes dont les valeurs de amount est > 300
	public List<Account> findByOperationsAmountGreaterThan(double am);
	//Chercher les comptes qui ont effectué une opération de débit >200
	public List<Account> findByOperationsTypeAndOperationsAmountGreaterThan(String type, Double amount);
	
		
	@Query(" {\"match\": { \"age\": ?0} }")
	public Streamable<Account>  findx(int a);

	
}
