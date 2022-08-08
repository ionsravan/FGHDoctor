package com.ambitious.fghdoctor.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ambitious.fghdoctor.R;

public class CallSupportFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private ImageView iv_Call, iv_Calltwo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_call_support, container, false);
        finds();

        return mView;
    }

    private void finds() {

        iv_Call = mView.findViewById(R.id.iv_Call);
        iv_Calltwo = mView.findViewById(R.id.iv_Calltwo);

        iv_Call.setOnClickListener(this);
        iv_Calltwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+918553445936"));
                startActivity(intent);
                break;

            case R.id.iv_Calltwo:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "+917093538380"));
                startActivity(callIntent);
                break;

        }

    }
}
