package hendraganteng.udacitybakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.adapter.RecipeAdapter;
import hendraganteng.udacitybakingapp.network.BakingService;
import hendraganteng.udacitybakingapp.network.ServiceGenerator;
import hendraganteng.udacitybakingapp.network.model.Recipe;
import hendraganteng.udacitybakingapp.util.CustomToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<Recipe>> {

    private static final String TAG = "MainActivity";
    @BindView(R.id.rv)
    RecyclerView rv;
    private Call<List<Recipe>> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestRecipes();
    }

    private void requestRecipes() {
        mCall = ServiceGenerator
                .createService(BakingService.class)
                .getRecipes();

        // why this class implement interface instead of anonymous class?
        // it came from book: "Effective Java, Item 5: Avoid creating unnecessary objects"
        mCall.enqueue(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null) {
            //cancel retrofit if activity is destroyed
            mCall.cancel();
        }
    }

    @Override
    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
        List<Recipe> recipeList = response.body();
        if (recipeList != null) {
            RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            rv.setLayoutManager(linearLayoutManager);
            rv.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
            rv.setAdapter(recipeAdapter);

            // for (Recipe recipe : recipeList) {
            //     Log.d(TAG, "onResponse: " + recipe.getName());
            // }
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
        //TODO handle error
        Log.e(TAG, "onFailure: " + t.getMessage(), t);
        CustomToast.show(MainActivity.this, t.getMessage());
    }
}
