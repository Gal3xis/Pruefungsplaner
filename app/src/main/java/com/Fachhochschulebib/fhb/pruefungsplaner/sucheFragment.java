package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// sucheFragment
//
//
//
// autor:
// inhalt:  auswählen und suchen von professoren,modulen,semestern, prüfungsphase
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Pruefplan;

import java.util.ArrayList;
import java.util.List;


import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;
import static com.Fachhochschulebib.fhb.pruefungsplaner.Terminefragment.validation;


public class sucheFragment extends Fragment {
    final List<String> studiengang = new ArrayList();
    final List<String> prof = new ArrayList();
    final List<Integer> rueckgabe = new ArrayList();
    final List<Integer> rueckgabeStudiengang = new ArrayList();
    final List<Integer> rueckgabeDatum = new ArrayList();
    final List<Integer> rueckgabeSemester = new ArrayList();
    final List<String> fertigsortiert = new ArrayList();
    private AppDatabase database = AppDatabase.getAppDatabase(getContext());

    AppDatabase roomDaten = AppDatabase.getAppDatabase(getContext());
    List<Pruefplan> datenEinlesen = roomDaten.userDao().getAll(validation);


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
        final Button btnSemester1 = (Button) v.findViewById(R.id.btns1);
        final Button btnSemester2 = (Button) v.findViewById(R.id.btns2);
        final Button btnSemester3 = (Button) v.findViewById(R.id.btns3);
        final Button btnSemester4 = (Button) v.findViewById(R.id.btns4);
        final Button btnSemester5 = (Button) v.findViewById(R.id.btns5);
        final Button btnSemester6 = (Button) v.findViewById(R.id.btns6);

        //Überprüfung ob ein Semster-Button geklickt wurde
        //der Wert des Semsters wird gespeichert
        rueckgabeSemester.clear();
        btnSemester1.setOnClickListener(new Button.OnClickListener() {
            boolean geklickt = true;
            @Override
            public void onClick(View v) {
                if (geklickt) {
                    if (rueckgabeSemester.size() <= 0) {
                        for (int i = 0; i < (datenEinlesen.size()); i++) {
                            rueckgabeSemester.add(99999);
                        }
                    }

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(1).equals(datenEinlesen.get(i).getSemester())) {
                            btnSemester1.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemester.set(i, i);

                        }
                    }
                    geklickt = false;
                } else {

                    btnSemester1.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(1).equals(datenEinlesen.get(i).getSemester())) {
                            rueckgabeSemester.set(i, 99999);
                        }
                    }

                    geklickt = true;
                }
            }
        });

        btnSemester2.setOnClickListener(new Button.OnClickListener() {
            boolean geklickt = true;

            @Override
            public void onClick(View v) {
                if (geklickt) {
                    if (rueckgabeSemester.size() <= 0) {
                        for (int i = 0; i < (datenEinlesen.size()); i++) {
                            rueckgabeSemester.add(99999);
                        }
                    }

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(2).equals(datenEinlesen.get(i).getSemester())) {
                            btnSemester2.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemester.set(i, i);

                        }
                    }
                    geklickt = false;
                } else {

                    btnSemester2.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(2).equals(datenEinlesen.get(i).getSemester())) {
                            rueckgabeSemester.set(i, 99999);
                        }
                    }

                    geklickt = true;
                }
            }
        });
        btnSemester3.setOnClickListener(new Button.OnClickListener() {
            boolean geklickt = true;

            @Override
            public void onClick(View v) {
                if (geklickt) {
                    if (rueckgabeSemester.size() <= 0) {
                        for (int i = 0; i < (datenEinlesen.size()); i++) {
                            rueckgabeSemester.add(99999);
                        }
                    }

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(3).equals(datenEinlesen.get(i).getSemester().toString())) {
                            btnSemester3.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemester.set(i, i);

                        }
                    }
                    geklickt = false;
                } else {

                    btnSemester3.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(3).equals(datenEinlesen.get(i).getSemester().toString())) {
                            rueckgabeSemester.set(i, 99999);
                        }
                    }

                    geklickt = true;
                }
            }
        });

        btnSemester4.setOnClickListener(new Button.OnClickListener() {
            boolean geklickt = true;

            @Override
            public void onClick(View v) {
                if (geklickt) {
                    if (rueckgabeSemester.size() <= 0) {
                        for (int i = 0; i < (datenEinlesen.size()); i++) {
                            rueckgabeSemester.add(99999);
                        }
                    }

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(4).equals(datenEinlesen.get(i).getSemester())) {
                            btnSemester4.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemester.set(i, i);

                        }
                    }
                    geklickt = false;
                } else {

                    btnSemester4.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(4).equals(datenEinlesen.get(i).getSemester().toString())) {
                            rueckgabeSemester.set(i, 99999);
                        }
                    }

                    geklickt = true;
                }
            }
        });

        btnSemester5.setOnClickListener(new Button.OnClickListener() {
            boolean geklickt = true;

            @Override
            public void onClick(View v) {
                if (geklickt) {
                    if (rueckgabeSemester.size() <= 0) {
                        for (int i = 0; i < (datenEinlesen.size()); i++) {
                            rueckgabeSemester.add(99999);
                        }
                    }
                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(5).equals(datenEinlesen.get(i).getSemester().toString())) {
                            btnSemester5.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemester.set(i, i);
                        }
                    }
                    geklickt = false;
                } else {
                    btnSemester5.setBackgroundResource(R.drawable.button_rounded_corners2);
                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(5).equals(datenEinlesen.get(i).getSemester().toString())) {
                            rueckgabeSemester.set(i, 99999);
                        }
                    }
                    geklickt = true;
                }
            }
        });

        btnSemester6.setOnClickListener(new Button.OnClickListener() {
            boolean geklickt = true;

            @Override
            public void onClick(View v) {
                if (geklickt) {
                    if (rueckgabeSemester.size() <= 0) {
                        for (int i = 0; i < (datenEinlesen.size()); i++) {
                            rueckgabeSemester.add(99999);
                        }
                    }
                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(6).equals(datenEinlesen.get(i).getSemester())) {
                            btnSemester6.setBackgroundResource(R.drawable.button_rounded_corners);
                            rueckgabeSemester.set(i, i);
                        }
                    }
                    geklickt = false;
                } else {

                    btnSemester6.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < datenEinlesen.size(); i++) {
                        if (String.valueOf(6).equals(datenEinlesen.get(i).getSemester())) {
                            rueckgabeSemester.set(i, 99999);
                        }
                    }
                    geklickt = true;
                }
            }
        });

        try {
            //Spinner aufruf und setzen der Spinner mit werten füllen
            List<String> spinnerArray = new ArrayList<String>();
            List<String> spinnerArrayProf = new ArrayList<String>();
            //hinzufügen der Module zum Spinneritem
            for (int i = 0; i < datenEinlesen.size(); i++) {
                spinnerArray.add(datenEinlesen.get(i).getModul());
            }
            //auswahlmöglichkeit Alle Module hinzufügen
            spinnerArray.add(1, "Alle Module");


            // SpinenArray Prüfer mit Werten füllen
            for (int i = 0; i < datenEinlesen.size(); i++) {
                spinnerArrayProf.add(datenEinlesen.get(i).getErstpruefer());

            }

            //doppelte Nameneinträge löschen für Module und Prüfer
            for (int i = 0; i < spinnerArray.size(); i++) {
                for (int a = i; a < spinnerArray.size(); a++) {

                    if (spinnerArray.get(i).equals(spinnerArray.get(a))) {

                        spinnerArray.remove(a);
                    }
                }
            }
            //doppelte Nameneinträge löschen für Module und Prüfer
            for (int i = 0; i < spinnerArrayProf.size(); i++) {
                for (int a = i; a < spinnerArrayProf.size(); a++) {
                    if (spinnerArrayProf.get(i).equals(spinnerArrayProf.get(a))) {
                        spinnerArrayProf.remove(a);
                    }
                }
            }




            //adapter aufruf
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    v.getContext(), R.layout.simple_spinner_item, spinnerArray);
            ArrayAdapter<String> adapterProf = new ArrayAdapter<String>(
                    v.getContext(), android.R.layout.simple_spinner_item, spinnerArrayProf);

            ArrayAdapter<String> adapteracPof = new ArrayAdapter<String>
                    (v.getContext(), android.R.layout.simple_list_item_1, spinnerArrayProf);



            //grafische ausgabe dropdown
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            adapterProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);




            //grafische ausgabe
            Spinner spStudiengang = (Spinner) v.findViewById(R.id.spStudiengang);
            spStudiengang.setAdapter(adapter);
            Spinner spProf = (Spinner) v.findViewById(R.id.spProf);
            spProf.setAdapter(adapterProf);

            final AutoCompleteTextView acProf = (AutoCompleteTextView) v.findViewById(R.id.acProfessor);
            acProf.setThreshold(1);//will start working from first character
            acProf.setAdapter(adapteracPof);//setting the adapter data into the AutoCompleteTextView

            prof.add("Alle");
            studiengang.add("Alle");
            //initialisierung der anfagswerte
            int i;
            for (i = 0; i < datenEinlesen.size(); i++) {
                rueckgabe.add(i);
                rueckgabeStudiengang.add(i);
                rueckgabeDatum.add(i);

            }
            acProf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int a;
                    rueckgabe.clear();
                    for (a = 0; a < datenEinlesen.size(); a++) {
                        if (acProf.getText().toString().equals("Alle")) {
                            rueckgabe.add(a);

                        } else if (acProf.getText().toString().equals(datenEinlesen.get(a).getErstpruefer())) {
                            rueckgabe.add(a);
                            TextView textt = (TextView) v.findViewById(R.id.txtmessage);

                        }
                    }
                    //txtview.setText(prof.get(prof.size()-1).toString() + pruefplandaten.profname[i].toString());
                }
                //... your stuf
            });




            spStudiengang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    rueckgabeStudiengang.clear();
                    studiengang.add(parent.getItemAtPosition(position).toString()); //this is your selected item
                    int i;
                    String a;
                    for (i = 0; i < (datenEinlesen.size()); i++) {
                        if (studiengang.get(studiengang.size() - 1).toString().equals("Alle Module")) {
                            rueckgabeStudiengang.add(i);
                        } else {
                            if (studiengang.get(studiengang.size() - 1).toString().equals(datenEinlesen.get(i).getModul().toString())) {
                                rueckgabeStudiengang.add(i);
                                // database.userDao().Checkverbindung(Tabellenrueckgabe());

                            }
                        }
                        //txtview.setText(prof.get(prof.size()-1).toString() + pruefplandaten.profname[i].toString());
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button but1 = (Button) v.findViewById(R.id.BtnOk);
            final TextView textt = (TextView) v.findViewById(R.id.txtmessage);
            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (acProf.getText().toString().equals("Alle")) {
                        int a;
                        rueckgabe.clear();
                        for (a = 0; a < (datenEinlesen.size()); a++) {
                            rueckgabe.add(a);
                        }
                    }

                    database.userDao().suchezuruecksetzen(false);
                    List<Pruefplan> userdaten = AppDatabase.getAppDatabase(v.getContext()).userDao().getAll(validation);
                    for (int i =0; i< Tabellenrueckgabe().size();i++) {
                        // Toast.makeText(getContext(),Tabellenrueckgabe().get(i), Toast.LENGTH_SHORT).show();
                        database.userDao().update2(true,Integer.valueOf(userdaten.get(Integer.valueOf(Tabellenrueckgabe().get(i))).getID()));
                    }

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_placeholder, new TerminefragmentSuche());
                    ft.commit();

                }
            });


        }catch (Exception e)
        {
            Log.d("Fehler sucheFragment","Fehler beim ermitteln der Module");
        }




        return v;
    }
        public List<String> Tabellenrueckgabe () {
            int i;
            int j;
            int k;
            int l;
            String test = "a";
            boolean checkSemester = true;
            for(int z = 0; z< rueckgabeSemester.size(); z++)
            {
                //überprüfung ob Semester ausgewählt wurden, sonst alle Semester anzeigen
                if(rueckgabeSemester.get(z).equals(rueckgabeSemester.get(z+1)))
                {

                }
                else{
                    //nicht Alle Semester anzeigen, weil ein Semester oder mehrere Semester ausgewählt wurden
                    checkSemester = false;
                    break;
                }

            }

            if (checkSemester)
            {
                for (int z = 0; z < datenEinlesen.size(); z++) {
                        rueckgabeSemester.add(z);
                }
            }


            fertigsortiert.clear();
            for (i = 0; i < (rueckgabeStudiengang.size()); i++) {
                for (j = 0; j < (rueckgabeSemester.size()); j++) {
                    if (rueckgabeStudiengang.get(i).equals(rueckgabeSemester.get(j))) {
                        for (k = 0; k < (rueckgabeDatum.size()); k++) {
                            if (rueckgabeDatum.get(k).equals(rueckgabeStudiengang.get(i))) {
                                for (l = 0; l < (rueckgabe.size()); l++) {
                                    if (rueckgabe.get(l).equals(rueckgabeStudiengang.get(i))) {
                                        fertigsortiert.add(String.valueOf(rueckgabeStudiengang.get(i)));
                                        test = String.valueOf(rueckgabeDatum.get(k)) + test;
                                    }
                                }
                            }
                        }
                        //rueckgabeStudiengang.add(i);
                    }
                }
                //txtview.setText(prof.get(prof.size()-1).toString() + pruefplandaten.profname[i].toString());
            }
            return (fertigsortiert);
        }


    }

