package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// sucheFragment
//
// autor:
// inhalt:  Auswahl und Suche von Professoren, Modulen, Semestern, Prüfungsphase
// zugriffsdatum: 11.12.19
//
//
//////////////////////////////

import android.app.Activity;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;
import static com.Fachhochschulebib.fhb.pruefungsplaner.Terminefragment.validation;

//TODO: Shared prefs???

public class sucheFragment extends Fragment {
    final List<String> sgModulList = new ArrayList();
    final List<String> profList = new ArrayList();
    final List<Integer> rueckgabeProfList = new ArrayList();
    final List<Integer> rueckgabeSgModuleList = new ArrayList();
    final List<Integer> rueckgabeDatumsList = new ArrayList();
    final List<Integer> rueckgabeSemModulList = new ArrayList();
    final List<String> sortedList = new ArrayList();
    private String profName = "Alle";
    private com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase database = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(getContext());

    com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase roomDaten = com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase.getAppDatabase(getContext());
    List<com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag> ppeList = roomDaten.userDao().getAll(validation);

    // Start Merlin Gürtler
    public void registerButton(Button btn, int value) {
        btn.setOnClickListener(new Button.OnClickListener() {
            boolean clicked = true;

            @Override
            public void onClick(View v) {
                if (clicked) {
                    if (rueckgabeSemModulList.size() <= 0) {
                        for (int i = 0; i < (ppeList.size()); i++) {
                            rueckgabeSemModulList.add(99999);
                        }
                    }

                    for (int i = 0; i < ppeList.size(); i++) {
                        if (String.valueOf(value).equals(ppeList.get(i).getSemester())) {
                            btn.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemModulList.set(i, i);

                        }
                    }
                    clicked = false;
                } else {

                    btn.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < ppeList.size(); i++) {
                        if (String.valueOf(value).equals(ppeList.get(i).getSemester())) {
                            rueckgabeSemModulList.set(i, 99999);
                        }
                    }

                    clicked = true;
                }
            }
        });
    }

    // Ende Merlin Gürtler

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_suche, container, false);
        //setContentView(R.layout.hauptfenster);

        //Initialiseren der UI Komponente
        //Spinners spinner = new Spinners();
        final Button btnSemester1 = (Button) v.findViewById(R.id.btns1);
        final Button btnSemester2 = (Button) v.findViewById(R.id.btns2);
        final Button btnSemester3 = (Button) v.findViewById(R.id.btns3);
        final Button btnSemester4 = (Button) v.findViewById(R.id.btns4);
        final Button btnSemester5 = (Button) v.findViewById(R.id.btns5);
        final Button btnSemester6 = (Button) v.findViewById(R.id.btns6);

        //Überprüfung, ob ein Semester-Button geklickt wurde
        //der Wert des Semsters wird gespeichert
        rueckgabeSemModulList.clear();

        // Start Merlin Gürtler
        registerButton(btnSemester1,1);
        registerButton(btnSemester2,2);
        registerButton(btnSemester3,3);
        registerButton(btnSemester4,4);
        registerButton(btnSemester5,5);
        registerButton(btnSemester6,6);
        // Ende Merlin Gürtler

        try {
            //Spinner-Aufruf und Spinner mit Werten füllen
            List<String> spinnerModuleArrayList = new ArrayList<String>();
            List<String> spinnerProfArrayList = new ArrayList<String>();

            //Hinzufügen der Module zum Spinner-Item
            for (int i = 0; i < ppeList.size(); i++) {
                spinnerModuleArrayList.add(ppeList.get(i).getModul());
            }

            //Auswahlmöglichkeit "Klicken um Modul zu wählen" hinzufügen
            spinnerModuleArrayList.add(0, "Klicken um Modul zu wählen");

            //Spinner-Array Prüfer mit Werten füllen
            for (int i = 0; i < ppeList.size(); i++) {
                spinnerProfArrayList.add(ppeList.get(i).getErstpruefer());
            }

            //Doppelte Namenseinträge für Prüfer löschen
            for (int i = 0; i < spinnerProfArrayList.size(); i++) {
                for (int a = i; a < spinnerProfArrayList.size(); a++) {
                    if (spinnerProfArrayList.get(i).equals(spinnerProfArrayList.get(a))) {
                        spinnerProfArrayList.remove(a);
                    }
                }
            }

            //Adapter-Aufruf (LG: Sind hier alle drei Adapter notwendig?)
            // Auswahl Module
            ArrayAdapter<String> adapterModule = new ArrayAdapter<String>(
                    v.getContext(), R.layout.simple_spinner_item, spinnerModuleArrayList);


            // Für das AutoComplete
            ArrayAdapter<String> adapterProfAutoComplete = new ArrayAdapter<String>
                    (v.getContext(), android.R.layout.simple_list_item_1, spinnerProfArrayList);

            //Grafische Ausgabe dropdown
            adapterModule.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // adapterProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Grafische Ausgabe
            //DONE (08/2020) LG unused Code?

            Spinner spSGModule = (Spinner) v.findViewById(R.id.spStudiengang);
            spSGModule.setAdapter(adapterModule);
            //Spinner spProf = (Spinner) v.findViewById(R.id.spProf);
            //spProf.setAdapter(adapterProf);

            final AutoCompleteTextView acProf
                    = (AutoCompleteTextView) v.findViewById(R.id.acProfessor);
            acProf.setThreshold(1);//will start working from first character
            acProf.setAdapter(adapterProfAutoComplete);//setting the adapter data
            // into the AutoCompleteTextView
            profList.add("Alle");
            sgModulList.add("Alle");

            //Initialisierung der Anfangswerte
            int i;
            for (i = 0; i < ppeList.size(); i++) {
                rueckgabeProfList.add(i);
                rueckgabeSgModuleList.add(i);
                rueckgabeDatumsList.add(i);
            }

            // Start Merlin Gürtler
            // The TextChanged Listener is listening
            // on the input events of the AutoCompleteTextView acProf
            // this is necessary because the rueckgabe before
            // only changed when the field was clicked
            acProf.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                // After the text changed the name is saved in
                // after the input the app iterate
                // through the intern Database and select
                // the dozent
                @Override
                public void afterTextChanged(Editable s) {
                    int a;
                    rueckgabeProfList.clear();
                    profName = acProf.getText().toString();
                    for (a = 0; a < ppeList.size(); a++) {
                        if (acProf.getText().toString().equals("Alle")) {
                            rueckgabeProfList.add(a);
                        } else if (Pattern.matches("^.*("                       // Wildcard begin
                                        + acProf.getText().toString().trim().toLowerCase()    // Input Name
                                        +").*$",                                              // Wildcard end
                            ppeList.get(a).getErstpruefer().toLowerCase())) // Name in db
                        {
                            rueckgabeProfList.add(a);
                        }
                    }
                }


            });

            // Ende Merlin Gürtler

            acProf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Start Merlin Gürtler
                    // Close the Keyboard when an Item/Dozent is chosen for better UX
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getActivity().getSystemService(
                                    Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(), 0);
                    // This part is unecessary the Text changed Listener is the better
                    // solution because now the user can write an professor on his own,
                    // so he must not choose the Item
                    // Ende Merlin Gürtler
                    /*
                    int a;
                    rueckgabeProfList.clear();
                    for (a = 0; a < ppeList.size(); a++) {
                        if (acProf.getText().toString().equals("Alle")) {
                            rueckgabeProfList.add(a);

                        } else if (acProf.getText()
                                         .toString()
                                         .equals(ppeList.get(a).getErstpruefer())) {
                            rueckgabeProfList.add(a);
                            //TextView textt = (TextView) v.findViewById(R.id.txtmessage);
                        }
                    }
                    //txtview.setText(prof.get(prof.size()-1).toString()
                    // + pruefplandaten.profname[i].toString());
                    */
                }

                //... your stuf
            });

            spSGModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    rueckgabeSgModuleList.clear();
                    //this is your selected item
                    sgModulList.add(parent.getItemAtPosition(position).toString());

                    int i;
                    String a;
                    for (i = 0; i < (ppeList.size()); i++) {
                        if (sgModulList.get(sgModulList.size() - 1).toString()
                                .equals("Klicken um Modul zu wählen")) {
                            rueckgabeSgModuleList.add(i);
                        } else {
                            if (sgModulList.get(sgModulList.size() - 1).toString()
                                    .equals(ppeList.get(i).getModul().toString())) {
                                rueckgabeSgModuleList.add(i);
                                // database.userDao().Checkverbindung(Tabellenrueckgabe());
                            }
                        }
                        //txtview.setText(prof.get(prof.size()-1).toString()
                        // + pruefplandaten.profname[i].toString());
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button btnOk = (Button) v.findViewById(R.id.BtnOk);
            //final TextView textt = (TextView) v.findViewById(R.id.txtmessage);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start Merlin Gürtler
                    if(profName.equals("Alle")
                            &&  !sgModulList.get(sgModulList.size() - 1).toString().equals("Klicken um Modul zu wählen"))
                    {
                        sortedList.clear();
                        ppeList = roomDaten.userDao().getModule(sgModulList .get(sgModulList .size() - 1));
                        for(int m = 0; m < ppeList.size(); m++) {
                            sortedList.add(String.valueOf(ppeList.get(m).getID()));
                        }

                        database.userDao().sucheUndZurueckSetzen(false);
                        for (int i =0; i< sortedList.size();i++) {
                            // Toast.makeText(getContext(),Tabellenrueckgabe().get(i), Toast.LENGTH_SHORT).show();
                            database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                        }
                    } else if(!profName.equals("Alle")) {
                        sortedList .clear();
                        ppeList = roomDaten.userDao().getModuleProf("%" +
                                acProf.getText().toString().trim() + "%");
                        for(int m = 0; m < ppeList.size(); m++) {
                            sortedList.add(String.valueOf(ppeList.get(m).getID()));
                        }

                        database.userDao().sucheUndZurueckSetzen(false);
                        for (int i =0; i< sortedList.size();i++) {
                            // Toast.makeText(getContext(),Tabellenrueckgabe().get(i), Toast.LENGTH_SHORT).show();
                            database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                        }
                    } else {
                        // Ende Merlin Gürtler
                        if (acProf.getText().toString().equals("Alle")) {
                            int a;
                            rueckgabeProfList.clear();
                            for (a = 0; a < (ppeList.size()); a++) {
                                rueckgabeProfList.add(a);
                            }
                        }

                        database.userDao().sucheUndZurueckSetzen(false);
                        List<PruefplanEintrag> ppeList = AppDatabase.getAppDatabase(v.getContext())
                                .userDao().getAll(validation);
                        for (int i = 0; i < Tabellenrueckgabe().size(); i++) {
                            // Toast.makeText(getContext(),Tabellenrueckgabe().get(i),
                            // Toast.LENGTH_SHORT).show();
                            database.userDao().update2(true,
                                    Integer.valueOf(ppeList.get(
                                            Integer.valueOf(Tabellenrueckgabe().get(i))).getID()));
                        }
                    }

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_placeholder, new TerminefragmentSuche());
                    ft.commit();

                }
            });

        }catch (Exception e)
        {
            Log.d("Fehler sucheFragment","Fehler beim Ermitteln der Module");
        }

        return v;
    }

    public List<String> Tabellenrueckgabe () {
        int i, j, k, l;
        String test = "a";
        boolean checkSemester = true;
        for(int z = 0; z< rueckgabeSemModulList.size(); z++)
        {
            //überprüfung, ob Semester ausgewählt wurden. Sonst alle Semester anzeigen.
            if(!rueckgabeSemModulList.get(z).equals(rueckgabeSemModulList.get(z+1)))
            {
                // DONE (08/2020) LG: Vereinfachung if(!...)
                // Gäbler: Nicht alle Semester anzeigen, weil ein oder
                // mehrere Semester ausgewählt wurden
                checkSemester = false;
                break;
            }
        }

        if (checkSemester)
        {
            for (int z = 0; z < ppeList.size(); z++) {
                rueckgabeSemModulList.add(z);
            }
        }

        sortedList.clear();
        for (i = 0; i < (rueckgabeSgModuleList.size()); i++) {
            for (j = 0; j < (rueckgabeSemModulList.size()); j++) {
                if (rueckgabeSgModuleList.get(i).equals(rueckgabeSemModulList.get(j))) {
                    for (k = 0; k < (rueckgabeDatumsList.size()); k++) {
                        if (rueckgabeDatumsList
                                .get(k)
                                .equals(rueckgabeSgModuleList.get(i))) {
                            for (l = 0; l < (rueckgabeProfList.size()); l++) {
                                if (rueckgabeProfList
                                        .get(l)
                                        .equals(rueckgabeSgModuleList.get(i))) {
                                    sortedList.add(
                                            String.valueOf(rueckgabeSgModuleList.get(i)));
                                    test = String.valueOf(rueckgabeDatumsList.get(k)) + test;
                                }//if
                            }//for
                        }//of
                    }//for
                    //rueckgabeStudiengang.add(i);
                }//if
            }//for
            //txtview.setText(prof.get(prof.size()-1).toString() +
            // pruefplandaten.profname[i].toString());
        }//for
        return (sortedList);
    }
}