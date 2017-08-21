package hendraganteng.udacitybakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.adapter.DetailAdapter;
import hendraganteng.udacitybakingapp.network.model.Ingredient;
import hendraganteng.udacitybakingapp.network.model.Step;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

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
        List<Ingredient> ingredientList = getIntent().getParcelableArrayListExtra("ingredients");
        List<Step> stepList = getIntent().getParcelableArrayListExtra("steps");

        DetailAdapter detailAdapter = new DetailAdapter(DetailActivity.this, ingredientList, stepList);
        rv.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
        rv.setAdapter(detailAdapter);
    }
}
