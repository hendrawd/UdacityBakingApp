package hendraganteng.udacitybakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
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
import hendraganteng.udacitybakingapp.network.model.Ingredient;
import hendraganteng.udacitybakingapp.network.model.Recipe;
import hendraganteng.udacitybakingapp.util.CustomToast;
import hendraganteng.udacitybakingapp.util.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static hendraganteng.udacitybakingapp.R.id.rv;

public class RecipeAppWidgetConfigureActivity extends AppCompatActivity
        implements Callback<List<Recipe>>, RecipeAdapter.ListItemClick {

    private static final String TAG = "RecipeAppWidgetConfigur";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    @BindView(rv)
    RecyclerView mRecyclerView;
    private Call<List<Recipe>> mCall;
    private List<Recipe> mRecipeList;

    public RecipeAppWidgetConfigureActivity() {
        super();
    }

    static void saveIngredients(Context context, int appWidgetId, String text) {
        PreferencesManager.putString(context, PREF_PREFIX_KEY + appWidgetId, text);
    }

    static String loadIngredients(Context context, int appWidgetId) {
        String titleValue = PreferencesManager.getString(context, PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteIngredients(Context context, int appWidgetId) {
        PreferencesManager.remove(context, PREF_PREFIX_KEY + appWidgetId);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.recipe_app_widget_configure);
        ButterKnife.bind(this);

        init();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        requestRecipes();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void requestRecipes() {
        mCall = ServiceGenerator
                .createService(BakingService.class)
                .getRecipes();

        // why this class implement interface instead of anonymous class?
        // it came from book: "Effective Java, Item 5: Avoid creating unnecessary objects"
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
        mRecipeList = response.body();
        if (mRecipeList != null) {
            RecipeAdapter recipeAdapter = new RecipeAdapter(this, mRecipeList);
            mRecyclerView.setAdapter(recipeAdapter);
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
        //TODO handle error
        Log.e(TAG, "onFailure: " + t.getMessage(), t);
        CustomToast.show(this, t.getMessage());
    }

    @Override
    public void onListItemClick(Recipe recipe) {
        CustomToast.show(this, recipe.getName());

        List<Ingredient> ingredientList = recipe.getIngredients();
        StringBuilder ingredientBuilder = new StringBuilder();
        for (Ingredient ingredient : ingredientList) {
            ingredientBuilder.append(ingredient.getQuantity());
            ingredientBuilder.append(" of ");
            ingredientBuilder.append(ingredient.getIngredient());
            ingredientBuilder.append("\n");
        }

        saveIngredients(this, mAppWidgetId, ingredientBuilder.toString());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RecipeAppWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

