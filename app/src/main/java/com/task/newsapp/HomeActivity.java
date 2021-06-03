package com.task.newsapp;

import android.annotation.SuppressLint;
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
                    Toast.makeText(this, "Top Headlines News Fragment", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.nav_world:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new WorldNewsFragment())
                            .commit();
                    toolbar.setTitle("World");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "World News Fragment", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.nav_politics:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new PoliticsNewsFragment())
                            .commit();
                    toolbar.setTitle("Politics");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "Politics News Fragment", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.nav_entertainment:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new EntertainmentNewsFragment())
                            .commit();
                    toolbar.setTitle("Entertainment");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "Entertainment News Fragment", Toast.LENGTH_SHORT).show();

                    break;

                case R.id.nav_sports:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new SportsNewsFragment())
                            .commit();
                    toolbar.setTitle("Sports");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "Sports News Fragment", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.nav_technology:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.fragment_container, new TechnologyNewsFragment())
                            .commit();
                    toolbar.setTitle("Technology");
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "Technology News Fragment", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.action_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
