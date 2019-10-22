package com.tis4.matriz_ensino.repositories;

import java.util.List;

import com.tis4.matriz_ensino.models.ItemTurma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTurmaRepository extends JpaRepository<ItemTurma, Long> {

    List<ItemTurma> findAllByDisciplinaIdAndTurmaId(Long disciplinaId, Long turmaId);

    List<ItemTurma> findAllByDisciplinaIdAndTurmaIdAndGlobalFalse(Long disciplinaId, Long turmaId);
}