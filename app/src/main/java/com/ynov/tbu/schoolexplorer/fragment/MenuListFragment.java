package com.ynov.tbu.schoolexplorer.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.activity.MapsActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActionBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActionBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuListFragment extends android.app.Fragment {
    private OnFragmentInteractionListener mListener;

    public MenuListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuListFragment newInstance() {
        MenuListFragment fragment = new MenuListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);

        ImageView btnMap = view.findViewById(R.id.image_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuListFragment.this.getActivity().getBaseContext(), MapsActivity.class);
                MenuListFragment.this.getActivity().startActivity(myIntent);
            }
        });

        ImageView btnEdit = view.findViewById(R.id.image_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickEditMode();
            }
        });

        return view;
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
        void onClickEditMode();
    }
}
