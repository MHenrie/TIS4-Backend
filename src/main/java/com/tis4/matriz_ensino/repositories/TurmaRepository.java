package com.tis4.matriz_ensino.repositories;

import java.util.List;
import java.util.Optional;

import com.tis4.matriz_ensino.models.Turma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    Optional<Turma> findByNomeAndAno(String nome, Short ano);

    Optional<Turma> findByProfessorIdAndAno(Long professorId, Short ano);

    List<Turma> findAllBySupervisorIdAndAno(Long supervisorId, Short ano);

    List<Turma> findAllBySerie(String serie);

}