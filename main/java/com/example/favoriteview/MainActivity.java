package com.example.favoriteview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavoriteView favoriteView0 = findViewById(R.id.fv0);
        FavoriteView favoriteView1 = findViewById(R.id.fv1);
        favoriteView0.setFavoriteListener(isFavorite -> {
            if (isFavorite)
                Toast.makeText(this, "It's your favorite.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "It's not your favorite.", Toast.LENGTH_SHORT).show();
        });


        favoriteView1.setFavorite(true);
        favoriteView1.setFavoriteListener(isFavorite -> {
            if (isFavorite)
                Toast.makeText(this, "It's your favorite.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "It's not your favorite.", Toast.LENGTH_SHORT).show();
        });
    }


}