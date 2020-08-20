package com.agro.agro.ui.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.agro.agro.R;
import com.agro.agro.api.ApiService;
import com.agro.agro.api.ApiServiceGenerator;
import com.agro.agro.entity.DateEntity;
import com.agro.agro.models.ApiRequest;
import com.agro.agro.models.ApiResponse;
import com.agro.agro.utils.EnumButtonStatus;
import com.agro.agro.viewmodels.DatabaseViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HomeFragment extends Fragment {

    private String videoURL = "";

    private VLCVideoLayout mVideoLayout = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener_ip_address;

    private long diffDays = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        DatabaseViewModel viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);


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

        SwitchMaterial switchMaterial = view.findViewById(R.id.switch_fertilize);

        DatabaseViewModel viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        viewModel.check().observe(getViewLifecycleOwner(), new Observer<List<DateEntity>>() {
            @Override
            public void onChanged(List<DateEntity> dateEntities) {
                Timber.e("Da : %s", dateEntities.toString());

                long diff = Math.abs(Calendar.getInstance().getTime().getTime() - dateEntities.get(0).date.getTime());
                diffDays = diff / (24 * 60 * 60 * 1000);
                Timber.e("Date Difference : %s", diffDays);
            }
        });

        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (diffDays < 7) {
                        buttonView.setChecked(false);
                        Toast.makeText(getContext(), "Will activate after " + String.valueOf(7 - diffDays) + " days.", Toast.LENGTH_SHORT).show();
                    } else {
                        buttonView.setChecked(true);
                        buttonView.setEnabled(false);

                        ApiService service = ApiServiceGenerator.createService(ApiService.class);

                        Call<ApiResponse> call = service.postData("button", new ApiRequest("button", EnumButtonStatus.ON));

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                                if (response.isSuccessful()) {
                                    viewModel.update();
                                } else {
                                    try {
                                        assert response.errorBody() != null;
                                        Timber.e("Res : %s", response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getContext(), "Error Connection.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                                Timber.e(t, "Err : ");
                                Toast.makeText(getContext(), "Error Connection.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
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
