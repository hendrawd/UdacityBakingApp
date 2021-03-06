package hendraganteng.udacitybakingapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hendraganteng.udacitybakingapp.R;
import hendraganteng.udacitybakingapp.activity.DetailActivity;
import hendraganteng.udacitybakingapp.network.model.Step;

public class StepFragment extends Fragment {

    private static final String ALLOWED_VIDEO_EXTENSION = ".mp4";
    private static final String KEY_PLAYBACK_POSITION = "key_playback_position";
    private static final String KEY_PLAY_WHEN_READY = "key_play_when_ready";
    private static final String KEY_CURRENT_WINDOW = "key_current_window";
    private static final String KEY_STEP_INDEX = "step_index";
    @BindView(R.id.sepv)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.iv)
    ImageView imageView;
    @BindView(R.id.tv_step)
    TextView tvStep;
    @BindView(R.id.b_prev)
    Button bPrev;
    @BindView(R.id.b_next)
    Button bNext;
    Unbinder unbinder;
    private SimpleExoPlayer mPlayer;
    private long mPlaybackPosition;
    private boolean mPlayWhenReady;
    private int mCurrentWindow;
    private int mStepIndex;

    @OnClick({
            R.id.b_prev,
            R.id.b_next
    })
    public void buttonClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.b_prev:
                changeContent(getPrevStep());
                break;
            case R.id.b_next:
                changeContent(getNextStep());
                break;
        }
    }

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(int stepIndex) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_STEP_INDEX, stepIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mStepIndex = arguments.getInt(KEY_STEP_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, parent);
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        showContent(getStep(mStepIndex));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showContent(Step step) {
        simpleExoPlayerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        tvStep.setText(step.getDescription());
        showVideo(step);

        //show default broken video icon
        if (simpleExoPlayerView.getVisibility() != View.VISIBLE &&
                imageView.getVisibility() != View.VISIBLE) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_broken_video);
        }

        if (mStepIndex == 0) {
            bPrev.setVisibility(View.INVISIBLE);
        } else {
            bPrev.setVisibility(View.VISIBLE);
        }
        if (getNextStep() == null) {
            mStepIndex--;
            bNext.setVisibility(View.INVISIBLE);
        } else {
            mStepIndex--;
            bNext.setVisibility(View.VISIBLE);
        }
    }

    private Step getStep(int index) {
        DetailActivity detailActivity = (DetailActivity) getActivity();
        return detailActivity.getStep(index);
    }

    private Step getPrevStep() {
        return getStep(--mStepIndex);
    }

    private Step getNextStep() {
        return getStep(++mStepIndex);
    }

    private void showVideo(Step step) {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );

        simpleExoPlayerView.setPlayer(mPlayer);

        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

        if (!TextUtils.isEmpty(step.getVideoURL()) ||
                !TextUtils.isEmpty(step.getThumbnailURL())) {
            Uri uri = null;
            String thumbnailUrl = step.getThumbnailURL();
            if (!TextUtils.isEmpty(step.getVideoURL())) {
                uri = Uri.parse(step.getVideoURL());
            } else if (!TextUtils.isEmpty(thumbnailUrl)) {
                if (thumbnailUrl.endsWith(ALLOWED_VIDEO_EXTENSION)) {
                    uri = Uri.parse(step.getThumbnailURL());
                } else {
                    showImage(imageView, step.getThumbnailURL());
                }
            }
            if (uri != null) {
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                MediaSource mediaSource = buildMediaSource(uri);
                mPlayer.prepare(mediaSource, true, false);
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(
                uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(),
                null,
                null
        );
    }

    private void resetPlayerState() {
        mPlaybackPosition = 0;
        mPlayWhenReady = false;
        mCurrentWindow = 0;
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_PLAYBACK_POSITION, mPlayer.getCurrentPosition());
        outState.putBoolean(KEY_PLAY_WHEN_READY, mPlayer.getPlayWhenReady());
        outState.putInt(KEY_CURRENT_WINDOW, mPlayer.getCurrentWindowIndex());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_PLAYBACK_POSITION)) {
                mPlaybackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
            }
            if (savedInstanceState.containsKey(KEY_PLAY_WHEN_READY)) {
                mPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            }
            if (savedInstanceState.containsKey(KEY_CURRENT_WINDOW)) {
                mCurrentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void showImage(ImageView imageView, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl) || imageUrl.endsWith(ALLOWED_VIDEO_EXTENSION)) {
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        Picasso.with(getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_broken_video)
                .error(R.drawable.ic_broken_video)
                .into(imageView);
    }

    public void changeContent(Step step) {
        resetPlayerState();
        releasePlayer();
        showContent(step);
    }

    public void setStepIndex(int stepIndex) {
        this.mStepIndex = stepIndex;
    }
}
