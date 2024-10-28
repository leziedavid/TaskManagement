// package com.mobisoft.taskmanagement.pagination;

// import java.util.List;
// import java.util.stream.Collectors;

// public class Paginator {

//     public static <T> Page<T> paginate(List<T> items, int page, int size, String sortBy) {
//         // Trier la liste selon le critère de tri
//         List<T> sortedItems = items.stream()
//             .sorted((a, b) -> {
//                 // Remplacez cette logique de tri par celle qui correspond à vos besoins
//                 return 0; // À remplacer par la logique de tri appropriée
//             })
//             .collect(Collectors.toList());

//         // Calculer l'index de début et de fin pour la pagination
//         int start = (page - 1) * size;
//         int end = Math.min(start + size, sortedItems.size());

//         // Créer la sous-liste paginée
//         List<T> paginatedItems = sortedItems.subList(start, end);

//         // Créer et retourner l'objet Page
//         return new Page<>(paginatedItems, sortedItems.size(), page, size);
//     }
// }



package com.mobisoft.taskmanagement.pagination;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Paginator {

    public static <T> Page<T> paginate(List<T> items, int page, int size, String sortBy) {
        // Trier la liste selon le critère de tri en ordre décroissant
        List<T> sortedItems = items.stream()
            .sorted(Comparator.comparing((T item) -> getFieldValue(item, sortBy)).reversed())
            .collect(Collectors.toList());

        // Calculer l'index de début et de fin pour la pagination
        int start = (page - 1) * size;
        int end = Math.min(start + size, sortedItems.size());

        // Créer la sous-liste paginée
        List<T> paginatedItems = sortedItems.subList(start, end);

        // Créer et retourner l'objet Page
        return new Page<>(paginatedItems, sortedItems.size(), page, size);
    }

    // Méthode pour obtenir la valeur d'un champ en fonction du nom
    @SuppressWarnings("unchecked")
    private static <T> Comparable<Object> getFieldValue(T item, String fieldName) {
        try {
            return (Comparable<Object>) item.getClass().getMethod("get" + capitalize(fieldName)).invoke(item);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException("Erreur lors de l'accès au champ: " + fieldName, e);
        }
    }

    // Méthode utilitaire pour capitaliser la première lettre
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

