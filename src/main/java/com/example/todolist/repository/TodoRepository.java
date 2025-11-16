package com.example.todolist.repository;


import com.example.todolist.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des opérations de base de données sur l'entité Todo.
 * Étend JpaRepository pour bénéficier des méthodes CRUD standards.
 *
 * @author YOUESSAH Audrey
 * @version 1.0
 * @since 2025-11-16
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * Recherche toutes les tâches selon leur statut de complétion.
     *
     * @param completee le statut de complétion recherché (true = complétée, false = non complétée)
     * @return la liste des tâches correspondant au statut
     */
    List<Todo> findByCompletee(Boolean completee);

    /**
     * Recherche les tâches dont le titre contient la chaîne de caractères spécifiée.
     * La recherche est insensible à la casse.
     *
     * @param titre la chaîne de caractères à rechercher dans le titre
     * @return la liste des tâches dont le titre contient la chaîne recherchée
     */
    List<Todo> findByTitreContainingIgnoreCase(String titre);

    /**
     * Compte le nombre de tâches selon leur statut de complétion.
     *
     * @param completee le statut de complétion à compter
     * @return le nombre de tâches correspondant au statut
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completee = :completee")
    Long countByCompletee(Boolean completee);
}