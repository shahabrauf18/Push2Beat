package com.attribes.push2beat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.databinding.FragmentSpeedometerBinding;
import com.github.anastr.speedviewlib.ImageSpeedometer;


/**
 * Created by Maaz on 12/14/16.
 */
public class SpeedoMeterFragment extends android.support.v4.app.Fragment {

    FragmentSpeedometerBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speedometer, container, false);


        initSpeednMeter(rootView);
        return rootView;
    }

    private void initSpeednMeter(View rootView) {
        ImageSpeedometer speedView = (ImageSpeedometer)rootView.findViewById(R.id.speed_view);
        speedView.setMaxSpeed(80);// change MAX speed to 320

        speedView.speedTo(1);
        speedView.speedTo(Common.getInstance().getSpeedValue(),4000);      // change speed to 140 Km/h   Common.getInstance().getSpeedValue() or 40


    }

}
