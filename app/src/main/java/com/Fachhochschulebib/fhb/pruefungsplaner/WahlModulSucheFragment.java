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

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

import java.util.List;

import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;

public class WahlModulSucheFragment extends Fragment {

    private com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase database = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(getContext());
    com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase roomDaten = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(getContext());
    List<PruefplanEintrag> ppeList = roomDaten.userDao().getAll2();

    private String selectedstudiengang = "Alle Studiengänge";
    private String modulName = "Alle";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.wahlmodul, container, false);

        final Button searchBtn = v.findViewById(R.id.BtnOk);
        final EditText editWahlModul = v.findViewById(R.id.wahlModulName);
        final Spinner spStudiengang = v.findViewById(R.id.spStudiengang);

        // Studiengang auswahl
        List<String> StudiengangArrayList = database.userDao().getStudiengangDistinct();
        StudiengangArrayList.add(0,"Alle Studiengänge");

        // Design für den Spinner
        ArrayAdapter<String> adapterStudiengang = new ArrayAdapter<String>(
                v.getContext(), R.layout.simple_spinner_item, StudiengangArrayList);
        adapterStudiengang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStudiengang.setAdapter(adapterStudiengang);

        spStudiengang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Setze den ausgewählten Studiengang
                selectedstudiengang = parent.getItemAtPosition(position).toString();
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
                        || !selectedstudiengang.equals("Alle Studiengänge")) {

                    // Nur ein Modulnamen eingetragen
                    if (selectedstudiengang.equals("Alle Studiengänge")
                            && !modulName.equals("Alle")) {

                        ppeList = database.userDao().getModule("%" + modulName.trim() + "%");

                    // Alles eingegeben
                    } else if (!selectedstudiengang.equals("Alle Studiengänge")
                            && !modulName.equals("Alle")) {

                        ppeList = database.userDao().getModuleWithCourseAndModule("%" +
                                modulName.trim() + "%", selectedstudiengang);

                    // Nur ein Studiengang ausgewählt
                    } else if (!selectedstudiengang.equals("Alle Studiengänge")
                            && modulName.equals("Alle")) {

                        ppeList = database.userDao().getModuleWithCourse(selectedstudiengang);

                    }


                    // Setze die gewählten Daten in der DB
                    database.userDao().sucheUndZurueckSetzen(false);
                    for (int i = 0; i < ppeList.size(); i++) {
                        database.userDao().update2(true,
                                Integer.valueOf(ppeList.get(i).getID()));
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
