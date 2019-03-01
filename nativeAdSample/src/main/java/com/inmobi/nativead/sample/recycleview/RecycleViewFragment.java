package com.inmobi.nativead.sample.recycleview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.inmobi.nativead.sample.PlacementId;
import com.inmobi.nativead.sample.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecycleViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecycleViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecycleViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerview;
    private LinearLayoutManager mLayoutManager;
    private GridAdapter mAdapter;
    private List<Meizi> datas=new ArrayList<>();
    private InMobiNative nativead,nativead1,nativead2,nativead3,nativead4;

    private final static String TAG="RecycleViewFragment";

    public RecycleViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecycleViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecycleViewFragment newInstance(String param1, String param2) {
        RecycleViewFragment fragment = new RecycleViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_recycle_view, container, false);

        recyclerview=(RecyclerView)view.findViewById(R.id.grid_recycler);
        mLayoutManager=new LinearLayoutManager(getContext(),GridLayoutManager.VERTICAL,false);//设置为一个1  列的纵向网格布局
        recyclerview.setLayoutManager(mLayoutManager);

        mAdapter = new GridAdapter(getContext(),datas);
        recyclerview.setAdapter(mAdapter);//recyclerview设置适配器

        nativead=new InMobiNative(getContext(), PlacementId.YOUR_PLACEMENT_ID_RECYCLEVIEW,listener);
        nativead.load();

        nativead1=new InMobiNative(getContext(), PlacementId.YOUR_PLACEMENT_ID_RECYCLEVIEW,listener);
        nativead1.load();

        nativead2=new InMobiNative(getContext(), PlacementId.YOUR_PLACEMENT_ID_RECYCLEVIEW,listener);
        nativead2.load();

        nativead3=new InMobiNative(getContext(), PlacementId.YOUR_PLACEMENT_ID_RECYCLEVIEW,listener);
        nativead3.load();

        nativead4=new InMobiNative(getContext(), PlacementId.YOUR_PLACEMENT_ID_RECYCLEVIEW,listener);
        nativead4.load();

        return view;
    }

    private NativeAdEventListener listener=new NativeAdEventListener() {
        @Override
        public void onAdLoadSucceeded(InMobiNative inMobiNative) {
            super.onAdLoadSucceeded(inMobiNative);
            Log.e(TAG,"onAdLoadSucceeded");
            Meizi data=new Meizi();

            Log.e(TAG,"onAdLoadSucceeded"+inMobiNative.getAdTitle()+inMobiNative.getAdLandingPageUrl());
            Log.e(TAG,"onAdLoadSucceeded"+(inMobiNative==null?"Null":"NoNull"));
            data.setInMobiNative(new WeakReference<InMobiNative>(inMobiNative));
            datas.add(data);

            Meizi data1=new Meizi();
            data1.setUrl("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=3d2175db3cd3d539d530078052ee8325/b7003af33a87e950c1e1a6491a385343fbf2b425.jpg");
            datas.add(data1);

            mAdapter.notifyDataSetChanged();
            //http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=3d2175db3cd3d539d530078052ee8325/b7003af33a87e950c1e1a6491a385343fbf2b425.jpg


        }

        @Override
        public void onAdLoadFailed(InMobiNative inMobiNative, InMobiAdRequestStatus inMobiAdRequestStatus) {
            super.onAdLoadFailed(inMobiNative, inMobiAdRequestStatus);
            Log.e(TAG, "Failed to load ad. " + inMobiAdRequestStatus.getMessage());
        }

        @Override
        public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
            super.onAdFullScreenDisplayed(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
        }

        @Override
        public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
            super.onAdFullScreenWillDisplay(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
        }

        @Override
        public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
            super.onAdFullScreenDismissed(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
        }

        @Override
        public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
            super.onUserWillLeaveApplication(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
        }

        @Override
        public void onAdImpressed(InMobiNative inMobiNative) {
            super.onAdImpressed(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
            Toast.makeText(getActivity(),"AdImpressed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdClicked(InMobiNative inMobiNative) {
            super.onAdClicked(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
            Toast.makeText(getActivity(),"AdClicked",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdStatusChanged(InMobiNative inMobiNative) {
            super.onAdStatusChanged(inMobiNative);
            Log.e(TAG, "onAdFullScreenDisplayed ");
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
