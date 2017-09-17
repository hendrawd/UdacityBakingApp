package hendraganteng.udacitybakingapp.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.fragment.DetailFragment;
import hendraganteng.udacitybakingapp.fragment.StepFragment;
import hendraganteng.udacitybakingapp.network.model.Ingredient;
import hendraganteng.udacitybakingapp.network.model.Step;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setupBackButton();
        setupContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int actionId = item.getItemId();
        switch (actionId) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mainFragment = fragmentManager.findFragmentById(R.id.fl_main);
        if (mainFragment instanceof StepFragment) {
            showDetailFragment();
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    private void setupBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showDetailFragment() {
        DetailFragment detailFragment = DetailFragment.newInstance(
                getIngredientList(),
                getStepList()
        );
        replaceFragment(R.id.fl_main, detailFragment);
    }

    private void setupContent() {
        showDetailFragment();

        if (findViewById(R.id.fl_second) != null) {
            StepFragment stepFragment = StepFragment.newInstance(0);
            replaceFragment(R.id.fl_second, stepFragment);
        }
    }

    private void replaceFragment(@IdRes int fragmentContainerId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction()
                    .replace(fragmentContainerId, fragment)
                    .commit();
        }
    }

    private ArrayList<Step> getStepList() {
        return getIntent().getParcelableArrayListExtra("steps");
    }

    private ArrayList<Ingredient> getIngredientList() {
        return getIntent().getParcelableArrayListExtra("ingredients");
    }

    public Step getStep(int index) {
        if (index > getStepList().size() - 1) {
            return null;
        }
        return getStepList().get(index);
    }

    public void openStep(int stepPosition) {
        StepFragment stepFragment = StepFragment.newInstance(stepPosition);
        if (findViewById(R.id.fl_second) != null) {
            replaceFragment(R.id.fl_second, stepFragment);
        } else {
            replaceFragment(R.id.fl_main, stepFragment);
        }
    }
}
