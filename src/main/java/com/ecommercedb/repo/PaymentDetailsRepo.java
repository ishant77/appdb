package com.ecommercedb.repo;

import org.springframework.data.mongodb.repository.MongoRepository;


import org.springframework.stereotype.Repository;

import com.ecommercedb.dto.PaymentDetails;

@Repository
public interface PaymentDetailsRepo extends MongoRepository<PaymentDetails,Integer>{

}
