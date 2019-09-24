package com.tis4.matriz_ensino.repository;

import java.util.Optional;

import com.tis4.matriz_ensino.model.Turma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    Optional<Turma> findByProfessorId(Long professorId);

    Optional<Turma> findBySupervisorId(Long supervisorId);

}