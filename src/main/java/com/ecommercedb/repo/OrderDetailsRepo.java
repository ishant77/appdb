package com.ecommercedb.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommercedb.dto.OrderDetails;

@Repository
public interface OrderDetailsRepo extends MongoRepository<OrderDetails,Integer>{

}
