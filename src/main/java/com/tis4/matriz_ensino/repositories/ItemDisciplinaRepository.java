package com.tis4.matriz_ensino.repositories;

import java.util.List;

import com.tis4.matriz_ensino.models.ItemDisciplina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDisciplinaRepository extends JpaRepository<ItemDisciplina, Long> {

    List<ItemDisciplina> findAllByDisciplinaIdAndGlobalTrue(Long disciplinaId);

}