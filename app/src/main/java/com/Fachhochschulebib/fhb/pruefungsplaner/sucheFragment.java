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
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import static com.Fachhochschulebib.fhb.pruefungsplaner.Tabelle.ft;

public class sucheFragment extends Fragment {

    SharedPreferences mSharedPreferencesValidation;
    final List<String> sgModulList = new ArrayList();
    final List<String> profList = new ArrayList();
    final List<Integer> rueckgabeProfList = new ArrayList();
    final List<Integer> rueckgabeSgModuleList = new ArrayList();
    final List<Integer> rueckgabeDatumsList = new ArrayList();
    final List<Integer> rueckgabeSemModulList = new ArrayList();
    final List<String> sortedList = new ArrayList();
    private String profName;
    private String dateForSearch = null;
    String pruefJahr, aktuellePruefphase,
            rueckgabeStudiengang, validation;
    List<PruefplanEintrag> ppeList = new ArrayList();

    private AppDatabase database = AppDatabase.getAppDatabase(getContext());

    AppDatabase roomDaten = AppDatabase.getAppDatabase(getContext());

    // Start Merlin Gürtler
    // Funktion um die Führende 0 hinzuzufügen
    public String formatDate (String dateToFormat) {
        if(dateToFormat.length() == 1) {
            dateToFormat = "0" + dateToFormat;
        }
        return dateToFormat;
    }
    // Ende Merlin Gürtler

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

        profName = getContext().getString(R.string.all);

        // Nun aus Shared Preferences
        // Hole die Werte für die Validierung
        mSharedPreferencesValidation
                = container.getContext().getSharedPreferences("validation", 0);

        pruefJahr = mSharedPreferencesValidation.getString("pruefJahr", "0");
        aktuellePruefphase = mSharedPreferencesValidation.getString("aktuellePruefphase", "0");
        rueckgabeStudiengang = mSharedPreferencesValidation.getString("rueckgabeStudiengang", "0");
        String selectedStudiengang  = mSharedPreferencesValidation.
                getString("selectedStudiengang","0");

        // Start Merlin Gürtler
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Erstelle Validierung und starte DB Abfrage
                validation = pruefJahr + rueckgabeStudiengang + aktuellePruefphase;
                ppeList = roomDaten.userDao().getAll(validation);
                // Ende Merlin Gürtler

                //Überprüfung, ob ein Semester-Button geklickt wurde
                //der Wert des Semsters wird gespeichert
                rueckgabeSemModulList.clear();

                //Initialisierung der Anfangswerte
                int i;
                for (i = 0; i < ppeList.size(); i++) {
                    rueckgabeProfList.add(i);
                    rueckgabeSgModuleList.add(i);
                    rueckgabeDatumsList.add(i);
                }

            }
        }).start();

        final View v = inflater.inflate(R.layout.activity_suche, container, false);
        //setContentView(R.layout.hauptfenster);

        final AutoCompleteTextView acProf
                = (AutoCompleteTextView) v.findViewById(R.id.acProfessor);

        Spinner spSGModule = (Spinner) v.findViewById(R.id.spStudiengang);

        //Initialiseren der UI Komponente
        //Spinners spinner = new Spinners();
        final Button btnSemester1 = (Button) v.findViewById(R.id.btns1);
        final Button btnSemester2 = (Button) v.findViewById(R.id.btns2);
        final Button btnSemester3 = (Button) v.findViewById(R.id.btns3);
        final Button btnSemester4 = (Button) v.findViewById(R.id.btns4);
        final Button btnSemester5 = (Button) v.findViewById(R.id.btns5);
        final Button btnSemester6 = (Button) v.findViewById(R.id.btns6);

        // Start Merlin Gürtler
        registerButton(btnSemester1,1);
        registerButton(btnSemester2,2);
        registerButton(btnSemester3,3);
        registerButton(btnSemester4,4);
        registerButton(btnSemester5,5);
        registerButton(btnSemester6,6);
        // Ende Merlin Gürtler

        //Auswahlmöglichkeit "Klicken um Modul zu wählen" hinzufügen
        List<String> spinnerModuleArrayList = new ArrayList<String>();
        spinnerModuleArrayList.add(0, getContext().getString(R.string.modul_search));

        //Adapter-Aufruf (LG: Sind hier alle drei Adapter notwendig?)
        // Auswahl Module
        // Hier schon setzen für ein besseres UI
        ArrayAdapter<String> adapterModule = new ArrayAdapter<String>(
                v.getContext(), R.layout.simple_spinner_item, spinnerModuleArrayList);
        spSGModule.setAdapter(adapterModule);

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Spinner-Aufruf und Spinner mit Werten füllen
                    spinnerModuleArrayList.addAll(roomDaten.userDao().getModuleWithCourseDistinct(selectedStudiengang));
                    List<String> spinnerProfArrayList = roomDaten.userDao().getErstprueferDistinct(selectedStudiengang);


                    // Für das AutoComplete
                    ArrayAdapter<String> adapterProfAutoComplete = new ArrayAdapter<String>
                            (v.getContext(), android.R.layout.simple_list_item_1, spinnerProfArrayList);

                    //Grafische Ausgabe dropdown
                    adapterModule.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // adapterProf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //Spinner spProf = (Spinner) v.findViewById(R.id.spProf);
                            //spProf.setAdapter(adapterProf);

                            acProf.setThreshold(1);//will start working from first character
                            acProf.setAdapter(adapterProfAutoComplete);//setting the adapter data
                            // into the AutoCompleteTextView
                            // beim ändern der Orientierung crasht die app wegen Problemen mit dem Context
                            try {
                                profList.add(getContext().getString(R.string.all));
                                sgModulList.add(getContext().getString(R.string.all));
                            } catch (Exception e) {
                                Log.d("ERROR", "ERROR " + e);
                            }

                        }
                    });
                }
            }).start();


            // Start Merlin Gürtler
            final TextView searchDate = v.findViewById(R.id.daySearch);

            searchDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPrefPruefPeriode
                            = getContext().getSharedPreferences("PruefPeriode", Context.MODE_PRIVATE);
                    String startDate = sharedPrefPruefPeriode.getString("startDatum", "0");
                    String endDate = sharedPrefPruefPeriode.getString("endDatum", "0");

                    // Daten des Startdatums
                    int day = Integer.parseInt(startDate.substring(0,2));
                    int month = Integer.parseInt(startDate.substring(3,5));
                    int year = Integer.parseInt(startDate.substring(6,10));

                    Calendar startDateForPicker = Calendar.getInstance();
                    startDateForPicker.set(Calendar.YEAR, year);
                    startDateForPicker.set(Calendar.MONTH, month - 1);
                    startDateForPicker.set(Calendar.DAY_OF_MONTH, day);

                    // Daten des Enddatums
                    int day2 = Integer.parseInt(endDate.substring(0,2));
                    int month2 = Integer.parseInt(endDate.substring(3,5));
                    int year2 = Integer.parseInt(endDate.substring(6,10));

                    Calendar endDateForPicker = Calendar.getInstance();
                    endDateForPicker.set(Calendar.YEAR, year2);
                    endDateForPicker.set(Calendar.MONTH, month2 - 1);
                    endDateForPicker.set(Calendar.DAY_OF_MONTH, day2);

                    DatePickerDialog picker = new DatePickerDialog(getContext(),
                            R.style.ProgressStyle,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    searchDate.setText(formatDate(String.valueOf(dayOfMonth))
                                            + "." + formatDate(String.valueOf(monthOfYear))
                                            + "." + formatDate(String.valueOf(year)));

                                    // Das Datum für die Abfrage
                                    Calendar selectedDate = Calendar.getInstance();
                                    selectedDate.set(Calendar.YEAR, year);
                                    selectedDate.set(Calendar.MONTH, monthOfYear);
                                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    SimpleDateFormat targetFormat
                                            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    dateForSearch = targetFormat.format(selectedDate.getTime());
                                }
                            }, year, month, day);
                    // Setze das Start- und Enddatum
                    picker.getDatePicker().setMinDate(startDateForPicker.getTimeInMillis());
                    picker.getDatePicker().setMaxDate(endDateForPicker.getTimeInMillis());
                    picker.show();

                }
            });

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
                        if (acProf.getText().toString().equals(getContext().getString(R.string.all))) {
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
                    try {
                        inputMethodManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.d("Exception", "Keyboard not open");
                    }
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
                                .equals(getContext().getString(R.string.modul_search))) {
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(dateForSearch != null) {
                                sortedList.clear();
                                ppeList = roomDaten.userDao().getByDate(dateForSearch.substring(0,10) + "%");

                                for(PruefplanEintrag eintrag: ppeList) {
                                    sortedList.add(String.valueOf(eintrag.getID()));
                                }

                                database.userDao().sucheUndZurueckSetzen(false);
                                for (int i =0; i< sortedList.size();i++) {
                                    // Toast.makeText(getContext(),Tabellenrueckgabe().get(i), Toast.LENGTH_SHORT).show();
                                    database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                                }
                            }
                            else {
                                if(profName.equals(getContext().getString(R.string.all))
                                        &&  !sgModulList.get(sgModulList.size() - 1).toString().
                                        equals(getContext().getString(R.string.modul_search)))
                                {
                                    sortedList.clear();
                                    ppeList = roomDaten.userDao().getModule(sgModulList.get(sgModulList .size() - 1));
                                    for(PruefplanEintrag eintrag: ppeList) {
                                        sortedList.add(String.valueOf(eintrag.getID()));
                                    }

                                    database.userDao().sucheUndZurueckSetzen(false);
                                    for (int i =0; i< sortedList.size();i++) {
                                        // Toast.makeText(getContext(),Tabellenrueckgabe().get(i), Toast.LENGTH_SHORT).show();
                                        database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                                    }
                                } else if(!profName.equals(getContext().getString(R.string.all))) {
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
                                    if (acProf.getText().toString().equals(getContext().getString(R.string.all))) {
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
                    }).start();
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