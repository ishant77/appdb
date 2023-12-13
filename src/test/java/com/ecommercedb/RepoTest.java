package com.ecommercedb;




import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.ecommercedb.dto.DeliveryDetails;
import com.ecommercedb.dto.OrderDetails;
import com.ecommercedb.dto.PaymentDetails;
import com.ecommercedb.dto.Product;
import com.ecommercedb.repo.DeliveryDetailsRepo;
import com.ecommercedb.repo.OrderDetailsRepo;
import com.ecommercedb.repo.PaymentDetailsRepo;

@DataMongoTest
public class RepoTest {
	
	@Autowired
	DeliveryDetailsRepo deliveryDetailsRepo;
	
	@Autowired
	PaymentDetailsRepo paymentDetailsRepo;
	
	@Autowired
	OrderDetailsRepo orderDetailsRepo;
	
	
	@Test
	void DeliveryRepoTest() {
		DeliveryDetails deliveryDetails = new DeliveryDetails( "deliverydetails",1,"delivered","abc",8989);
		DeliveryDetails result = deliveryDetailsRepo.save(deliveryDetails);
		assertEquals(result,deliveryDetails);
	}
	
	@Test
	void PaymentrepoTest() {
		PaymentDetails paymentDetails = new PaymentDetails("paymentdetails",1,"credit_card",1898,"done");
		PaymentDetails result = paymentDetailsRepo.save(paymentDetails);
		assertEquals(result,paymentDetails);
		
	}
	
	@Test
	void OrdersRepoTest() {
		List<Product> list = new ArrayList<>();
		list.add(new Product(1,"abc",23,899));
		list.add(new Product(2,"def",4,999));
		OrderDetails orderDetails = new OrderDetails("orderdetails",1,list,1189);
		OrderDetails result = orderDetailsRepo.save(orderDetails);
		assertEquals(result,orderDetails);
	}
	
	

}
