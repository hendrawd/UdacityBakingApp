package hendraganteng.udacitybakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.activity.DetailActivity;
import hendraganteng.udacitybakingapp.network.model.Ingredient;
import hendraganteng.udacitybakingapp.network.model.Recipe;
import hendraganteng.udacitybakingapp.network.model.Step;

/**
 * @author hendrawd on 8/20/17
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        Recipe recipe = recipeList.get(position);

        final String imageUrl = recipe.getImage();
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.ivRecipeImage);
        }
        holder.tvTitle.setText(context.getString(R.string.recipe_name, recipe.getName()));
        holder.tvIngredient.setText(context.getString(R.string.ingredient_count, recipe.getIngredients().size()));
        holder.tvStep.setText(context.getString(R.string.step_count, recipe.getSteps().size()));
        holder.tvServing.setText(context.getString(R.string.serving_count, recipe.getServings()));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @BindView(R.id.iv_recipe_image)
        ImageView ivRecipeImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;
        @BindView(R.id.tv_step)
        TextView tvStep;
        @BindView(R.id.tv_serving)
        TextView tvServing;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Context context = v.getContext();
            int itemPosition = getAdapterPosition();
            Recipe recipe = recipeList.get(itemPosition);
            Intent openDetailActivity = new Intent(context, DetailActivity.class);

            ArrayList<Ingredient> ingredientArrayList = new ArrayList<>(recipe.getIngredients());
            openDetailActivity.putParcelableArrayListExtra("ingredients", ingredientArrayList);

            ArrayList<Step> stepArrayList = new ArrayList<>(recipe.getSteps());
            openDetailActivity.putParcelableArrayListExtra("steps", stepArrayList);

            context.startActivity(openDetailActivity);
        }
    }
}
