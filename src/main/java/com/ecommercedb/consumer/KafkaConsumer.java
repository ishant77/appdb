package com.ecommercedb.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommercedb.customException.InvalidValueException;
import com.ecommercedb.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaConsumer {
	
	@Autowired
	private OrderService orderService;
	
	@KafkaListener(topics = {"ordersTopic"})
	public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws  InvalidValueException{
		try {
			log.info("ConsumerRecord : {}",consumerRecord);
			orderService.processOrder(consumerRecord);
		}
		catch(InvalidValueException e){
			log.error(e.getMessage());
		}
		

		
	}
	
	

}
