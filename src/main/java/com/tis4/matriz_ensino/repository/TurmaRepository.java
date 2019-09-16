package com.tis4.matriz_ensino.repository;

import com.tis4.matriz_ensino.models.Turma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    Turma findById(long id);

    Turma findByProfessorId(long professorId);

    Turma findBySupervisorId(long supervisorId);

}