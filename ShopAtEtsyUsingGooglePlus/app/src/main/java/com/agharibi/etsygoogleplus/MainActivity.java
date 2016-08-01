package com.agharibi.etsygoogleplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.agharibi.etsygoogleplus.api.Etsy;
import com.agharibi.etsygoogleplus.google.GoogleServicesHelper;
import com.agharibi.etsygoogleplus.model.ActiveListings;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private View progressBar;
    private TextView errorView;
    private ListingAdapter adapter;
    private GoogleServicesHelper mGoogleServicesHelper;
    private static final String STATE_ACTIVE_LISTINGS = "StateActiveListings";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressbar);
        errorView = (TextView) findViewById(R.id.error_view);

        //setup recyclerView
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL));

        adapter = new ListingAdapter(this);
        recyclerView.setAdapter(adapter);

        mGoogleServicesHelper = new GoogleServicesHelper(this, adapter);
        showLoading();

        if(savedInstanceState != null) {

            if(savedInstanceState.containsKey(STATE_ACTIVE_LISTINGS)) {
                adapter.success((ActiveListings) savedInstanceState.getParcelable(STATE_ACTIVE_LISTINGS), null);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleServicesHelper.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleServicesHelper.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGoogleServicesHelper.handleActvityResult(requestCode, resultCode, data);

        if(requestCode == ListingAdapter.REQUEST_CODE_PLUS_ONE) {
            adapter.notifyDataSetChanged(); // This will re-draw the state of plusOneButton on the screen.
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ActiveListings activeListings = adapter.getActiveListings();
        if (activeListings != null) {
            outState.putParcelable(STATE_ACTIVE_LISTINGS, activeListings);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    public void showError() {
        errorView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
}
