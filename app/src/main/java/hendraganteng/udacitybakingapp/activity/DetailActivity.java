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
                // handle back button click
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }
        return false;
    }

    private void setupBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupContent() {
        DetailFragment detailFragment = DetailFragment.newInstance(
                getIngredientList(),
                getStepList()
        );
        replaceFragment(R.id.fl_detail, detailFragment);

        if (findViewById(R.id.fl_step) != null) {
            StepFragment stepFragment = StepFragment.newInstance(0);
            replaceFragment(R.id.fl_step, stepFragment);
        }
    }

    private void replaceFragment(@IdRes int fragmentContainerId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commit();
        }
    }

    private ArrayList<Step> getStepList() {
        return getIntent().getParcelableArrayListExtra("steps");
    }

    private ArrayList<Ingredient> getIngredientList() {
        return getIntent().getParcelableArrayListExtra("ingredients");
    }

    public Step getStep(int index) {
        return getStepList().get(index);
    }
}
