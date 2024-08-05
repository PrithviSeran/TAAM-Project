package com.example.b07demosummer2024.firebase;

import com.example.b07demosummer2024.Item;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class ItemFetcher {
    private static final DatabaseReference databaseRoot = FirebaseReferences.DATABASE_ROOT;

    /**
     * Function that queries the Firebase for all items that match the given queries.
     * An item matches a set of queries iff for all i, the value of Item.toArray() at index i,
     * matches with the ith query. If they do match, then compareFunction should return True,
     * otherwise False. Note that queries that exceed the number of properties
     * of Item excluding Description will be ignored.
     *
     * @param callback the function that handles the results of the search
     * @param compareFunction the function that checks if the item's value matches with the query.
     *                        The inputs are compareFunction(ith query, Item.toArray()[i])
     * @param queries the values of Item (lot num, name, ..., period) that we want to search for
     * @see Item
     * @see Item#toArray()
     */
    public static void searchItems(FirebaseCallback<List<Item>> callback,
                                   BiPredicate<String, String> compareFunction,
                                   String... queries) {
        databaseRoot.child("Items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Item> searchResults = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    String[] itemAttr = item.toArray();
                    boolean isEqual = true;

                    for (int i = 0; i < itemAttr.length - 1; i++) {
                        if (!compareFunction.test(queries[i], itemAttr[i])) {
                            isEqual = false;
                            break;
                        }
                    }
                    if (isEqual) {searchResults.add(item);}
                }
                callback.onFirebaseSuccess(searchResults);
            } else {
                callback.onFirebaseFailure(Objects.toString(task.getException(),
                        "No message available"));
            }
        });
    }

    public static void getAllItems(FirebaseCallback<List<Item>> callback) {
        searchItems(callback, (a, b) -> true, null, null, null, null, null);
    }
}
