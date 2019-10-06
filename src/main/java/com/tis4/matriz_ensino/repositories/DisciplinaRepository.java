package com.tis4.matriz_ensino.repositories;

import java.util.List;

import com.tis4.matriz_ensino.models.Disciplina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findAllBySerie(String serie);
}