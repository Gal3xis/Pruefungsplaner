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

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

import java.util.ArrayList;
import java.util.List;

import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;

public class WahlModulSucheFragment extends Fragment {

    private AppDatabase database = AppDatabase.getAppDatabase(getContext());
    List<PruefplanEintrag> ppeList;

    private String selectedStudiengangSpinner;
    private String modulName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.wahlmodul, container, false);

        final Spinner spStudiengang = v.findViewById(R.id.spStudiengang);
        final Button searchBtn = v.findViewById(R.id.BtnOk);
        final AutoCompleteTextView editWahlModul = v.findViewById(R.id.wahlModulName);

        // Studiengang auswahl
        List<String> studiengangArrayList = new ArrayList<String>();
        studiengangArrayList.add(0,v.getContext().getString(R.string.all_cours));

        // Design für den Spinner
        // Hier schon setzen für ein besseres UI
        ArrayAdapter<String> adapterStudiengang = new ArrayAdapter<String>(
                v.getContext(), R.layout.simple_spinner_item, studiengangArrayList);

        adapterStudiengang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStudiengang.setAdapter(adapterStudiengang);

        selectedStudiengangSpinner = getContext().getString(R.string.all_cours);
        modulName = getContext().getString(R.string.all);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> modulNameArrayList = database.userDao().getModuleOrdered();
                ArrayAdapter<String> adapterModuleAutoComplete = new ArrayAdapter<String>
                        (v.getContext(), android.R.layout.simple_list_item_1, modulNameArrayList);

                studiengangArrayList.addAll(database.userDao().getChoosenStudiengang(true));


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        editWahlModul.setAdapter(adapterModuleAutoComplete);
                        // spStudiengang.setAdapter(adapterStudiengang);
                    }
                });

            }
        }).start();

        editWahlModul.setOnItemClickListener(new AdapterView.OnItemClickListener(){

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

        spStudiengang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Setze den ausgewählten Studiengang
                selectedStudiengangSpinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        editWahlModul.addTextChangedListener(new TextWatcher() {
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
                modulName = editWahlModul.getText().toString();
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
                                || !selectedStudiengangSpinner.equals(getContext().getString(R.string.all_cours))) {

                            // Nur ein Modulnamen eingetragen
                            if (selectedStudiengangSpinner.equals(getContext().getString(R.string.all_cours))
                                    && !modulName.equals(getContext().getString(R.string.all))) {

                                ppeList = database.userDao().getModule("%" + modulName.trim() + "%");

                                // Alles eingegeben
                            } else if (!selectedStudiengangSpinner.equals(getContext().getString(R.string.all_cours))
                                    && !modulName.equals(getContext().getString(R.string.all))) {

                                ppeList = database.userDao().getModuleWithCourseAndModule("%" +
                                        modulName.trim() + "%", selectedStudiengangSpinner);

                                // Nur ein Studiengang ausgewählt
                            } else if (!selectedStudiengangSpinner.equals(getContext().getString(R.string.all_cours))
                                    && modulName.equals(getContext().getString(R.string.all))) {

                                ppeList = database.userDao().getModuleWithCourseOrdered(selectedStudiengangSpinner);

                            }


                            // Setze die gewählten Daten in der DB
                            database.userDao().sucheUndZurueckSetzen(false);
                            for (PruefplanEintrag eintrag: ppeList) {
                                database.userDao().update2(true,
                                        Integer.valueOf(eintrag.getID()));
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

                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_placeholder, new TerminefragmentSuche());
                            ft.commit();
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(!((modulName.trim().length() > 3 && !modulName.equals(v.getContext().getString(R.string.all)))
                                        || !selectedStudiengangSpinner.equals(v.getContext().getString(R.string.all_cours)))) {

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
