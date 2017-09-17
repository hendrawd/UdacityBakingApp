package hendraganteng.udacitybakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.activity.DetailActivity;
import hendraganteng.udacitybakingapp.network.model.Ingredient;
import hendraganteng.udacitybakingapp.network.model.Step;
import hendraganteng.udacitybakingapp.util.CustomToast;

/**
 * @author hendrawd on 8/20/17
 */

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int VIEW_TYPE_TITLE = 0;
    public static int VIEW_TYPE_INGREDIENT = 1;
    public static int VIEW_TYPE_STEP = 2;
    private int stepStartIndex;
    private List<DetailData> detailDataList;

    public DetailAdapter(Context context, List<Ingredient> ingredientList, List<Step> stepList) {
        detailDataList = new ArrayList<>();

        DetailData ingredientTitle = new DetailData();
        ingredientTitle.setContent(context.getString(R.string.ingredient));
        detailDataList.add(ingredientTitle);
        stepStartIndex++;

        for (Ingredient ingredient : ingredientList) {
            DetailData ingredientDetailData = new DetailData();
            ingredientDetailData.setContent(ingredient);
            detailDataList.add(ingredientDetailData);
            stepStartIndex++;
        }

        DetailData stepTitle = new DetailData();
        stepTitle.setContent(context.getString(R.string.step));
        detailDataList.add(stepTitle);
        stepStartIndex++;

        for (Step step : stepList) {
            DetailData stepDetailData = new DetailData();
            stepDetailData.setContent(step);
            detailDataList.add(stepDetailData);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_TITLE) {
            view = layoutInflater.inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == VIEW_TYPE_INGREDIENT) {
            view = layoutInflater.inflate(R.layout.item_ingredient, parent, false);
            return new IngredientViewHolder(view);
        } else if (viewType == VIEW_TYPE_STEP) {
            view = layoutInflater.inflate(R.layout.item_step, parent, false);
            return new StepViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        DetailData detailData = detailDataList.get(position);
        int viewType = detailData.getType();
        if (viewType == VIEW_TYPE_TITLE) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.tvTitle.setText((String) detailData.getContent());
        } else if (viewType == VIEW_TYPE_INGREDIENT) {
            IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
            Ingredient ingredient = (Ingredient) detailData.getContent();
            ingredientViewHolder.tvIngredient.setText(
                    context.getString(
                            R.string.ingredient_item_content,
                            ingredient.getQuantity(),
                            ingredient.getMeasure(),
                            ingredient.getIngredient()
                    )
            );
        } else if (viewType == VIEW_TYPE_STEP) {
            StepViewHolder stepViewHolder = (StepViewHolder) holder;
            Step step = (Step) detailData.getContent();
            //TODO ini dilengkapi datanya
            //ini bener gak yang dipake shortDescription?
            stepViewHolder.tvStep.setText(step.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return detailDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return detailDataList.get(position).getType();
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_step)
        TextView tvStep;
        @BindView(R.id.iv_recipe_image)
        ImageView ivRecipeImage;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Context context = v.getContext();
            final int position = getAdapterPosition();
            final int stepPosition = position - stepStartIndex;
            if (context instanceof DetailActivity) {
                DetailActivity detailActivity = (DetailActivity) context;
                detailActivity.openStep(stepPosition);
            }
            CustomToast.show(v.getContext(), "Item with position " + position + " clicked, Step with position " + stepPosition + " clicked");
        }
    }

    public class DetailData {
        private Object object;

        public int getType() {
            if (object instanceof Ingredient) {
                return VIEW_TYPE_INGREDIENT;
            }
            if (object instanceof Step) {
                return VIEW_TYPE_STEP;
            }
            return VIEW_TYPE_TITLE;
        }

        public Object getContent() {
            return object;
        }

        public void setContent(Object object) {
            this.object = object;
        }
    }


}
