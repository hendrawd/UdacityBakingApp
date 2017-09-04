package hendraganteng.udacitybakingapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.activity.DetailActivity;
import hendraganteng.udacitybakingapp.network.model.Step;

public class StepFragment extends Fragment {

    private int index;
    private static final String KEY_INDEX = "index";

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(int index) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(KEY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    private Step getStep(int index) {
        DetailActivity detailActivity = (DetailActivity) getActivity();
        return detailActivity.getStep(index);
    }

}
