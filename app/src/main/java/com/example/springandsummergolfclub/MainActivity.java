package com.example.springandsummergolfclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.springandsummergolfclub.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtName;
    ListView listView;
    private TextView txtEmail;
    private DrawerLayout drawerLayout;
    private SQLiteHandler db;
    private SessionManager session;
    ActivityMainBinding activityMainBinding;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        listView = (ListView) findViewById(R.id.listview);

        int[] imageId = {R.drawable.coastal, R.drawable.download, R.drawable.hotsprings, R.drawable.highlands,
                R.drawable.serengeti};

        String[] names = {getString(R.string.coastal_gc), getString(R.string.serengeti_gc), getString(R.string.mau_gc),
                getString(R.string.mara_gc), getString(R.string.buffalo_springs)};

        String[] descriptions = {getString(R.string.golf_club_located_on_the_beautiful_coastline),
                getString(R.string.serengeti_desc), getString(R.string.mau_desc),
                getString(R.string.mara_desc), getString(R.string.hotsprings_desc)};

        String[] details = {getString(R.string.myCoastal), getString(R.string.serengeti_clubs),
                getString(R.string.higland_mau_clubs), getString(R.string.mara_plains),
                getString(R.string.buffaloSprings)};

        ArrayList<GolfClub> golfClubArrayList = new ArrayList<>();
        for (int i = 0; i < imageId.length; i++) {

            GolfClub golfClub = new GolfClub(names[i], imageId[i], descriptions[i], details[i]);
            golfClubArrayList.add(golfClub);

        }


        ListAdapter listAdapter = new ListAdapter(MainActivity.this, golfClubArrayList);

        activityMainBinding.listview.setAdapter(listAdapter);
        activityMainBinding.listview.setClickable(true);
        activityMainBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, GolfClubActivity.class);
                intent.putExtra("name", names[position]);
                intent.putExtra("imageid", imageId[position]);
                intent.putExtra("descriptions", descriptions[position]);
                intent.putExtra("inDetail", details[position]);
                startActivity(intent);

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation_bar, R.string.close_navigation_bar);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        txtName = (TextView) headerView.findViewById(R.id.name);
        txtEmail = (TextView) headerView.findViewById(R.id.email);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fragments()).addToBackStack(null).commit();
                break;
            case R.id.btnLogout:
                logoutUser();
                break;
            case R.id.home:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

//    private void showChangeLanguageDialog() {
//        final String[] listItems = {"English", "French"};
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//        mBuilder.setTitle("Choose Language");
//        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    setLocale("fr");
//                } else {
//                    setLocate("en");
//                }
//            }
//        });
//
//        AlertDialog mDialog = mBuilder.create();
//        mDialog.show();
//    }
//
//    private void setLocale(String lang){
//    Locale locale = new Locale(lang);
//    Locale.setDefault(locale);
//
//    Configuration config = new configuration();
//    config.locale = locale;
//
//    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().
//            getDisplayMetrics());
//
//    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE)
//                .edit();
//    editor.putString("My_Lang", lang);
//    editor.apply();
//
//
//    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}