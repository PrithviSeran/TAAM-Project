/*
 * MainActivity.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import com.example.b07demosummer2024.R;

import java.util.Objects;

/**
 * Class used to make checks and load <code>HomeFragment</code>.
 * MainActivity is expected to be the first class called when app
 * is starting.
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called to display main activity.
     * Loads <code>HomeFragment</code> if <code>savedInstanceState</code> is null.
     *
     * @param savedInstanceState    The past state of MainActivity. savedInstanceState is
     * expected to be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    /**
     * This method transitions screen from the current fragment to <code>fragment</code>.
     * Using the <code>FragmentTransaction</code> class, it replaces the current displayed
     * fragment with parameter <code>fragment</code>.
     *
     * <p>
     * <code>fragment</code> should not be null. Generally, <code>fragment</code> is defined
     * in the method call.
     *
     * @param fragment      fragment is the new instantiated class of the corresponding
     *                      xml file. fragment is always instantiated in the method call.
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Throws <code>RuntimeException</code> if id of menuItem does not
     * match home id.
     * @param item      A previously created menuItem
     * @return          True if item id matches home id, calls super class
     * <code>onOptionsItemSelected</code> otherwise, which only <code>RuntimeException</code>.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}