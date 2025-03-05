package com.stanok.stanokdelivery.repository;

import com.stanok.stanokdelivery.model.Stanok;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StanokRepository extends JpaRepository<Stanok, UUID> {
}
