package com.ecommercedb;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;


import com.ecommercedb.customException.InvalidValueException;
import com.ecommercedb.dto.DeliveryDetails;
import com.ecommercedb.dto.OrderDetails;
import com.ecommercedb.dto.PaymentDetails;
import com.ecommercedb.dto.Product;
import com.ecommercedb.repo.DeliveryDetailsRepo;
import com.ecommercedb.repo.OrderDetailsRepo;
import com.ecommercedb.repo.PaymentDetailsRepo;
import com.ecommercedb.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
	

	
	@Mock
	DeliveryDetailsRepo deliveryDetailsRepo;
	
	@Mock
	PaymentDetailsRepo paymentDetailsRepo;
	
	@Mock
	OrderDetailsRepo orderDetailsRepo;
	
	@Mock
	ObjectMapper objectMapper;
	
	@Mock
	KafkaTemplate<Integer,String> kafkaTemplate;
	
	@InjectMocks
	OrderService orderService;
	

	
	
	@Test
	void OrderServicePaymentTest() throws JsonProcessingException, InvalidValueException {
		
		PaymentDetails paymentDetails = new PaymentDetails("paymentdetails",1,"credit_card",1898,"done");
		JSONObject json = new JSONObject(paymentDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when(objectMapper.readValue(cr.value(),PaymentDetails.class)).thenReturn(paymentDetails);
		when(paymentDetailsRepo.save(any(PaymentDetails.class))).thenReturn(paymentDetails);
		
		String result = orderService.processOrder(cr);
		
		assertEquals(result,"Successfully saved");
		
		
		
		
		
	}
	@Test
	void OrderServiceDeliveryTest() throws JsonProcessingException, InvalidValueException {
		DeliveryDetails deliveryDetails = new DeliveryDetails( "deliverydetails",1,"delivered","abc",8989);
		
		JSONObject json = new JSONObject(deliveryDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when(objectMapper.readValue(cr.value(),DeliveryDetails.class)).thenReturn(deliveryDetails);
		when(deliveryDetailsRepo.save(any(DeliveryDetails.class))).thenReturn(deliveryDetails);
		
		String result = orderService.processOrder(cr);
		
		assertEquals(result,"Successfully saved");
			
		
	}
	
	@Test
	void OrderServiceOrderDetailsTest() throws JsonProcessingException, InvalidValueException {
		List<Product> list = new ArrayList<>();
		list.add(new Product(1,"abc",23,899));
		list.add(new Product(2,"def",4,999));
		OrderDetails orderDetails = new OrderDetails("orderdetails",1,list,1189);
		
		JSONObject json = new JSONObject(orderDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when(objectMapper.readValue(cr.value(),OrderDetails.class)).thenReturn(orderDetails);
		when(orderDetailsRepo.save(any(OrderDetails.class))).thenReturn(orderDetails);
		
		String result = orderService.processOrder(cr);
		
		assertEquals(result,"Successfully saved");
			
		
	}
	
	@Test
	void OrderServiceExceptionTest() throws  InvalidValueException, JsonMappingException, JsonProcessingException {
		
		PaymentDetails paymentDetails = new PaymentDetails("paymentdetails",null,"credit_card",1898,"done");
		JSONObject json = new JSONObject(paymentDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		assertThrows(InvalidValueException.class,()->orderService.processOrder(cr));
				
		
	}
	
	@Test
	void OrderServiceJsonProcessingExceptionTest() throws  InvalidValueException, JsonMappingException, JsonProcessingException {
		
		DeliveryDetails deliveryDetails = new DeliveryDetails( "deliverydetails",1,"delivered","abc",8989);
		JSONObject json = new JSONObject(deliveryDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when(objectMapper.readValue(cr.value(),DeliveryDetails.class)).thenThrow(JsonProcessingException.class);
		
		assertThrows(InvalidValueException.class,()->orderService.processOrder(cr));
				
		
	}
	
	@Test
	void OrderServiceJsonMappingExceptionTest() throws  InvalidValueException, JsonMappingException, JsonProcessingException {
		
		DeliveryDetails deliveryDetails = new DeliveryDetails( "deliverydetails",1,"delivered","abc",8989);
		JSONObject json = new JSONObject(deliveryDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when(objectMapper.readValue(cr.value(),DeliveryDetails.class)).thenThrow(JsonMappingException.class);
		
		assertThrows(InvalidValueException.class,()->orderService.processOrder(cr));
				
		
	}

}
