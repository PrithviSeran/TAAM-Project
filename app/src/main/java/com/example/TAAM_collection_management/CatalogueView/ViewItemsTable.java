/*
 * ViewItemsTable.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.CatalogueView;

/**
 * ViewItemsTable is the interface used by all classes that are
 * which allows them to display items in a table format.
 */
public interface ViewItemsTable {

    /**
     * Called to display items from database in a table view.
     * Upon successful task completion, the desired table view is implement.
     * Upon a failed task, a popup is displayed and exception is logged.
     */
    void displayItems();

}
