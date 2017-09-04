package hendraganteng.udacitybakingapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.adapter.DetailAdapter;
import hendraganteng.udacitybakingapp.network.model.Ingredient;
import hendraganteng.udacitybakingapp.network.model.Step;

public class DetailFragment extends Fragment {

    private static final String KEY_INGREDIENT = "INGREDIENT";
    private static final String KEY_STEP = "STEP";

    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Step> stepList;
    private RecyclerView recyclerView;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(ArrayList<Ingredient> ingredientList, ArrayList<Step> stepList) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_INGREDIENT, ingredientList);
        args.putParcelableArrayList(KEY_STEP, stepList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredientList = getArguments().getParcelableArrayList(KEY_INGREDIENT);
            stepList = getArguments().getParcelableArrayList(KEY_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_detail, container, false);
        recyclerView = (RecyclerView) parent.findViewById(R.id.rv);
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupContent();
    }

    private void setupContent() {
        DetailAdapter detailAdapter = new DetailAdapter(getContext(), ingredientList, stepList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(detailAdapter);
    }

}
