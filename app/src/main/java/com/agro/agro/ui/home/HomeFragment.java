package com.agro.agro.ui.home;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.agro.agro.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String videoURL = "";

    private VLCVideoLayout mVideoLayout = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener_ip_address;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        String ip_address = sharedPreferences.getString("video_ip_address", "");

        setVideoURL(ip_address);

        sharedPreferenceChangeListener_ip_address = (sharedPreferences, key) -> {
            if (key.equals("video_ip_address")) {
                setVideoURL(key);
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener_ip_address);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<String> args = new ArrayList<>();
        mLibVLC = new LibVLC(requireContext(), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mVideoLayout = view.findViewById(R.id.video_layout);
    }

    @Override
    public void onResume() {
        super.onResume();

        mMediaPlayer.attachViews(mVideoLayout, null, false, false);

        final Media media = new Media(mLibVLC, Uri.parse(videoURL));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();
    }

    @Override
    public void onStop() {
        super.onStop();

        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener_ip_address);
    }

    private void setVideoURL(String videoURL) {
        this.videoURL = "http://" + videoURL + "/mjpeg/1";
    }
}
