package com.ecommercedb;




import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommercedb.consumer.KafkaConsumer;
import com.ecommercedb.customException.InvalidValueException;
import com.ecommercedb.dto.DeliveryDetails;
import com.ecommercedb.service.OrderService;


@ExtendWith(MockitoExtension.class)
public class KafkaConsumerTest {
	
	@InjectMocks
	KafkaConsumer consumer;
	
	@Mock
	OrderService orderService;
	
	@Test
	void consumerTest() throws InvalidValueException {
		
		DeliveryDetails deliveryDetails = new DeliveryDetails( "deliverydetails",1,"delivered","abc",8989);
		
		JSONObject json = new JSONObject(deliveryDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when(orderService.processOrder(any(ConsumerRecord.class))).thenReturn("Successfully saved");
		
		consumer.onMessage(cr);
		
		verify(orderService,times(1)).processOrder(cr);
			
	}
	
	@Test
	void consumerExceptionTest() throws InvalidValueException {
		
		DeliveryDetails deliveryDetails = new DeliveryDetails( "deliverydetails",0,"delivered","abc",8989);
		
		JSONObject json = new JSONObject(deliveryDetails);
		String serializedobj = json.toString(); 
		ConsumerRecord<Integer,String> cr = new ConsumerRecord<>("topic",1,3,1,serializedobj);
		
		when((orderService).processOrder(any(ConsumerRecord.class))).thenThrow(InvalidValueException.class);
		
		consumer.onMessage(cr);
		
		verify(orderService,times(1)).processOrder(cr);
		
			
	}

}
