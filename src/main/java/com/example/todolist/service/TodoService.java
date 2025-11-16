package com.example.todolist.service;


import com.example.todolist.entity.Todo;
import com.example.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service contenant la logique métier pour la gestion des tâches.
 * Cette classe orchestre les opérations entre le contrôleur et le repository.
 *
 * @author Votre Nom
 * @version 1.0
 * @since 2025-11-16
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * Récupère toutes les tâches de la base de données.
     *
     * @return la liste de toutes les tâches
     */
    @Transactional(readOnly = true)
    public List<Todo> obtenirToutesLesTaches() {
        log.info("Récupération de toutes les tâches");
        return todoRepository.findAll();
    }

    /**
     * Récupère une tâche spécifique par son identifiant.
     *
     * @param id l'identifiant de la tâche recherchée
     * @return un Optional contenant la tâche si trouvée, vide sinon
     */
    @Transactional(readOnly = true)
    public Optional<Todo> obtenirTacheParId(Long id) {
        log.info("Récupération de la tâche avec l'id: {}", id);
        return todoRepository.findById(id);
    }

    /**
     * Crée une nouvelle tâche dans la base de données.
     *
     * @param todo l'objet Todo à créer
     * @return la tâche créée avec son identifiant généré
     * @throws IllegalArgumentException si le todo est null
     */
    public Todo creerTache(Todo todo) {
        if (todo == null) {
            log.error("Tentative de création d'une tâche null");
            throw new IllegalArgumentException("La tâche ne peut pas être null");
        }

        log.info("Création d'une nouvelle tâche: {}", todo.getTitre());

        // S'assurer que l'id est null pour une création
        todo.setId(null);

        return todoRepository.save(todo);
    }

    /**
     * Met à jour une tâche existante.
     *
     * @param id l'identifiant de la tâche à modifier
     * @param todoModifie les nouvelles données de la tâche
     * @return un Optional contenant la tâche modifiée si trouvée, vide sinon
     * @throws IllegalArgumentException si le todoModifie est null
     */
    public Optional<Todo> modifierTache(Long id, Todo todoModifie) {
        if (todoModifie == null) {
            log.error("Tentative de modification avec une tâche null");
            throw new IllegalArgumentException("La tâche ne peut pas être null");
        }

        log.info("Modification de la tâche avec l'id: {}", id);

        return todoRepository.findById(id)
                .map(todoExistant -> {
                    // Mise à jour des champs modifiables
                    todoExistant.setTitre(todoModifie.getTitre());
                    todoExistant.setDescription(todoModifie.getDescription());
                    todoExistant.setCompletee(todoModifie.getCompletee());

                    Todo tacheSauvegardee = todoRepository.save(todoExistant);
                    log.info("Tâche modifiée avec succès: {}", id);
                    return tacheSauvegardee;
                });
    }

    /**
     * Supprime une tâche de la base de données.
     *
     * @param id l'identifiant de la tâche à supprimer
     * @return true si la tâche a été supprimée, false si elle n'existe pas
     */
    public boolean supprimerTache(Long id) {
        log.info("Suppression de la tâche avec l'id: {}", id);

        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            log.info("Tâche supprimée avec succès: {}", id);
            return true;
        }

        log.warn("Tentative de suppression d'une tâche inexistante: {}", id);
        return false;
    }

    /**
     * Récupère toutes les tâches selon leur statut de complétion.
     *
     * @param completee le statut de complétion recherché
     * @return la liste des tâches correspondant au statut
     */
    @Transactional(readOnly = true)
    public List<Todo> obtenirTachesParStatut(Boolean completee) {
        log.info("Récupération des tâches avec le statut completee: {}", completee);
        return todoRepository.findByCompletee(completee);
    }

    /**
     * Recherche des tâches dont le titre contient une chaîne de caractères.
     *
     * @param titre la chaîne de caractères à rechercher
     * @return la liste des tâches correspondantes
     */
    @Transactional(readOnly = true)
    public List<Todo> rechercherTachesParTitre(String titre) {
        log.info("Recherche de tâches contenant: {}", titre);
        return todoRepository.findByTitreContainingIgnoreCase(titre);
    }

    /**
     * Marque une tâche comme complétée ou non complétée.
     *
     * @param id l'identifiant de la tâche
     * @param completee le nouveau statut de complétion
     * @return un Optional contenant la tâche modifiée si trouvée, vide sinon
     */
    public Optional<Todo> changerStatutTache(Long id, Boolean completee) {
        log.info("Changement du statut de la tâche {} à: {}", id, completee);

        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setCompletee(completee);
                    return todoRepository.save(todo);
                });
    }

    /**
     * Compte le nombre total de tâches selon leur statut.
     *
     * @param completee le statut à compter
     * @return le nombre de tâches
     */
    @Transactional(readOnly = true)
    public Long compterTachesParStatut(Boolean completee) {
        log.info("Comptage des tâches avec le statut: {}", completee);
        return todoRepository.countByCompletee(completee);
    }
}
