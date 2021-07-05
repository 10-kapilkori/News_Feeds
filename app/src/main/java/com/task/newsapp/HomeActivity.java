package com.task.newsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.task.newsapp.ui.entertainment.EntertainmentNewsFragment;
import com.task.newsapp.ui.home.HomeNewsFragment;
import com.task.newsapp.ui.politics.PoliticsNewsFragment;
import com.task.newsapp.ui.saved.SavedNewsFragment;
import com.task.newsapp.ui.sports.SportsNewsFragment;
import com.task.newsapp.ui.technology.TechnologyNewsFragment;
import com.task.newsapp.ui.topheadlines.TopHeadlinesNewsFragment;
import com.task.newsapp.ui.world.WorldNewsFragment;

public class HomeActivity extends AppCompatActivity {
    FragmentManager manager;
    Toolbar toolbar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeNewsFragment()).commit();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {
                case R.id.nav_home:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim)
                            .replace(R.id.fragment_container, new HomeNewsFragment())
                            .commit();
                    toolbar.setTitle("Home");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_top_headlines:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new TopHeadlinesNewsFragment())
                            .commit();
                    toolbar.setTitle("Top Headlines");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_world:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new WorldNewsFragment())
                            .commit();
                    toolbar.setTitle("World");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_politics:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new PoliticsNewsFragment())
                            .commit();
                    toolbar.setTitle("Politics");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_entertainment:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new EntertainmentNewsFragment())
                            .commit();
                    toolbar.setTitle("Entertainment");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_sports:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new SportsNewsFragment())
                            .commit();
                    toolbar.setTitle("Sports");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_technology:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new TechnologyNewsFragment())
                            .commit();
                    toolbar.setTitle("Technology");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_saved:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new SavedNewsFragment())
                            .commit();
                    toolbar.setTitle("Saved News");
                    drawerLayout.closeDrawer(GravityCompat.START);

                default:
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setView(getLayoutInflater().inflate(R.layout.custom_about, null))
                        .setPositiveButton("Done", (dialog, which) -> dialog.dismiss())
                        .show();
                return true;
            default:
                return false;
        }
    }
}
