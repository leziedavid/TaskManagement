package com.mobisoft.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Action;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByTaskTaskId(Long taskId);

    List<Action> findByTask_TaskId(Long taskId);

}
