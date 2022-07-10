package com.cs.challenge.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cs.challenge.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String>{

}