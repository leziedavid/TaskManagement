package com.mobisoft.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
