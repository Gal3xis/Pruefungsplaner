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
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Courses;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class AddCourseFragment extends Fragment {
    private RecyclerView recyclerView;
    CheckListAdapter mAdapter;
    List<Boolean> courseChosen = new ArrayList<>();
    List<String> courseName = new ArrayList<String>();
    AppDatabase database;

    public void onCreate(Bundle savedInstanceState) {
        database = AppDatabase.getAppDatabase(getContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Erhalte die gewählte Fakultät aus den Shared Preferences
                SharedPreferences sharedPreferencesFacultyEditor =
                        getContext().
                                getSharedPreferences("validation",0);

                String faculty = sharedPreferencesFacultyEditor.getString("returnFaculty", "0");

                // Fülle die Recylcerview
                List<Courses> courses =
                        database.userDao().getCourses(faculty);

                for(Courses cours: courses) {
                    courseName.add(cours.getCourseName());
                    courseChosen.add(cours.getChoosen());
                }

                mAdapter = new CheckListAdapter(courseName,
                        courseChosen,
                        getActivity().getApplicationContext());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

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

        v.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Aktualisiere die Studiengänge
                        for(int i = 0; i < courseChosen.size(); i++) {
                            database.userDao().updateCourse(courseName.get(i),
                                    courseChosen.get(i));
                        }

                        // die Retrofitdaten aus den Shared Preferences
                        SharedPreferences mSharedPreferencesPPServerAdress
                                = AddCourseFragment.this.getContext().getSharedPreferences("Server_Address", MODE_PRIVATE);

                        String relativePPlanURL
                                = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0");

                        String serverAddress
                                = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0");

                        SharedPreferences mSharedPreferencesCurrentTermin
                                = AddCourseFragment.this.getContext()
                                .getSharedPreferences("PruefTermin", 0);

                        String currentDate
                                = mSharedPreferencesCurrentTermin.getString("aktPruefTermin", "0");


                        SharedPreferences mSharedPreferencesValidation
                                = AddCourseFragment.
                                this.getContext().getSharedPreferences("validation", 0);

                        String examineYear = mSharedPreferencesValidation.getString("examineYear", "0");
                        String currentExamine = mSharedPreferencesValidation.getString("currentPeriode", "0");

                        List <Courses> courses = database.userDao().getCourses();

                        // aktualsiere die db Einträge

                        JSONArray studiengangIds = new JSONArray();

                        String courseName;

                        for(Courses studiengang: courses) {
                            try {
                                courseName = studiengang.getCourseName();
                                if(!studiengang.getChoosen()) {
                                    // lösche nicht die Einträge der gewählten Studiengänge und favorit
                                    database.userDao().deletePruefplanEintragExceptChoosen(courseName, false);
                                }
                                if(database.userDao().getOneEntryByName(courseName) == null && studiengang.getChoosen()) {
                                    JSONObject idJson = new JSONObject();
                                    idJson.put("ID", studiengang.getSgid());
                                    studiengangIds.put(idJson);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);
                        // > 2 da auch bei einem leeren Json Array [] gesetzt werden
                        if(studiengangIds.toString().length() > 2) {
                            retrofit.UpdateUnkownCourses(
                                    getContext(),
                                    database,
                                    examineYear,
                                    currentExamine,
                                    currentDate,
                                    serverAddress,
                                    studiengangIds.toString());
                        }

                        retrofit.setUserCourses(getContext(), database,
                                serverAddress);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Feedback nach Update
                                Toast.makeText(v.getContext(),
                                        v.getContext().getString(R.string.courseActualisation),
                                        Toast.LENGTH_SHORT).show();

                                Intent mainWindow = new Intent(v.getContext(), table.class);
                                startActivity(mainWindow);
                            }
                        });
                    }
                }).start();
            }
        });

        return v;
    }
}
