package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.UserListAdapter;
import com.attribes.push2beat.databinding.FragmentCatchMeBinding;
import com.attribes.push2beat.models.BodyParams.GetListRequestParams;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.attribes.push2beat.network.DAL.ListOfUserDAL;
import com.attribes.push2beat.network.interfaces.UsersArrivalListener;

import java.util.List;

/**
 * Created by android on 12/11/16.
 */
public class CatchMeFragment extends Fragment {




    public CatchMeFragment() {
    }

    private FragmentCatchMeBinding binding;
    private RecyclerView mRecycle;

    private CatchMeFragment.OnStartButtonListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catch_me,container,false);
        View view = binding.getRoot();
        initMapFragment();
        init();

        fetchUsers();



        return view;
    }

    private void initMapFragment() {
        MapFragment fragment = new MapFragment(DevicePreferences.getInstance().getLocation());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.catchme_map_view,fragment,Constants.CATCH_MAP_TAG).commit();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
//        listener = (CatchMeFragment.OnStartButtonListener) childFragment;
    }


    private void init() {
        onAttachFragment(getParentFragment());


        binding.progress.progressWheel.setVisibility(View.VISIBLE);
        mRecycle = binding.myRecyclerView;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(mLayoutManager);
//        getMapFragment().moveMapCamera(Common.getInstance().getLocation().getLatitude(),Common.getInstance().getLocation().getLongitude());


    }

    private void fetchUsers() {
        GetListRequestParams params = new GetListRequestParams();

         params.setUser_id(Integer.parseInt(DevicePreferences.getInstance().getuser().getId()));
        params.setLat(DevicePreferences.getInstance().getLocation().getLatitude());
        params.setLng(DevicePreferences.getInstance().getLocation().getLongitude());



        ListOfUserDAL.getUserList(params, new UsersArrivalListener() {
            @Override
            public void onDataRecieved(final List<Datum> data)
            {
                binding.progress.progressWheel.setVisibility(View.GONE);

                mRecycle.setAdapter(new UserListAdapter(data, new RecyclerAdapterInterface() {
                    @Override
                    public void onstartCallback(int position) {
                        startLoaderFragment();
                        Common.getInstance().setOpponentData(data.get(position));
                   //  listener.onStartCmiyc(data.get(position));
                    }
                }));

                displayUserOnMap(data);

            }

            @Override
            public void onEmptyData(String msg) {
                binding.progress.progressWheel.setVisibility(View.GONE);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startLoaderFragment() {
        LoaderFragment fragment = new LoaderFragment("Waiting for opponent...");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.base_view,fragment,Constants.CATCH_LOADER_TAG).commit();

    }



    private void displayUserOnMap(List<Datum> data)
    {

        if(data!=null)
        {
            getMapFragment().showUsers(data);

        }
    }

    private MapFragment getMapFragment()
    {
        FragmentManager fm = getFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.CATCH_MAP_TAG);
        return fragment;
    }



    public interface OnStartButtonListener
    {
        void onStartCmiyc(Datum datum);
    }




}
