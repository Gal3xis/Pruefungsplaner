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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;

import java.util.ArrayList;
import java.util.List;


public class ChoiceModulSearchFragment extends Fragment {

    private final AppDatabase database = AppDatabase.getAppDatabase(getContext());
    List<TestPlanEntry> ppeList;

    private String selectedCourseSpinner;
    private String modulName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.wahlmodul, container, false);

        final Spinner spCourse = v.findViewById(R.id.spStudiengang);
        final Button searchBtn = v.findViewById(R.id.BtnOk);
        final AutoCompleteTextView editChoiceModul = v.findViewById(R.id.wahlModulName);

        // Studiengang auswahl
        List<String> courseArrayList = new ArrayList<String>();
        courseArrayList.add(0,v.getContext().getString(R.string.all_cours));

        // Design für den Spinner
        // Hier schon setzen für ein besseres UI
        ArrayAdapter<String> adapterCourse = new ArrayAdapter<String>(
                v.getContext(), R.layout.simple_spinner_item, courseArrayList);

        adapterCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapterCourse);

        selectedCourseSpinner = getContext().getString(R.string.all_cours);
        modulName = getContext().getString(R.string.all);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> modulNameArrayList = database.userDao().getModuleOrdered();
                ArrayAdapter<String> adapterModuleAutoComplete = new ArrayAdapter<String>
                        (v.getContext(), android.R.layout.simple_list_item_1, modulNameArrayList);

                courseArrayList.addAll(database.userDao().getChoosenCourse(true));


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        editChoiceModul.setAdapter(adapterModuleAutoComplete);
                        // spStudiengang.setAdapter(adapterStudiengang);
                    }
                });

            }
        }).start();

        editChoiceModul.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Schließe das Keyboard nach auswahl des Modules
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                try {
                    inputMethodManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.d("Exception", "Keyboard not open");
                }
            }

        });

        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Setze den ausgewählten Studiengang
                selectedCourseSpinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        editChoiceModul.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Setze das ausgewählte Modul
                modulName = editChoiceModul.getText().toString();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Um nicht zu viele Module zu laden muss der Name mindestens drei Zeichen lang sein,
                oder ein Studiengang ausgewählt sein
                 */

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if((modulName.trim().length() > 3 && !modulName.equals(getContext().getString(R.string.all)))
                                || !selectedCourseSpinner.equals(getContext().getString(R.string.all_cours))) {

                            // Nur ein Modulnamen eingetragen
                            if (selectedCourseSpinner.equals(getContext().getString(R.string.all_cours))
                                    && !modulName.equals(getContext().getString(R.string.all))) {

                                ppeList = database.userDao().getEntriesByModule("%" + modulName.trim() + "%");

                                // Alles eingegeben
                            } else if (!selectedCourseSpinner.equals(getContext().getString(R.string.all_cours))
                                    && !modulName.equals(getContext().getString(R.string.all))) {

                                ppeList = database.userDao().getEntriesWithCourseAndModule("%" +
                                        modulName.trim() + "%", selectedCourseSpinner);

                                // Nur ein Studiengang ausgewählt
                            } else if (!selectedCourseSpinner.equals(getContext().getString(R.string.all_cours))
                                    && modulName.equals(getContext().getString(R.string.all))) {

                                ppeList = database.userDao().getEntriesWithCourseOrdered(selectedCourseSpinner);

                            }


                            // Setze die gewählten Daten in der DB
                            database.userDao().searchAndReset(false);
                            for (TestPlanEntry entry: ppeList) {
                                database.userDao().update2(true,
                                        Integer.parseInt(entry.getID()));
                            }

                            // Merlin Gürtler
                            // Schließe das Keyboard falls offen
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getActivity().getSystemService(
                                            Activity.INPUT_METHOD_SERVICE);

                            try {
                                inputMethodManager.hideSoftInputFromWindow(
                                        getActivity().getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {
                                Log.d("Exception", "Keyboard not open");
                            }

                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_placeholder, new TermineFragmentSearch());
                            ft.commit();
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(!((modulName.trim().length() > 3 && !modulName.equals(v.getContext().getString(R.string.all)))
                                        || !selectedCourseSpinner.equals(v.getContext().getString(R.string.all_cours)))) {

                                    Toast.makeText(v.getContext(),v.getContext().getString(R.string.elective_search), Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        return v;
    }
}
