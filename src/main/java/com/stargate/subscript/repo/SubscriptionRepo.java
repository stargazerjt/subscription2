package com.stargate.subscript.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stargate.subscript.models.Subscription;


public interface SubscriptionRepo extends JpaRepository<Subscription, Long>  {

}
