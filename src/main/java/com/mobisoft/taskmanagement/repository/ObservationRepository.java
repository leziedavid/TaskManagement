package com.mobisoft.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Observation;

import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    @Modifying
    @Query(value = "INSERT INTO observation_files (observation_id, files_data_id) VALUES (?1, ?2)", nativeQuery = true)
    void insertObservationFileRelation(Long observationId, String id);

    List<Observation> findByTaskTaskId(Long taskId);
    
}
