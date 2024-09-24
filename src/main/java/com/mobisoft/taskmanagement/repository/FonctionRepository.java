package com.mobisoft.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobisoft.taskmanagement.entity.Fonction;

public interface FonctionRepository extends JpaRepository<Fonction, Long> {
}
