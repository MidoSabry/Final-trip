package com.example.tripreminderapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        getSupportActionBar().hide();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        ActionBar actionBar = getActionBar();
//        actionBar.hide();


//        Intent intent = new Intent(HomeActivity.this,AddTripActivity.class);
//        startActivityForResult(intent,100);

    }



 //   @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.navigation_home) {
//            //Toast.makeText(this, "add More", Toast.LENGTH_SHORT).show();
//            startActivityForResult(new Intent(this, AddTripActivity.class),100);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //Mido
    //To make refresh
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        requestCode &= 0xffff;
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            if (fragment != null) {
//                fragment.onActivityResult(requestCode, resultCode, data);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    
}