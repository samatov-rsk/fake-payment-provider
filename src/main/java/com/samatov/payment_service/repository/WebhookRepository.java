package com.samatov.payment_service.repository;

import com.samatov.payment_service.model.Webhook;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookRepository extends ReactiveCrudRepository<Webhook, Long> { }