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
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import static com.Fachhochschulebib.fhb.pruefungsplaner.table.ft;

public class searchFragment extends Fragment {

    SharedPreferences mSharedPreferencesValidation;
    final List<String> courseModuleList = new ArrayList();
    final List<String> profList = new ArrayList();
    final List<Integer> returnProfList = new ArrayList();
    final List<Integer> returnCourseModuleList = new ArrayList();
    final List<Integer> returnDateList = new ArrayList();
    final List<Integer> returnSemesterModuleList = new ArrayList();
    final List<String> sortedList = new ArrayList();
    private String profName;
    private String dateForSearch = null;
    String examineYear, currentExaminePeriod,
            returnCourse, validation;
    List<TestPlanEntry> ppeList = new ArrayList();

    private AppDatabase database = AppDatabase.getAppDatabase(getContext());

    AppDatabase roomData = AppDatabase.getAppDatabase(getContext());

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
                    if (returnSemesterModuleList.size() <= 0) {
                        for (int i = 0; i < (ppeList.size()); i++) {
                            returnSemesterModuleList.add(99999);
                        }
                    }

                    for (int i = 0; i < ppeList.size(); i++) {
                        if (String.valueOf(value).equals(ppeList.get(i).getSemester())) {
                            btn.setBackgroundResource(R.drawable.button_rounded_corners);
                            returnSemesterModuleList.set(i, i);

                        }
                    }
                    clicked = false;
                } else {

                    btn.setBackgroundResource(R.drawable.button_rounded_corners2);

                    for (int i = 0; i < ppeList.size(); i++) {
                        if (String.valueOf(value).equals(ppeList.get(i).getSemester())) {
                            returnSemesterModuleList.set(i, 99999);
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

        examineYear = mSharedPreferencesValidation.getString("examineYear", "0");
        currentExaminePeriod = mSharedPreferencesValidation.getString("currentPeriode", "0");
        returnCourse = mSharedPreferencesValidation.getString("returnCourse", "0");
        String selectedCourse  = mSharedPreferencesValidation.
                getString("selectedCourse","0");

        // Start Merlin Gürtler
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Erstelle Validierung und starte DB Abfrage
                validation = examineYear + returnCourse + currentExaminePeriod;
                ppeList = roomData.userDao().getEntriesByValidation(validation);
                // Ende Merlin Gürtler

                //Überprüfung, ob ein Semester-Button geklickt wurde
                //der Wert des Semsters wird gespeichert
                returnSemesterModuleList.clear();

                //Initialisierung der Anfangswerte
                int i;
                for (i = 0; i < ppeList.size(); i++) {
                    returnProfList.add(i);
                    returnCourseModuleList.add(i);
                    returnDateList.add(i);
                }

            }
        }).start();

        final View v = inflater.inflate(R.layout.activity_suche, container, false);
        //setContentView(R.layout.hauptfenster);

        final AutoCompleteTextView acProf
                = (AutoCompleteTextView) v.findViewById(R.id.acProfessor);

        Spinner spCourseModule = (Spinner) v.findViewById(R.id.spStudiengang);

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
        spCourseModule.setAdapter(adapterModule);

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Spinner-Aufruf und Spinner mit Werten füllen
                    spinnerModuleArrayList.addAll(roomData.userDao().getModuleWithCourseDistinct(selectedCourse));
                    List<String> spinnerProfArrayList = roomData.userDao().getFirstExaminerDistinct(selectedCourse);


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
                                courseModuleList.add(getContext().getString(R.string.all));
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
                    SharedPreferences sharedPrefCurrentPeriode
                            = getContext().getSharedPreferences("currentPeriode", Context.MODE_PRIVATE);

                    String startDate = sharedPrefCurrentPeriode.getString("startDate", "0");
                    String endDate = sharedPrefCurrentPeriode.getString("endDate", "0");
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
                    returnProfList.clear();
                    profName = acProf.getText().toString();
                    for (a = 0; a < ppeList.size(); a++) {
                        if (acProf.getText().toString().equals(getContext().getString(R.string.all))) {
                            returnProfList.add(a);
                        } else if (Pattern.matches("^.*("                       // Wildcard begin
                                        + acProf.getText().toString().trim().toLowerCase()    // Input Name
                                        +").*$",                                              // Wildcard end
                            ppeList.get(a).getFirstExaminer().toLowerCase())) // Name in db
                        {
                            returnProfList.add(a);
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

            spCourseModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    returnCourseModuleList.clear();
                    //this is your selected item
                    courseModuleList.add(parent.getItemAtPosition(position).toString());

                    int i;
                    String a;
                    for (i = 0; i < (ppeList.size()); i++) {
                        if (courseModuleList.get(courseModuleList.size() - 1).toString()
                                .equals(getContext().getString(R.string.modul_search))) {
                            returnCourseModuleList.add(i);
                        } else {
                            if (courseModuleList.get(courseModuleList.size() - 1).toString()
                                    .equals(ppeList.get(i).getModule().toString())) {
                                returnCourseModuleList.add(i);
                                // database.userDao().Checkverbindung(tableReturn());
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
                                ppeList = roomData.userDao().getEntriesByDate(dateForSearch.substring(0,10) + "%");

                                for(TestPlanEntry entry: ppeList) {
                                    sortedList.add(String.valueOf(entry.getID()));
                                }

                                database.userDao().searchAndReset(false);
                                for (int i =0; i< sortedList.size();i++) {
                                    // Toast.makeText(getContext(),tableReturn().get(i), Toast.LENGTH_SHORT).show();
                                    database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                                }
                            }
                            else {
                                if(profName.equals(getContext().getString(R.string.all))
                                        &&  !courseModuleList.get(courseModuleList.size() - 1).toString().
                                        equals(getContext().getString(R.string.modul_search)))
                                {
                                    sortedList.clear();
                                    ppeList = roomData.userDao().getEntriesByModule(courseModuleList.get(courseModuleList.size() - 1));
                                    for(TestPlanEntry entry: ppeList) {
                                        sortedList.add(String.valueOf(entry.getID()));
                                    }

                                    database.userDao().searchAndReset(false);
                                    for (int i =0; i< sortedList.size();i++) {
                                        // Toast.makeText(getContext(),tableReturn().get(i), Toast.LENGTH_SHORT).show();
                                        database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                                    }
                                } else if(!profName.equals(getContext().getString(R.string.all))) {
                                    sortedList .clear();
                                    ppeList = roomData.userDao().getEntriesByProf("%" +
                                            acProf.getText().toString().trim() + "%");

                                    for(int m = 0; m < ppeList.size(); m++) {
                                        sortedList.add(String.valueOf(ppeList.get(m).getID()));
                                    }

                                    database.userDao().searchAndReset(false);
                                    for (int i =0; i< sortedList.size();i++) {
                                        // Toast.makeText(getContext(),tableReturn().get(i), Toast.LENGTH_SHORT).show();
                                        database.userDao().update2(true, Integer.valueOf(sortedList.get(i)));
                                    }
                                } else {
                                    // Ende Merlin Gürtler
                                    if (acProf.getText().toString().equals(getContext().getString(R.string.all))) {
                                        int a;
                                        returnProfList.clear();
                                        for (a = 0; a < (ppeList.size()); a++) {
                                            returnProfList.add(a);
                                        }
                                    }

                                    database.userDao().searchAndReset(false);
                                    List<TestPlanEntry> ppeList = AppDatabase.getAppDatabase(v.getContext())
                                            .userDao().getEntriesByValidation(validation);
                                    for (int i = 0; i < tableReturn().size(); i++) {
                                        // Toast.makeText(getContext(),tableReturn().get(i),
                                        // Toast.LENGTH_SHORT).show();
                                        database.userDao().update2(true,
                                                Integer.valueOf(ppeList.get(
                                                        Integer.valueOf(tableReturn().get(i))).getID()));
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
                            ft.replace(R.id.frame_placeholder, new TermineFragmentSearch());
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

    public List<String> tableReturn() {
        int i, j, k, l;
        String test = "a";
        boolean checkSemester = true;
        for(int z = 0; z< returnSemesterModuleList.size(); z++)
        {
            //überprüfung, ob Semester ausgewählt wurden. Sonst alle Semester anzeigen.
            if(!returnSemesterModuleList.get(z).equals(returnSemesterModuleList.get(z+1)))
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
                returnSemesterModuleList.add(z);
            }
        }

        sortedList.clear();
        for (i = 0; i < (returnCourseModuleList.size()); i++) {
            for (j = 0; j < (returnSemesterModuleList.size()); j++) {
                if (returnCourseModuleList.get(i).equals(returnSemesterModuleList.get(j))) {
                    for (k = 0; k < (returnDateList.size()); k++) {
                        if (returnDateList
                                .get(k)
                                .equals(returnCourseModuleList.get(i))) {
                            for (l = 0; l < (returnProfList.size()); l++) {
                                if (returnProfList
                                        .get(l)
                                        .equals(returnCourseModuleList.get(i))) {
                                    sortedList.add(
                                            String.valueOf(returnCourseModuleList.get(i)));
                                    test = String.valueOf(returnDateList.get(k)) + test;
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