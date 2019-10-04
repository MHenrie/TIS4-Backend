package com.tis4.matriz_ensino.repository;

import com.tis4.matriz_ensino.model.Disciplina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

}