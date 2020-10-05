package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// TerminefragmentSuche
//
//
//
// autor:
// inhalt:  Ermöglicht die Suche nach Wahlmodulen und zur darstelllung an den Recycleview adapter übergeben
// zugriffsdatum: 01.09.20
//
//
//
//
//
//
//////////////////////////////

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Studiengang;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;


public class AddCourseFragment extends Fragment {
    private RecyclerView recyclerView;
    CheckListAdapter mAdapter;
    final List<Boolean> studiengangGewaehlt = new ArrayList<Boolean>();
    final List<String> studiengangName = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.choose_courses, container, false);

        //Komponenten  initialisieren für die Verwendung
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewChecklist);
        recyclerView.setHasFixedSize(true);
        //linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Erhalte die gewählte Fakultät aus den Shared Preferences
        SharedPreferences sharedPrefFakultaetValidation =
                getContext().
                        getSharedPreferences("validation",0);

        String faculty = sharedPrefFakultaetValidation.getString("rueckgabeFakultaet", "0");
        AppDatabase datenbank = AppDatabase.getAppDatabase(getContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Fülle die Recylcerview
                List<Studiengang> studienganenge =
                        datenbank.userDao().getStudiengaenge(faculty);

                for(Studiengang studie: studienganenge) {
                    studiengangName.add(studie.getStudiengangName());
                    studiengangGewaehlt.add(studie.getGewaehlt());
                }

                mAdapter = new CheckListAdapter(studiengangName,
                        studiengangGewaehlt);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

        v.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Aktualisiere die Studiengänge
                        for(int i = 0; i < studiengangGewaehlt.size(); i++) {
                            datenbank.userDao().updateStudiengang(studiengangName.get(i),
                                    studiengangGewaehlt.get(i));
                        }

                        // die Retrofitdaten aus den Shared Preferences
                        SharedPreferences mSharedPreferencesPPServerAdress
                                = AddCourseFragment.this.getContext().getSharedPreferences("Server_Address", MODE_PRIVATE);

                        String relativePPlanURL
                                = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0");

                        String serverAddress
                                = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0");

                        SharedPreferences mSharedPreferencesPruefTermin
                                = AddCourseFragment.this.getContext()
                                .getSharedPreferences("PruefTermin", 0);

                        String aktuellerTermin
                                = mSharedPreferencesPruefTermin.getString("aktPruefTermin", "0");


                        SharedPreferences mSharedPreferencesValidation
                                = AddCourseFragment.
                                this.getContext().getSharedPreferences("validation", 0);

                        String pruefJahr = mSharedPreferencesValidation.getString("pruefJahr", "0");
                        String aktuellePruefphase = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");

                        SharedPreferences sharedPrefSelectedStudiengang = getContext().
                                getSharedPreferences("validation", Context.MODE_PRIVATE);
                        String selectedStudiengang  = sharedPrefSelectedStudiengang.
                                getString("selectedStudiengang","0");

                        // aktualsiere die db Einträge
                        // lösche nicht die Einträge des Hauptstudienganges
                        datenbank.userDao().deletePruefplanEintragExceptMainCourse(selectedStudiengang, false);
                        RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);
                        retrofit.RetrofitWebAccess(
                                getContext(),
                                datenbank,
                                pruefJahr,
                                aktuellePruefphase,
                                aktuellerTermin,
                                serverAddress);

                        retrofit.setUserCourses(getContext(), datenbank,
                                serverAddress);

                        final StartClass globalVariable = (StartClass) v.getContext().getApplicationContext();
                        globalVariable.setShowNoProgressBar(false);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Feedback nach Update
                                Toast.makeText(v.getContext(),
                                        v.getContext().getString(R.string.courseActualisation),
                                        Toast.LENGTH_SHORT).show();

                                Intent hauptfenster = new Intent(v.getContext(), Tabelle.class);
                                startActivity(hauptfenster);
                            }
                        });
                    }
                }).start();
            }
        });

        return v;
    }
}
