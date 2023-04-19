package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class SuggestDetailActivity extends AppCompatActivity {

    Suggest suggest;
    StartDestLoc startDestLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startDestLoc = (StartDestLoc) getIntent().getSerializableExtra("startDestLoc");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_detail);

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get previous obj
        if(getIntent().getExtras() != null) {
            suggest = (Suggest) getIntent().getSerializableExtra("suggest");
            Log.d("suggest", suggest.getRouteJson());
        }

        setTitle(suggest.getRoute()+" detail");


    }

    // Back button at title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(SuggestDetailActivity.this, TransitSuggestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("startDestLoc", startDestLoc);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}