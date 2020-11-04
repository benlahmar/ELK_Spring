/**
 * 
 */
package com.example.demo.model;
/**
 * 
 */

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;




/**
 * @author BEN LAHMAR
 *
 */

public class Operation implements Serializable {

	double amount;
	String type;
	LocalDateTime operationDate;
	
	
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDateTime getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(LocalDateTime operationDate) {
		this.operationDate = operationDate;
	}
	
	@Override
	public String toString() {
		return "Operation [amount=" + amount + ", type=" + type + ", operationDate=" + operationDate + "]";
	}
}
