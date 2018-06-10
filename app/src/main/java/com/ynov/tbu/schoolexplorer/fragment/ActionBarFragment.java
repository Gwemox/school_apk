package com.ynov.tbu.schoolexplorer.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ynov.tbu.schoolexplorer.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActionBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActionBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionBarFragment extends android.app.Fragment {
    private String titleBar;
    private Integer colorBar = Color.WHITE;
    private Integer showMenuResource = null;

    private OnFragmentInteractionListener mListener;

    public ActionBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param color Parameter 2.
     * @param showMenuResource Parameter 2. OPTIONAL
     * @return A new instance of fragment ActionBarFragment.
     */
    public static ActionBarFragment newInstance(String title, Integer color, Integer showMenuResource) {
        ActionBarFragment fragment = new ActionBarFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("color", color);
        if (showMenuResource != null ) {
            args.putInt("showMenuResource", showMenuResource);
        }
        fragment.setArguments(args);
        return fragment;
    }
    public static ActionBarFragment newInstance(String title, Integer color) {
        return newInstance(title, color, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleBar = getArguments().getString("title", "SchoolExplorer");
            colorBar = getArguments().getInt("color", Color.WHITE);
            showMenuResource = getArguments().getInt("showMenuResource");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action_bar, container, false);

        view.setBackgroundColor(colorBar);
        TextView title = view.findViewById(R.id.action_title);
        ImageView back = view.findViewById(R.id.image_action_back);
        title.setText(titleBar);

        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ActionBarFragment.this.getActivity().finish();
            }
        });


        ImageView list = view.findViewById(R.id.image_action_list);
        if (showMenuResource != null) {
            list.setVisibility(View.VISIBLE);
            list.setImageResource(showMenuResource);
            list.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    mListener.onMenuShow();
                }
            });
        } else {
            list.setVisibility(View.INVISIBLE);
        }

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
        void onMenuShow();
    }
}
