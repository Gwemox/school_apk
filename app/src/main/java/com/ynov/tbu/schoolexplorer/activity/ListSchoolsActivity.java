package com.ynov.tbu.schoolexplorer.activity;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.adapter.ListSchoolAdapter;
import com.ynov.tbu.schoolexplorer.controller.ApiRequest;
import com.ynov.tbu.schoolexplorer.controller.SchoolController;
import com.ynov.tbu.schoolexplorer.fragment.ActionBarFragment;
import com.ynov.tbu.schoolexplorer.response.SchoolsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSchoolsActivity extends AppCompatActivity implements ActionBarFragment.OnFragmentInteractionListener {

    private RecyclerView recyclerViewSchools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_schools);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ActionBarFragment.newInstance("Liste des écoles", ContextCompat.getColor(getBaseContext(), R.color.colorOrange)))
                .commit()
        ;

        // On récupère le recycler view
        recyclerViewSchools = this.findViewById(R.id.recyclerViewSchools);

        // On définit l'agencement des cellules, dans notre cas de façon verticale (comme une ListView)
        recyclerViewSchools.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));

    }

    @Override
    public void onMenuShow() {
    }

    private void updateSchools() {
        SchoolController schoolController = ApiRequest.getController(SchoolController.class);
        schoolController.getSchools(ApiRequest.getInstance().getToken(), ApiRequest.getSchoolsStatus()).enqueue(new Callback<SchoolsResponse>() {
            @Override
            public void onResponse(Call<SchoolsResponse> call, Response<SchoolsResponse> response) {
                SchoolsResponse schoolsResponse = response.body();
                if (schoolsResponse != null) {
                    // On créé un ListEntreeAdapter en lui fournissant note liste des entities
                    // Cet adapter sert à remplir notre recyclerview
                    recyclerViewSchools.setAdapter(new ListSchoolAdapter(schoolsResponse.getSchools()));
                } else {
                    Log.e("WS_ERROR", "Erreur lors de la lecture de la réponse du serveur", null);
                }
            }

            @Override
            public void onFailure(Call<SchoolsResponse> call, Throwable t) {
                Log.e("WS_ERROR", "Erreur lors de la lecture de la réponse du serveur", t);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        updateSchools();
    }
}
