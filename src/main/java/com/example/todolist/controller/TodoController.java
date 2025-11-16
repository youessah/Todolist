package com.example.todolist.controller;


import com.example.todolist.entity.Todo;
import com.example.todolist.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des tâches (TodoList).
 * Expose les endpoints de l'API pour les opérations CRUD.
 *
 * @author YOUESSAH Audrey
 * @version 1.0
 * @since 2025-11-16
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Todos", description = "API de gestion des tâches")
public class TodoController {

    private final TodoService todoService;

    /**
     * Récupère toutes les tâches.
     *
     * @return ResponseEntity contenant la liste de toutes les tâches (HTTP 200)
     */
    @GetMapping
    @Operation(
            summary = "Récupérer toutes les tâches",
            description = "Retourne la liste complète de toutes les tâches présentes en base de données"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des tâches récupérée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            )
    })
    public ResponseEntity<List<Todo>> obtenirToutesLesTaches() {
        log.info("GET /api/todos - Récupération de toutes les tâches");
        List<Todo> taches = todoService.obtenirToutesLesTaches();
        return ResponseEntity.ok(taches);
    }

    /**
     * Récupère une tâche spécifique par son identifiant.
     *
     * @param id l'identifiant de la tâche à récupérer
     * @return ResponseEntity contenant la tâche (HTTP 200) ou HTTP 404 si non trouvée
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer une tâche par ID",
            description = "Retourne une tâche spécifique identifiée par son ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tâche trouvée et retournée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tâche non trouvée avec cet ID"
            )
    })
    public ResponseEntity<Todo> obtenirTacheParId(
            @Parameter(description = "ID de la tâche", required = true)
            @PathVariable Long id) {
        log.info("GET /api/todos/{} - Récupération de la tâche", id);

        return todoService.obtenirTacheParId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Tâche non trouvée avec l'id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Crée une nouvelle tâche.
     *
     * @param todo l'objet Todo à créer (validé)
     * @return ResponseEntity contenant la tâche créée (HTTP 201)
     */
    @PostMapping
    @Operation(
            summary = "Créer une nouvelle tâche",
            description = "Crée une nouvelle tâche dans la base de données avec les informations fournies"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tâche créée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de la tâche invalides"
            )
    })
    public ResponseEntity<Todo> creerTache(
            @Parameter(description = "Données de la nouvelle tâche", required = true)
            @Valid @RequestBody Todo todo) {
        log.info("POST /api/todos - Création d'une nouvelle tâche: {}", todo.getTitre());

        Todo tacheCree = todoService.creerTache(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(tacheCree);
    }

    /**
     * Met à jour une tâche existante.
     *
     * @param id l'identifiant de la tâche à modifier
     * @param todo les nouvelles données de la tâche (validées)
     * @return ResponseEntity contenant la tâche modifiée (HTTP 200) ou HTTP 404 si non trouvée
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une tâche existante",
            description = "Met à jour les informations d'une tâche identifiée par son ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tâche modifiée avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tâche non trouvée avec cet ID"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de la tâche invalides"
            )
    })
    public ResponseEntity<Todo> modifierTache(
            @Parameter(description = "ID de la tâche à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de la tâche", required = true)
            @Valid @RequestBody Todo todo) {
        log.info("PUT /api/todos/{} - Modification de la tâche", id);

        return todoService.modifierTache(id, todo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Impossible de modifier, tâche non trouvée: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Supprime une tâche.
     *
     * @param id l'identifiant de la tâche à supprimer
     * @return ResponseEntity vide (HTTP 204) si suppression réussie, HTTP 404 si non trouvée
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une tâche",
            description = "Supprime définitivement une tâche identifiée par son ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Tâche supprimée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tâche non trouvée avec cet ID"
            )
    })
    public ResponseEntity<Void> supprimerTache(
            @Parameter(description = "ID de la tâche à supprimer", required = true)
            @PathVariable Long id) {
        log.info("DELETE /api/todos/{} - Suppression de la tâche", id);

        if (todoService.supprimerTache(id)) {
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Impossible de supprimer, tâche non trouvée: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère les tâches filtrées par statut de complétion.
     *
     * @param completee le statut de complétion (true/false)
     * @return ResponseEntity contenant la liste des tâches filtrées (HTTP 200)
     */
    @GetMapping("/statut/{completee}")
    @Operation(
            summary = "Filtrer les tâches par statut",
            description = "Retourne toutes les tâches complétées ou non complétées selon le paramètre"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des tâches filtrées récupérée",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            )
    })
    public ResponseEntity<List<Todo>> obtenirTachesParStatut(
            @Parameter(description = "Statut de complétion (true=complétée, false=non complétée)", required = true)
            @PathVariable Boolean completee) {
        log.info("GET /api/todos/statut/{} - Récupération des tâches par statut", completee);

        List<Todo> taches = todoService.obtenirTachesParStatut(completee);
        return ResponseEntity.ok(taches);
    }

    /**
     * Recherche des tâches par titre.
     *
     * @param titre la chaîne de caractères à rechercher dans les titres
     * @return ResponseEntity contenant la liste des tâches correspondantes (HTTP 200)
     */
    @GetMapping("/recherche")
    @Operation(
            summary = "Rechercher des tâches par titre",
            description = "Retourne toutes les tâches dont le titre contient la chaîne recherchée (insensible à la casse)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des tâches correspondantes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            )
    })
    public ResponseEntity<List<Todo>> rechercherTaches(
            @Parameter(description = "Chaîne de caractères à rechercher dans le titre", required = true)
            @RequestParam String titre) {
        log.info("GET /api/todos/recherche?titre={} - Recherche de tâches", titre);

        List<Todo> taches = todoService.rechercherTachesParTitre(titre);
        return ResponseEntity.ok(taches);
    }

    /**
     * Change le statut de complétion d'une tâche.
     *
     * @param id l'identifiant de la tâche
     * @param completee le nouveau statut de complétion
     * @return ResponseEntity contenant la tâche modifiée (HTTP 200) ou HTTP 404 si non trouvée
     */
    @PatchMapping("/{id}/statut")
    @Operation(
            summary = "Changer le statut d'une tâche",
            description = "Modifie uniquement le statut de complétion d'une tâche"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut modifié avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Todo.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tâche non trouvée avec cet ID"
            )
    })
    public ResponseEntity<Todo> changerStatutTache(
            @Parameter(description = "ID de la tâche", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouveau statut de complétion", required = true)
            @RequestParam Boolean completee) {
        log.info("PATCH /api/todos/{}/statut?completee={} - Changement de statut", id, completee);

        return todoService.changerStatutTache(id, completee)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Impossible de changer le statut, tâche non trouvée: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Compte le nombre de tâches par statut.
     *
     * @param completee le statut à compter
     * @return ResponseEntity contenant le nombre de tâches (HTTP 200)
     */
    @GetMapping("/statistiques/count")
    @Operation(
            summary = "Compter les tâches par statut",
            description = "Retourne le nombre de tâches complétées ou non complétées"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre de tâches retourné",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<Long> compterTachesParStatut(
            @Parameter(description = "Statut à compter", required = true)
            @RequestParam Boolean completee) {
        log.info("GET /api/todos/statistiques/count?completee={} - Comptage des tâches", completee);

        Long count = todoService.compterTachesParStatut(completee);
        return ResponseEntity.ok(count);
    }
}
