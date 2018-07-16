package com.maluta.bakingtime;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 6/30/2018.
 */

@SuppressWarnings("ALL")
public class StepFragment extends Fragment implements Player.EventListener{
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.description_tv)
    TextView mDescriptionTextView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.simple_exo_player)
    SimpleExoPlayerView mSimpleExoPlayerView;
    @BindView(R.id.thumbnail_iv)
    ImageView mThumnbnailIV;
    @BindView(R.id.recipe_step_desc_card)
    CardView descriptionCard;
    private SimpleExoPlayer mPlayer;


    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private MediaSource mMediaSource;

    //private String video;
    private long mPlayerPosition;
    private static String video = "";
    private static boolean hasVideo;

    private static final String EXTRA_DESCRIPTION_ID = "EXTRA_DESCRIPTION_ID";
    private static final String EXTRA_VIDEO_URL_ID = "EXTRA_VIDEO_URL_ID";
    private static final String EXTRA_IMAGE_URL_ID = "EXTRA_IMAGE_URL_ID";

    // Required empty public constructor
    public StepFragment() {
    }

    public static StepFragment newInstance(String description, String videoUrl,
                                                           String imageUrl) {

        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_DESCRIPTION_ID, description);
        arguments.putString(EXTRA_VIDEO_URL_ID, videoUrl);
        arguments.putString(EXTRA_IMAGE_URL_ID, imageUrl);
        StepFragment fragment = new StepFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_info_with_player, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String description = getArguments().getString(EXTRA_DESCRIPTION_ID);
        mDescriptionTextView.setText(description);

        String imageUrl = getArguments().getString(EXTRA_IMAGE_URL_ID);
        if (!imageUrl.isEmpty()) {
            Picasso.with(getActivity().getApplicationContext()).load(imageUrl)
                    .placeholder(R.drawable.recipe)
                    .error(R.drawable.recipe)
                    .into(mThumnbnailIV);
        } else {
            // Hide image view
            setViewVisibility(mThumnbnailIV, false);
        }

        video = getArguments().getString(EXTRA_VIDEO_URL_ID);

        //Toast.makeText(getActivity(), String.valueOf(hasVideo), Toast.LENGTH_LONG).show();
        if (!video.isEmpty()) {
            //hasVideo = true;
            // Init and show video view
            setViewVisibility(mSimpleExoPlayerView, true);
            initializeMediaSession();
            initializePlayer(Uri.parse(video));


        } else {
            //hasVideo = false;
            // Hide video view
            setViewVisibility(mSimpleExoPlayerView, false);
            // Show image view
            setViewVisibility(mThumnbnailIV, true);
            mThumnbnailIV.setImageResource(R.drawable.recipe);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            video = getArguments().getString(EXTRA_VIDEO_URL_ID);
            if (!video.isEmpty()) {
                hasVideo = true;
                Activity activity = getActivity();
                if(activity != null) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !DetailActivity.mTwoPane) {
                        expandVideoView(mSimpleExoPlayerView);
                        setViewVisibility(descriptionCard, false);
                        hideSystemUI();
                    }
                }
            } else {
                hasVideo = false;

                Activity activity = getActivity();
                if(activity != null) {
                    // Hide video view
                    setViewVisibility(mSimpleExoPlayerView, false);
                    setViewVisibility(descriptionCard, true);
                    String description = getArguments().getString(EXTRA_DESCRIPTION_ID);
                    mDescriptionTextView.setText(description);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !DetailActivity.mTwoPane) {


                        String imageUrl = getArguments().getString(EXTRA_IMAGE_URL_ID);
                        if (!imageUrl.isEmpty()) {
                            Picasso.with(getActivity().getApplicationContext()).load(imageUrl)
                                    .placeholder(R.drawable.recipe)
                                    .error(R.drawable.recipe)
                                    .into(mThumnbnailIV);
                        } else {
                            setViewVisibility(mThumnbnailIV, true);
                            mThumnbnailIV.setImageResource(R.drawable.recipe);
                        }


                        // Hide video view
                        //setViewVisibility(mSimpleExoPlayerView, false);


                        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                        getActivity().findViewById(R.id.recipe_step_tablayout).setVisibility(View.VISIBLE);
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }
            //Toast.makeText(getActivity(), String.valueOf(hasVideo), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void setViewVisibility(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void expandVideoView(SimpleExoPlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = LayoutParams.MATCH_PARENT;
    }

    private void shrinkVideoView(SimpleExoPlayerView exoPlayer){
        exoPlayer.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        exoPlayer.getLayoutParams().width = LayoutParams.MATCH_PARENT;
    }

    private void hideSystemUI() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        getActivity().findViewById(R.id.recipe_step_tablayout).setVisibility(View.GONE);
        //Use Google's "LeanBack" mode to get fullscreen in landscape
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    // helper method to set up simpleExoPlayer
    private void initializePlayer(Uri mediaUri) {
        if (mPlayer == null) {
            // create instance of exoPlayer with default settings
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderers = new DefaultRenderersFactory(getContext());
            mPlayer = ExoPlayerFactory.newSimpleInstance(renderers, trackSelector, loadControl);
            // sets to recent position if orientation changed mid-session
            if (mPlayerPosition != 0) {
                mPlayer.seekTo(mPlayerPosition);
            }
            mSimpleExoPlayerView.setPlayer(mPlayer);

            // builds media source then prepares player with media
            mMediaSource = buildMediaSource(mediaUri);
            mPlayer.prepare(mMediaSource);
            mPlayer.setPlayWhenReady(true);
            mPlayer.addListener(this);
        }
    }

    // method to release player and free up resources
    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayerPosition = mPlayer.getCurrentPosition();
            mMediaSource.releaseSource();
            mMediaSession.release();
            mPlayer.clearVideoSurface();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mMediaSession.setActive(false);
        }
    }

    // helper method that builds media source
    private MediaSource buildMediaSource(Uri uri) {
        // initializes some player defaults to use in initialization of media source
        DataSource.Factory mMediaDataSourceFactory = new DefaultDataSourceFactory(getActivity().getApplicationContext(),
                Util.getUserAgent(getContext(),
                        "bakingtime"));

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        return new ExtractorMediaSource(uri,
                mMediaDataSourceFactory,
                extractorsFactory,
                null,
                null);
    }

    // Sets up media session
    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity().getApplicationContext(), "TAG");
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new SessionCallback());
        mMediaSession.setActive(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // checking the orientation of screen
        if (hasVideo) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !DetailActivity.mTwoPane) {
                Log.d("ORIENTATION", " LANDSCAPE");
                // sets the boolean tracker used to adjust layout on orientation
                //isLandscape = true;
                expandVideoView(mSimpleExoPlayerView);
                setViewVisibility(descriptionCard, false);
                hideSystemUI();
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !DetailActivity.mTwoPane) {
                Log.d("ORIENTATION", " PORTRAIT");
                // sets the boolean tracker used to adjust layout on orientation
                //isLandscape = false;
                shrinkVideoView(mSimpleExoPlayerView);
                setViewVisibility(descriptionCard, true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                getActivity().findViewById(R.id.recipe_step_tablayout).setVisibility(View.VISIBLE);
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    private class SessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mPlayer.seekTo(0);
        }
    }
}
