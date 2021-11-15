package com.stonybrook.videoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

//
//public final class DashPlayer extends AppCompatActivity {
//    private SimpleExoPlayer videoPlayer;
//    private String sampleUrl = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd";
//
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.activity_player);
//        this.initializePlayer();
//    }
//
//    private final MediaSource buildMediaSource() {
//        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory((Context) this, "sample");
//        DashMediaSource dashMediaSource = new DashMediaSource(Uri.parse(this.sampleUrl), dataSourceFactory, (com.google.android.exoplayer2.source.dash.DashChunkSource.Factory) (new com.google.android.exoplayer2.source.dash.DefaultDashChunkSource.Factory(dataSourceFactory)), null,  null);
//        return (MediaSource) dashMediaSource;
//    }
//
//    private final void initializePlayer() {
//        this.videoPlayer = (new SimpleExoPlayer.Builder((Context) this)).build();
//        PlayerView simpleExoPlayer = (PlayerView) this.findViewById(R.id.player_view);
//        if (simpleExoPlayer != null) {
//            simpleExoPlayer.setPlayer((Player) this.videoPlayer);
//        }
//
//        MediaSource mediaSource = this.buildMediaSource();
//        if (mediaSource != null) {
//            if (this.videoPlayer != null) {
//                this.videoPlayer.prepare(mediaSource);
//            }
//        }
//
//    }
//
//    protected void onResume() {
//        super.onResume();
//        SimpleExoPlayer simpleExoPlayer = this.videoPlayer;
//        if (simpleExoPlayer != null) {
//            simpleExoPlayer.setPlayWhenReady(true);
//        }
//
//    }
//
//    protected void onStop() {
//        super.onStop();
//        SimpleExoPlayer simpleExoPlayer = this.videoPlayer;
//        if (simpleExoPlayer != null) {
//            simpleExoPlayer.setPlayWhenReady(false);
//        }
//
//        if (this.isFinishing()) {
//            this.releasePlayer();
//        }
//
//    }
//
//    private final void releasePlayer() {
//        SimpleExoPlayer simpleExoPlayer = this.videoPlayer;
//        if (simpleExoPlayer != null) {
//            simpleExoPlayer.release();
//        }
//
//    }
//
//}
