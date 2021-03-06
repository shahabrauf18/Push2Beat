package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.TrackListAdapter;
import com.attribes.push2beat.databinding.FragmentGhostRiderBinding;
import com.attribes.push2beat.models.BodyParams.GetListRequestParams;
import com.attribes.push2beat.models.Response.TrackList.Datum;
import com.attribes.push2beat.network.DAL.ListOfTrackDAL;
import com.attribes.push2beat.network.interfaces.TracksArrivalListener;

import java.util.List;

/**
 * Created by android on 12/17/16.
 */




public class GhostRiderFragment extends Fragment {

   private FragmentGhostRiderBinding binding;
    private OnStartButtonListener listener;
    private RecyclerView mRecycle;


    @Override
    public void onAttachFragment(Fragment childFragment) {
        listener = (OnStartButtonListener) childFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ghost_rider,container,false);
        View view = binding.getRoot();
        init();
        fetchTracks();
        return view;
    }

    private void init() {
        onAttachFragment(getParentFragment());

        binding.progress.progressWheel.setVisibility(View.VISIBLE);

        mRecycle = binding.ghostRecyclerView;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(mLayoutManager);
    }



    private void fetchTracks() {
        GetListRequestParams params = new GetListRequestParams();

        params.setUser_id(Integer.parseInt(DevicePreferences.getInstance().getuser().getId()));
        params.setLat(DevicePreferences.getInstance().getLocation().getLatitude());
        params.setLng(DevicePreferences.getInstance().getLocation().getLongitude());

        ListOfTrackDAL.getTrackList(params, new TracksArrivalListener() {
            @Override
            public void onDataRecieved(final List<Datum> data) {
               binding.progress.progressWheel.setVisibility(View.GONE);
               mRecycle.setAdapter(new TrackListAdapter(data, new RecyclerAdapterInterface() {
                   @Override
                   public void onstartCallback(int position) {

                       Datum datum = data.get(position);
                       listener.onStartGhostRider(datum);
                   }
               }));
            }

            @Override
            public void onEmptyData(String msg) {
                Toast.makeText(getContext(), "No Tracks found", Toast.LENGTH_SHORT).show();
                binding.progress.progressWheel.setVisibility(View.GONE);
            }
        });
    }




    public interface OnStartButtonListener
    {
        void onStartGhostRider(Datum datum);
    }

}
