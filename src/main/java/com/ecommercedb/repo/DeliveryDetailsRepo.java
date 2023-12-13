package com.ecommercedb.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecommercedb.dto.DeliveryDetails;

@Repository
public interface DeliveryDetailsRepo extends MongoRepository<DeliveryDetails,Integer>{

}
