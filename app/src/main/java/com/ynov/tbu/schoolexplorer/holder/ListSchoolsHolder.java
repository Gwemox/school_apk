package com.ynov.tbu.schoolexplorer.holder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.SchoolExplorerApplication;
import com.ynov.tbu.schoolexplorer.activity.ListSchoolsActivity;
import com.ynov.tbu.schoolexplorer.activity.MapsActivity;
import com.ynov.tbu.schoolexplorer.activity.SchoolShowActivity;
import com.ynov.tbu.schoolexplorer.fragment.ActionBarFragment;
import com.ynov.tbu.schoolexplorer.model.School;
import com.ynov.tbu.schoolexplorer.utils.GPSTracker;

/**
 * Created by titic on 05/06/2018.
 */

public class ListSchoolsHolder extends RecyclerView.ViewHolder{
    private TextView textName;
    private TextView textAddress;
    private TextView textCity;
    private TextView textNbStudents;
    private TextView textDistance;
    private ImageView imageSchool;
    private ImageView imageMap;
    private ImageView imageClose;
    private ImageView imageDelete;

    //itemView est la vue correspondante à 1 cellule
    public ListSchoolsHolder(final View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView
        textName = itemView.findViewById(R.id.text_school_name);
        textAddress = itemView.findViewById(R.id.text_school_address);
        textCity = itemView.findViewById(R.id.text_school_city);
        textNbStudents = itemView.findViewById(R.id.text_school_nb_student);
        textDistance = itemView.findViewById(R.id.text_school_distance);
        imageSchool = itemView.findViewById(R.id.imageSchool);

        imageClose = itemView.findViewById(R.id.image_close);
        imageMap = itemView.findViewById(R.id.image_map);
        imageDelete = itemView.findViewById(R.id.image_delete);

        imageClose.setVisibility(View.INVISIBLE);

    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'une école
    public void bind(final School school){
        textName.setText(school.getName());
        textAddress.setText(school.getAddress());
        textCity.setText(SchoolExplorerApplication.getInstance().getApplicationContext().getString(R.string.zip_code_city, school.getZipCode(), school.getCity()));
        textNbStudents.setText(SchoolExplorerApplication.getInstance().getApplicationContext().getString(R.string.students_message, school.getNumberStudent()));

        double currentLat = GPSTracker.getInstance().getLatitude();
        double currentLong = GPSTracker.getInstance().getLongitude();
        double distance = GPSTracker.meterDistanceBetweenPoints(currentLat, currentLong, school.getLatitude(), school.getLongitude()) / 1000; // En km
        textDistance.setText(SchoolExplorerApplication.getInstance().getApplicationContext().getString(R.string.distance_message, distance));

        if (school.getNumberStudent() < 50) {
            itemView.setBackgroundColor(Color.RED);
            imageSchool.setImageResource(R.drawable.ko_icon);
        } else if (school.getNumberStudent() < 200) {
            itemView.setBackgroundColor(Color.rgb(255, 128, 0));
        } else {
            itemView.setBackgroundColor(Color.GREEN);
        }

        imageMap.setVisibility(View.VISIBLE);
        imageDelete.setVisibility(View.VISIBLE);
        imageMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent myIntent = new Intent(itemView.getContext(), MapsActivity.class);
                myIntent.putExtra("focusSchoolId", school.getId());
                itemView.getContext().startActivity(myIntent);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(itemView.getContext(), SchoolShowActivity.class);
                myIntent.putExtra("school", school);
                itemView.getContext().startActivity(myIntent);
            }
        });
    }

    public ImageView getButtonDelete() {
        return imageDelete;
    }
}
