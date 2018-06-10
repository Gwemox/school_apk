package com.ynov.tbu.schoolexplorer.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.SchoolExplorerApplication;
import com.ynov.tbu.schoolexplorer.activity.ListSchoolsActivity;
import com.ynov.tbu.schoolexplorer.controller.ApiRequest;
import com.ynov.tbu.schoolexplorer.controller.SchoolController;
import com.ynov.tbu.schoolexplorer.model.School;
import com.ynov.tbu.schoolexplorer.response.SchoolResponse;
import com.ynov.tbu.schoolexplorer.utils.GPSTracker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowSchoolFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowSchoolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowSchoolFragment extends android.app.Fragment {

    private School school = null;
    private TextView textName;
    private TextView textAddress;
    private TextView textCity;
    private TextView textNbStudents;
    private TextView textDistance;
    private ImageView imageSchool;
    private ImageView imageMap;
    private ImageView imageClose;
    private ImageView imageDelete;

    private OnFragmentInteractionListener mListener;

    public ShowSchoolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param school Parameter 1.
     * @return A new instance of fragment ActionBarFragment.
     */
    public static ShowSchoolFragment newInstance(School school) {
        ShowSchoolFragment fragment = new ShowSchoolFragment();
        Bundle args = new Bundle();
        args.putSerializable("school", school);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            school = (School)getArguments().getSerializable("school");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cell_school, container, false);

        //c'est ici que l'on fait nos findView
        textName = view.findViewById(R.id.text_school_name);
        textAddress = view.findViewById(R.id.text_school_address);
        textCity = view.findViewById(R.id.text_school_city);
        textNbStudents = view.findViewById(R.id.text_school_nb_student);
        textDistance = view.findViewById(R.id.text_school_distance);
        imageSchool = view.findViewById(R.id.imageSchool);

        imageClose = view.findViewById(R.id.image_close);
        imageMap = view.findViewById(R.id.image_map);
        imageDelete = view.findViewById(R.id.image_delete);

        imageClose.setVisibility(View.VISIBLE);
        imageClose.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mListener.onCloseView();
            }
        });
        imageMap.setVisibility(View.INVISIBLE);
        imageDelete.setVisibility(View.INVISIBLE);

        textName.setText(school.getName());
        textAddress.setText(school.getAddress());
        textCity.setText(SchoolExplorerApplication.getInstance().getApplicationContext().getString(R.string.zip_code_city, school.getZipCode(), school.getCity()));
        textNbStudents.setText(SchoolExplorerApplication.getInstance().getApplicationContext().getString(R.string.students_message, school.getNumberStudent()));

        double currentLat = GPSTracker.getInstance().getLatitude();
        double currentLong = GPSTracker.getInstance().getLongitude();
        double distance = GPSTracker.meterDistanceBetweenPoints(currentLat, currentLong, school.getLatitude(), school.getLongitude()) / 1000; // En km
        textDistance.setText(SchoolExplorerApplication.getInstance().getApplicationContext().getString(R.string.distance_message, distance));

        if (school.getNumberStudent() < 50) {
            view.setBackgroundColor(Color.RED);
            imageSchool.setImageResource(R.drawable.ko_icon);
        } else if (school.getNumberStudent() < 200) {
            view.setBackgroundColor(Color.rgb(255, 128, 0));
        } else {
            view.setBackgroundColor(Color.GREEN);
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
        void onCloseView();
    }
}
