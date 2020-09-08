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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.List;

import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;

public class WahlModulSucheFragment extends Fragment {

    private AppDatabase database = AppDatabase.getAppDatabase(getContext());
    AppDatabase roomDaten = AppDatabase.getAppDatabase(getContext());
    List<PruefplanEintrag> ppeList = roomDaten.userDao().getAll2();

    private String selectedStudiengangSpinner = "Alle Studiengänge";
    private String modulName = "Alle";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.wahlmodul, container, false);

        final Button searchBtn = v.findViewById(R.id.BtnOk);
        final AutoCompleteTextView editWahlModul = v.findViewById(R.id.wahlModulName);
        final Spinner spStudiengang = v.findViewById(R.id.spStudiengang);


        // Damit keine Module des gewählten Studienganges angezeigt werden
        SharedPreferences sharedPrefSelectedStudiengang = getContext().
                getSharedPreferences("validation",Context.MODE_PRIVATE);
        String selectedStudiengang  = sharedPrefSelectedStudiengang.
                getString("selectedStudiengang","0");

        List<String> modulNameArrayList = database.userDao().getModuleExceptCourse(selectedStudiengang);
        ArrayAdapter<String> adapterModuleAutoComplete = new ArrayAdapter<String>
                (v.getContext(), android.R.layout.simple_list_item_1, modulNameArrayList);
        editWahlModul.setAdapter(adapterModuleAutoComplete);

        // Studiengang auswahl
        List<String> studiengangArrayList = database.userDao().getStudiengangDistinct(selectedStudiengang);
        studiengangArrayList.add(0,"Alle Studiengänge");

        // Design für den Spinner
        ArrayAdapter<String> adapterStudiengang = new ArrayAdapter<String>(
                v.getContext(), R.layout.simple_spinner_item, studiengangArrayList);
        adapterStudiengang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStudiengang.setAdapter(adapterStudiengang);

        editWahlModul.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Schließe das Keyboard nach auswahl des Modules
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(), 0);
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
                ppeList.clear();

                /*
                Um nicht zu viele Module zu laden muss der Name mindestens drei Zeichen lang sein,
                oder ein Studiengang ausgewählt sein
                 */

                if((modulName.trim().length() > 3 && !modulName.equals("Alle"))
                        || !selectedStudiengangSpinner.equals("Alle Studiengänge")) {

                    // Nur ein Modulnamen eingetragen
                    if (selectedStudiengangSpinner.equals("Alle Studiengänge")
                            && !modulName.equals("Alle")) {

                        ppeList = database.userDao().getModule("%" + modulName.trim() + "%");

                    // Alles eingegeben
                    } else if (!selectedStudiengangSpinner.equals("Alle Studiengänge")
                            && !modulName.equals("Alle")) {

                        ppeList = database.userDao().getModuleWithCourseAndModule("%" +
                                modulName.trim() + "%", selectedStudiengangSpinner);

                    // Nur ein Studiengang ausgewählt
                    } else if (!selectedStudiengangSpinner.equals("Alle Studiengänge")
                            && modulName.equals("Alle")) {

                        ppeList = database.userDao().getModuleWithCourse(selectedStudiengangSpinner);

                    }


                    // Setze die gewählten Daten in der DB
                    database.userDao().sucheUndZurueckSetzen(false);
                    for (PruefplanEintrag eintrag: ppeList) {
                        database.userDao().update2(true,
                                Integer.valueOf(eintrag.getID()));
                    }

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_placeholder, new TerminefragmentSuche());
                    ft.commit();
                } else {
                    Toast.makeText(v.getContext(),"Bitte wählen Sie einen Studiengang aus" +
                            ", oder geben einen Modulnamen ein der länger als drei Zeichen ist.", Toast.LENGTH_LONG)
                            .show();
                }


            }
        });

        return v;
    }
}
