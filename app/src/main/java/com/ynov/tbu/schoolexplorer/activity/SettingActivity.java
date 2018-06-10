package com.ynov.tbu.schoolexplorer.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.SchoolExplorerApplication;
import com.ynov.tbu.schoolexplorer.fragment.ActionBarFragment;

public class SettingActivity extends AppCompatActivity implements ActionBarFragment.OnFragmentInteractionListener{

    private CheckBox checkBoxPrivate;
    private CheckBox checkBoxPubic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        checkBoxPrivate = findViewById(R.id.checkBox_private);
        checkBoxPubic = findViewById(R.id.checkBox_public);

        //On change le logo en grisé quand on disabled + on force qu'une des deux checkbox soit checkée
        checkBoxPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b && !checkBoxPubic.isChecked()) {
                    checkBoxPubic.setChecked(true);
                }
                setImageChecbox(R.drawable.private_icon, compoundButton, b);
                SchoolExplorerApplication.getInstance().setSchoolPrivateEnabled(b);
            }
        });
        checkBoxPubic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b && !checkBoxPrivate.isChecked()) {
                    checkBoxPrivate.setChecked(true);
                }
                setImageChecbox(R.drawable.public_icon, compoundButton, b);
                SchoolExplorerApplication.getInstance().setSchoolPublicEnabled(b);
            }
        });

        //On récupère les anciennes valeurs
        // On donne les anciennes valeurs
        boolean privateEnabled = SchoolExplorerApplication.getInstance().isSchoolPrivateEnabled();
        boolean publicEnabled = SchoolExplorerApplication.getInstance().isSchoolPublicEnabled();
        checkBoxPrivate.setChecked(privateEnabled);
        checkBoxPubic.setChecked(publicEnabled);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ActionBarFragment.newInstance("Configuration", Color.YELLOW))
                .commit()
        ;
    }

    private void setImageChecbox(Integer imageRes, CompoundButton compoundButton, boolean b) {
        Drawable image = ContextCompat.getDrawable(SettingActivity.this, imageRes);
        if (b) {
            image.setColorFilter(getEnableddFilter());
        } else {
            image.setColorFilter(getDisabledFilter());

        }
        compoundButton.setBackgroundDrawable(image);
    }

    private PorterDuffColorFilter getDisabledFilter(){
        return new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }
    private PorterDuffColorFilter getEnableddFilter(){
        return new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onMenuShow() {

    }
}
