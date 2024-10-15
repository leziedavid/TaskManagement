package com.mobisoft.taskmanagement.pagination;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Paginator {

    public static <T> Page<T> paginate(List<T> items, int page, int size, String sortBy) {
        // Trier la liste selon le critère de tri
        List<T> sortedItems = items.stream()
            .sorted((a, b) -> {
                // Remplacez cette logique de tri par celle qui correspond à vos besoins
                return 0; // À remplacer par la logique de tri appropriée
            })
            .collect(Collectors.toList());

        // Calculer l'index de début et de fin pour la pagination
        int start = (page - 1) * size;
        int end = Math.min(start + size, sortedItems.size());

        // Créer la sous-liste paginée
        List<T> paginatedItems = sortedItems.subList(start, end);

        // Créer et retourner l'objet Page
        return new Page<>(paginatedItems, sortedItems.size(), page, size);
    }
}
