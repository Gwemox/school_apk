package com.ynov.tbu.schoolexplorer.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.controller.ApiRequest;
import com.ynov.tbu.schoolexplorer.controller.SchoolController;
import com.ynov.tbu.schoolexplorer.holder.ListSchoolsHolder;
import com.ynov.tbu.schoolexplorer.model.School;
import com.ynov.tbu.schoolexplorer.response.SchoolDeleteResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by titic on 05/06/2018.
 */

public class ListSchoolAdapter  extends RecyclerView.Adapter<ListSchoolsHolder> {
    private List<School> list;

    //ajouter un constructeur prenant en entrée une liste
    public ListSchoolAdapter(List<School> list) {
        this.list = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public ListSchoolsHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_school, viewGroup,false);

        return new ListSchoolsHolder(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque objet Entree
    @Override
    public void onBindViewHolder(final ListSchoolsHolder myViewHolder, int position) {
        final School myObject = list.get(position);
        final Integer m_position = myViewHolder.getAdapterPosition();

        myViewHolder.bind(myObject);

        myViewHolder.getButtonDelete().setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteSchool(myObject, m_position);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(myViewHolder.itemView.getContext());
                builder.setMessage("Etes vous sûre de vouloir supprimer l’école " + myObject.getName() + " ?").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();


                // TODO : Call API pour remove
            }
        });
    }

    private void deleteSchool(final School school, final int m_position) {

        SchoolController schoolController = ApiRequest.getController(SchoolController.class);
        schoolController.deleteSchool(ApiRequest.getInstance().getToken(), school.getId()).enqueue(new Callback<SchoolDeleteResponse>() {
            @Override
            public void onResponse(Call<SchoolDeleteResponse> call, Response<SchoolDeleteResponse> response) {
                list.remove(school);
                ListSchoolAdapter.this.notifyItemRemoved(m_position);
            }

            @Override
            public void onFailure(Call<SchoolDeleteResponse> call, Throwable t) {
                Log.e("ERROR_WS", "Impossible de supprimer l'école !", t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
