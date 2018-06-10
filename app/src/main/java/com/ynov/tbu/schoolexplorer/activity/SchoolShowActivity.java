package com.ynov.tbu.schoolexplorer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.controller.ApiRequest;
import com.ynov.tbu.schoolexplorer.controller.SchoolController;
import com.ynov.tbu.schoolexplorer.fragment.ActionBarFragment;
import com.ynov.tbu.schoolexplorer.fragment.MenuListFragment;
import com.ynov.tbu.schoolexplorer.model.School;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolShowActivity extends AppCompatActivity implements  ActionBarFragment.OnFragmentInteractionListener, MenuListFragment.OnFragmentInteractionListener {

    private EditText editName;
    private EditText editAddress;
    private EditText editZipCode;
    private EditText editCity;
    private EditText editOpeningHours;
    private EditText editPhone;
    private EditText editEmail;
    private EditText editLatitude;
    private EditText editLongitude;
    private EditText editNbStudents;
    private Spinner spinnerType;


    private Button btnSend;
    private Button btnCancel;

    private School schoolEdit = null;
    private boolean enabledForm = false;
    private MenuListFragment menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_show);

        editName = findViewById(R.id.editText_name);
        editAddress = findViewById(R.id.editText_address);
        editZipCode = findViewById(R.id.editText_zip_code);
        editCity = findViewById(R.id.editText_city);
        editOpeningHours = findViewById(R.id.editText_opening_hours);
        editPhone = findViewById(R.id.editText_phone);
        editEmail = findViewById(R.id.editText_email);
        editLatitude = findViewById(R.id.editText_lat);
        editLongitude = findViewById(R.id.editText_longitude);
        editNbStudents = findViewById(R.id.editText_nb_students);
        spinnerType = findViewById(R.id.spinner_type);
        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);

        String title = "Création";
        School school = (School)this.getIntent().getSerializableExtra("school");

        if (school != null) {
            title = "Détail " + school.getName();
            setEditMode(school);

            setEnabledForm(false);
            fillFormWithSchool(school);
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkValidForm()) {
                    return;
                }
                if (isEditMode()) {
                    updateSchool();
                } else {
                    postNewSchool();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillFormWithSchool(schoolEdit);
                setTitleDetail();
                setEnabledForm(false);
            }
        });

        Integer color = Color.parseColor("#8E24AA");

        if(isEditMode()) {
            color =  ContextCompat.getColor(getBaseContext(), R.color.colorOrange);
        }

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ActionBarFragment.newInstance(title, color, R.drawable.menu_icon))
                .commit()
        ;
    }

    private void setEnabledForm(Boolean enabled) {
        enabledForm = enabled;

        editName.setEnabled(enabledForm);
        editAddress.setEnabled(enabledForm);
        editZipCode.setEnabled(enabledForm);
        editCity.setEnabled(enabledForm);
        editOpeningHours.setEnabled(enabledForm);
        editPhone.setEnabled(enabledForm);
        editEmail.setEnabled(enabledForm);
        editLatitude.setEnabled(enabledForm);
        editLongitude.setEnabled(enabledForm);
        editNbStudents.setEnabled(enabledForm);
        spinnerType.setEnabled(enabledForm);

        btnSend.setEnabled(enabledForm);
        btnCancel.setEnabled(enabledForm);
    }

    private void fillFormWithSchool(School school) {
        if (schoolEdit != null) {
            editName.setText(school.getName());
            editAddress.setText(school.getAddress());
            editZipCode.setText(school.getZipCode());
            editCity.setText(school.getCity());
            editOpeningHours.setText(school.getOpeningHours());
            editPhone.setText(school.getPhone());
            editEmail.setText(school.getEmail());
            editLatitude.setText(String.format(Locale.US, "%.6f", school.getLatitude()));
            editLongitude.setText(String.format(Locale.US, "%.6f", school.getLongitude()));
            editNbStudents.setText(String.format(Locale.US, "%d", school.getNumberStudent()));

            if (school.getStatus().equals("private")) {
                spinnerType.setSelection(0);
            } else {
                spinnerType.setSelection(1);
            }
        }
    }

    private void setEditMode(School school) {
        schoolEdit = school;
    }

    private boolean isEditMode() {
        return schoolEdit != null;
    }

    private School getSchoolFromForm() {
        School school = new School();
        school.setName(editName.getText().toString());
        school.setAddress(editAddress.getText().toString());
        school.setCity(editCity.getText().toString());
        school.setEmail(editEmail.getText().toString());
        school.setLatitude(Double.valueOf(editLatitude.getText().toString()));
        school.setLongitude(Double.valueOf(editLongitude.getText().toString()));
        school.setNumberStudent(Integer.valueOf(editNbStudents.getText().toString()));
        school.setOpeningHours(editOpeningHours.getText().toString());
        school.setPhone(editPhone.getText().toString());
        school.setZipCode(editZipCode.getText().toString());

        if (spinnerType.getSelectedItemPosition() == 0) {
            school.setStatus("private");
        } else {
            school.setStatus("public");
        }

        return school;
    }

    private void updateSchool() {
        setEnabledForm(false);
        SchoolController schoolController = ApiRequest.getController(SchoolController.class);
        schoolController.patchSchool(ApiRequest.getInstance().getToken(), schoolEdit.getId(), getSchoolFromForm()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SchoolShowActivity.this.finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR_WS", "Impossible de patch l'école !", t);
                Toast.makeText(SchoolShowActivity.this.getBaseContext(), "Impossible de modifier l'école, merci de ré-essayer", Toast.LENGTH_LONG).show();
                setEnabledForm(true);
            }
        });
    }

    private void postNewSchool() {
        setEnabledForm(false);
        SchoolController schoolController = ApiRequest.getController(SchoolController.class);
        schoolController.newSchool(ApiRequest.getInstance().getToken(), getSchoolFromForm()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Intent myIntent = new Intent(SchoolShowActivity.this.getBaseContext(), ListSchoolsActivity.class);
                SchoolShowActivity.this.startActivity(myIntent);
                SchoolShowActivity.this.finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR_WS", "Impossible de créer l'école !", t);
                Toast.makeText(SchoolShowActivity.this.getBaseContext(), "Impossible de créer l'école, merci de ré-essayer", Toast.LENGTH_LONG).show();
                setEnabledForm(true);
            }
        });
    }

    @Override
    public void onMenuShow() {
        if (menu != null) {
            closeFragmentMenu();
        } else {
            openFragmentMenu();
        }
    }

    private void closeFragmentMenu() {
        getFragmentManager()
                .beginTransaction()
                .remove(menu)
                .commit()
        ;
        menu = null;
    }

    private void openFragmentMenu() {
        menu = MenuListFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_menu_container, menu)
                .commit()
        ;
    }

    private boolean checkValidForm() {
        boolean valid = true;
        if(TextUtils.isEmpty(editName.getText())) {
            valid = false;
            editName.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editAddress.getText())) {
            valid = false;
            editAddress.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editZipCode.getText())) {
            valid = false;
            editZipCode.setError(getText(R.string.field_required));
        } else if (!TextUtils.isDigitsOnly(editZipCode.getText())) {
            valid = false;
            editZipCode.setError(getText(R.string.field_invalid));
        }

        if(TextUtils.isEmpty(editCity.getText())) {
            valid = false;
            editCity.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editOpeningHours.getText())) {
            valid = false;
            editOpeningHours.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editPhone.getText())) {
            valid = false;
            editPhone.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editEmail.getText())) {
            valid = false;
            editEmail.setError(getText(R.string.field_required));
        } else if(!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()).matches()) {
            valid = false;
            editEmail.setError(getText(R.string.field_invalid));
        }

        if(TextUtils.isEmpty(editLatitude.getText())) {
            valid = false;
            editLatitude.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editLongitude.getText())) {
            valid = false;
            editLongitude.setError(getText(R.string.field_required));
        }

        if(TextUtils.isEmpty(editNbStudents.getText())) {
            valid = false;
            editNbStudents.setError(getText(R.string.field_required));
        } else if (!TextUtils.isDigitsOnly(editNbStudents.getText())) {
            valid = false;
            editNbStudents.setError(getText(R.string.field_invalid));
        }

        return valid;
    }

    private void setTitleEdition() {
        String title = "Edition " + schoolEdit.getName();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ActionBarFragment.newInstance(title, ContextCompat.getColor(getBaseContext(), R.color.colorOrange), R.drawable.menu_icon))
                .commit()
        ;
    }

    private void setTitleDetail() {
        String title = "Détail " + schoolEdit.getName();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, ActionBarFragment.newInstance(title, ContextCompat.getColor(getBaseContext(), R.color.colorOrange), R.drawable.menu_icon))
                .commit()
        ;
    }

    @Override
    public void onClickEditMode() {
        setEnabledForm(true);
        setTitleEdition();
        this.closeFragmentMenu();
    }
}