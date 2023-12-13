package com.ecommercedb.service;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.ecommercedb.customException.InvalidValueException;
import com.ecommercedb.dto.DeliveryDetails;
import com.ecommercedb.dto.OrderDetails;
import com.ecommercedb.dto.PaymentDetails;
import com.ecommercedb.repo.DeliveryDetailsRepo;
import com.ecommercedb.repo.OrderDetailsRepo;
import com.ecommercedb.repo.PaymentDetailsRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class OrderService {
	
	@Autowired
	KafkaTemplate<Integer, String> kafkaTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	DeliveryDetailsRepo deliveryDetailsRepo;
	
	@Autowired
	PaymentDetailsRepo paymentDetailsRepo;
	
	@Autowired
	OrderDetailsRepo orderDetailsRepo;
	

	public String processOrder(ConsumerRecord<Integer, String> consumerRecord) throws InvalidValueException {
			try {
			JSONObject json = new JSONObject(consumerRecord.value());
			validate(json);
			save(consumerRecord,json);
			log.info("Successfully saved data to db: "+json.toString());
			return "Successfully saved";
			}
			catch(JsonMappingException e) {
				throw new InvalidValueException("Error while mapping json value: "+e.getMessage(),e);
			}
			catch(IllegalArgumentException e) {
				throw new InvalidValueException("Invalid argument in object: "+e.getMessage(),e);
			}
			catch(JsonProcessingException e) {
				throw new InvalidValueException("Error while processing json object:  "+e.getMessage(),e);
			}
			

	}
	public void validate(JSONObject json) {
		if(json.optIntegerObject("id")==0 || json.optIntegerObject("id")==null) {
			CompletableFuture<SendResult<Integer, String>> result = kafkaTemplate.send("failureTopic", json.toString());
			log.error("id is missing, saved to dlq : "+result);
			
			throw new IllegalArgumentException("id is missing");
		} 
		log.info("Validation is successful");
		
	}
	public void save(ConsumerRecord<Integer,String> consumerRecord,JSONObject json) throws JsonMappingException, JsonProcessingException {
		String s = json.getString("datatype"); 
		if(s.equals("orderdetails")) {
			OrderDetails order = objectMapper.readValue(consumerRecord.value(), OrderDetails.class);
			orderDetailsRepo.save(order);
			
		}
		else if(s.equals("deliverydetails")) {
			DeliveryDetails delivery = objectMapper.readValue(consumerRecord.value(), DeliveryDetails.class);
			deliveryDetailsRepo.save(delivery);
			
		}
		else if(s.equals("paymentdetails")) {
			
			PaymentDetails payment = objectMapper.readValue(consumerRecord.value(), PaymentDetails.class);
			paymentDetailsRepo.save(payment);
			
		}
		
	}
}
