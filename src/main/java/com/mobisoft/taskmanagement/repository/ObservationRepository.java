package com.mobisoft.taskmanagement.repository;

import com.mobisoft.taskmanagement.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
}
