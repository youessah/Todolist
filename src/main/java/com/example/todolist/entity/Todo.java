package com.example.todolist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité représentant une tâche dans la liste des tâches à faire.
 * Cette classe est mappée avec la table "todos" en base de données.
 *
 * @author YOUESSAH Audrey
 * @version 1.0
 * @since 2025-11-16
 */
@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    /**
     * Identifiant unique de la tâche.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Titre de la tâche.
     * Ne peut pas être vide et doit contenir entre 1 et 100 caractères.
     */
    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(min = 1, max = 100, message = "Le titre doit contenir entre 1 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String titre;

    /**
     * Description détaillée de la tâche.
     * Optionnelle, peut contenir jusqu'à 500 caractères.
     */
    @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
    @Column(length = 500)
    private String description;

    /**
     * Statut d'achèvement de la tâche.
     * false = non terminée, true = terminée
     * Par défaut initialisée à false.
     */
    @Column(nullable = false)
    private Boolean completee = false;

    /**
     * Date et heure de création de la tâche.
     * Automatiquement renseignée lors de la création.
     */
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    /**
     * Date et heure de la dernière modification.
     * Automatiquement mise à jour lors de chaque modification.
     */
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * Méthode appelée automatiquement avant la persistance initiale de l'entité.
     * Initialise la date de création.
     */
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Méthode appelée automatiquement avant chaque mise à jour de l'entité.
     * Met à jour la date de modification.
     */
    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }
}