package com.tis4.matriz_ensino.repository;

import java.util.List;

import com.tis4.matriz_ensino.model.ItemDisciplina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDisciplinaRepository extends JpaRepository<ItemDisciplina, Long> {

    List<ItemDisciplina> findAllByDisciplinaId(Long disciplinaId);
}