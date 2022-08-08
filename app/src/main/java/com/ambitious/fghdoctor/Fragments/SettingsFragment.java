package com.ambitious.fghdoctor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;

public class SettingsFragment extends Fragment {

    private View view;
    private Switch switch_Noti;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_settings, container, false);
        finds();

        if (Utility.getSharedPreferencesBoolean(getContext(), "allownotifications", true)) {
            switch_Noti.setChecked(true);
        } else {
            switch_Noti.setChecked(false);
        }


        switch_Noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CustomSnakbar.showDarkSnakabar(getContext(), buttonView, "Notifications ON");
                    Utility.setSharedPreferenceBoolean(getContext(), "allownotifications", true);
                } else {
                    CustomSnakbar.showDarkSnakabar(getContext(), buttonView, "Notifications OFF");
                    Utility.setSharedPreferenceBoolean(getContext(), "allownotifications", false);
                }
            }
        });

        return view;
    }

    private void finds() {

        switch_Noti = view.findViewById(R.id.switch_Noti);

    }
}
