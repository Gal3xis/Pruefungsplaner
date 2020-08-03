package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// MyAdapter Recycleview
//
//
//
// autor:
// inhalt:  unterteilung von allen Prüfungen in einzelne tabellen und darstellung
// zugriffsdatum: 11.12.19
//
//
//
//
//
//
//////////////////////////////

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.Pruefplan;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public List<String> uebergebeneModule;
    private List<String> prueferUSemster;
    private List<String> moduleList;
    private List<String> Datum;
    private List<String> raumAdapter;
    private List<String> pruefform;
    private boolean speicher;
    private String studiengang;
    private TextView txtSecondScreen;
    static boolean favcheck = true;
    private Context context;
    private Intent calIntent;
    private RecyclerView.LayoutManager aktuelleslayout;
    private List<String> pruefplanid;
   private GregorianCalendar calDate =new GregorianCalendar();

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> module, List<String> prueferUndSemester, List<String> daatum, List<String> modul,List<String> ppid,List<String> pruefformList,RecyclerView.LayoutManager mLayout,List<String>  raum) {
        uebergebeneModule = module;
        Datum = daatum;
        prueferUSemster = prueferUndSemester;
        moduleList = modul;
        pruefplanid = ppid;
        raumAdapter = raum;
        pruefform = pruefformList;
        aktuelleslayout = mLayout;



    }

    public void add(int position, String item, String studiengang) {
        uebergebeneModule.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        uebergebeneModule.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.termine, parent, false);
        context = v.getContext();
        ViewHolder vh = new ViewHolder(v);



        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name = uebergebeneModule.get(position);
        String[] modulname = name.split(" ");
        studiengang = "";


        int b;
        for (b = 0; b < (modulname.length - 1); b++) {
            studiengang = (studiengang + " " + modulname[b]);

        }


        //Datenbank und Pruefplan laden
        AppDatabase datenbank =  AppDatabase.getAppDatabase(context);
        List<Pruefplan> pruefplandaten = datenbank.userDao().getAll2();

        //Überprüfung ob Prüfitem Favorisiert wurde
        int i;
        //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()), Toast.LENGTH_SHORT).show();
        speicher = false;
        int pruefid = Integer.valueOf(pruefplanid.get(position));
        for (i = 0; i < pruefplandaten.size(); i++) {
            if (Integer.valueOf(pruefplandaten.get(i).getID()).equals(pruefid))
            {
            if (pruefplandaten.get(i).getFavorit() ) {
                holder.ivicon.setColorFilter(Color.parseColor("#06ABF9"));
                // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
            }}}


        holder.txtHeader.setText(name);
        holder.ivicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favcheck = false;

                //Datenbank und Pruefplan laden
                AppDatabase datenbank =  AppDatabase.getAppDatabase(context);
                List<Pruefplan> pruefplandaten = datenbank.userDao().getAll2();

                //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
                int i;
                //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()), Toast.LENGTH_SHORT).show();
                speicher = false;
                for (i = 0; i < pruefplandaten.size(); i++) {
                            if ((pruefplandaten.get(i).getID().toString().equals(pruefplanid.get(position) )& ( pruefplandaten.get(i).getFavorit() ))) {
                                speicher = true;
                               // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
                            } }

                int pruefid = Integer.valueOf(pruefplanid.get(position));
                for (i = 0; i < pruefplandaten.size(); i++) {
                    if (Integer.valueOf(pruefplandaten.get(i).getID()).equals(pruefid))
                    {

                            holder.ivicon.setColorFilter(Color.parseColor("#06ABF9"));
                            // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
                        }}


                //Speichern des Prüfitem als Favorit
                if (!speicher) {
                   // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
                    for (i = 0; i < pruefplandaten.size(); i++) {
                            if ((pruefplandaten.get(i).getID().toString().equals(pruefplanid.get(position) ) & (!pruefplandaten.get(i).getFavorit() ))) {
                                datenbank.userDao().update(true,Integer.valueOf(pruefplanid.get(position)));
                               // Toast.makeText(v.getContext(), "140", Toast.LENGTH_SHORT).show();
                                List<Pruefplan> userdaten2 = datenbank.userDao().getAll2();
                                //Toast.makeText(v.getContext(), String.valueOf(userdaten2.get(i).getFavorit()), Toast.LENGTH_SHORT).show();
                            }
                    }


                    //Überprüfung ob Pürfungen zum Google Kalender Hinzugefügt werden sollen
                    SharedPreferences GoogleCalenderWert = v.getContext().getSharedPreferences("json8", 0);
                    //Creating editor to store uebergebeneModule to shared preferences
                    SharedPreferences.Editor googlekalenderEditor = GoogleCalenderWert.edit();
                    googlekalenderEditor.apply();
                    String checkGooglecalender = GoogleCalenderWert.getString("jsondata2", "0");

                    //Überprüfung des Wertes, wenn strJson2 "true" ist dann ist der Google Kalender aktiviert
                    boolean speicher2 = false;

                    for (int zaehler = 0; zaehler < checkGooglecalender.length(); zaehler++) {
                         String ss1 = String.valueOf(checkGooglecalender.charAt(zaehler));
                        if (ss1.equals(String.valueOf(1))) {
                            speicher2 = true;

                        }
                    }

                    //Hinzufügen der Prüfungen zum Google Kalender
                    CheckGoogleCalendar checkeintrag = new CheckGoogleCalendar();
                    checkeintrag.setCtx(context);




                    //Abrage vom geklickten Item
                    if(checkeintrag.checkCal(Integer.valueOf(pruefplanid.get(position)))) {
                        if (speicher2) {

                            //ermitteln von benötigten Variablen
                            String[] splitDatumUndUhrzeit = Datum.get(position).split(" ");
                            System.out.println(splitDatumUndUhrzeit[0]);
                            String[] splitTagMonatJahr = splitDatumUndUhrzeit[0].split("-");
                            System.out.println(splitDatumUndUhrzeit[0]);
                            holder.txtthirdline.setText("Uhrzeit: " + splitDatumUndUhrzeit[1].substring(0, 5).toString());
                            holder.button.setText(splitTagMonatJahr[2].toString() + "." + splitTagMonatJahr[1].toString() + "." + splitTagMonatJahr[0].toString());
                            final String[] sa = prueferUSemster.get(position).split(" ");
                            holder.txtFooter.setText("Prüfer: " + sa[0] + ", " + sa[1] + "  Semester: " + sa[2]);
                            String name = uebergebeneModule.get(position);
                            String[] modulname = name.split(" ");
                            studiengang = "";
                            int b;
                            for (b = 0; b < (modulname.length - 1); b++) {
                                studiengang = (studiengang + " " + modulname[b]);
                            }

                            int uhrzeitStart = Integer.valueOf(splitDatumUndUhrzeit[1].substring(0, 2));
                            int uhrzeitEnde = Integer.valueOf(splitDatumUndUhrzeit[1].substring(4, 5));
                            calDate = new GregorianCalendar(Integer.valueOf(splitTagMonatJahr[0]), (Integer.valueOf(splitTagMonatJahr[1]) - 1), Integer.valueOf(splitTagMonatJahr[2]), uhrzeitStart, uhrzeitEnde);


                            //Methode zum speichern im Kalender
                            int calendarid = calendarID(studiengang);


                            //funktion im Google Kalender um PrüfID und calenderID zu speichern
                            checkeintrag.insertCal(Integer.valueOf(pruefplanid.get(position)), calendarid);

                        }
                    }
                        Toast.makeText(v.getContext(), "Hinzugefügt", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Aufteilung Nach Verschieden tagen
        String[] splitTage = Datum.get(position).split(" ");
        System.out.println(splitTage[0]);
        if (position > 0) {
            String[] splitTagePositionVorher = Datum.get(position - 1).split(" ");

            //Vergleich der beiden Tage
            //wenn ungleich dann blaue box mit Datumseintrag
            if (splitTage[0].toString().equals(splitTagePositionVorher[0].toString())) {
                holder.button.setHeight(0);
            }
        }


        //darstellen der Werte in der Prüfitem Komponente
        String[] splitMonatJahrTage = splitTage[0].split("-");
        System.out.println(splitTage[0]);
        holder.txtthirdline.setText("Uhrzeit: " + splitTage[1].substring(0, 5).toString());
        holder.button.setText(splitMonatJahrTage[2].toString() + "." + splitMonatJahrTage[1].toString() + "." + splitMonatJahrTage[0].toString());
        final String[] splitPrueferUndSemester = prueferUSemster.get(position).split(" ");
        holder.txtFooter.setText("Prüfer: " + splitPrueferUndSemester[0] + ", " + splitPrueferUndSemester[1] + "  Semester: " + splitPrueferUndSemester[2]);
        //holder.txtthirdline.setText("Semester: " + Semester5.toString());

    }

    //Methode zum darstellen der "weiteren Informationen"
    public String giveString(int position) {

        try {
            String name = uebergebeneModule.get(position);
            String[] modulname = name.split(" ");
            studiengang = "";
            int b;
            for (b = 0; b < (modulname.length - 1); b++) {
                studiengang = (studiengang + " " + modulname[b]);

            }
        String raum2 = raumAdapter.get(position);
        String[] aufteilung1 = Datum.get(position).split(" ");
        String[] aufteilung2 = aufteilung1[0].split("-");
        //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
        final String[] sa = prueferUSemster.get(position).split(" ");

        //String mit dem inhalt für weitere Informationen
        String s = ("Informationen zur Prüfung \n \n Studiengang: " + modulname[modulname.length - 1] + "\n Modul: " + studiengang + "\n Erstprüfer: " + sa[0] + " \n Zweitprüfer: " + sa[1] + "\n Datum: " + aufteilung2[2].toString() + "." + aufteilung2[1].toString() + "." + aufteilung2[0].toString() + " \n Uhrzeit: " + aufteilung1[1].substring(0, 5).toString() +" Uhr" + " \n Raum: "+ raum2 +"\n Prüfungsform: "+ pruefform.get(position
        ) + "\n \n \n \n \n \n ");

        return (s);
        }catch(Exception e){

        }
        return ("0");
    }

    //Item anzahl
    @Override
    public int getItemCount() {
        return uebergebeneModule.size();
    }


    public int disabbleview() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {


        return position;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txtHeader;
        private TextView txtFooter;
        private TextView txtthirdline;
        public LinearLayout layout;
        private ImageView ivicon;
        private Button button;


        private ViewHolder(View v) {
            super(v);
            ivicon = (ImageView) v.findViewById(R.id.icon);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtSecondScreen = (TextView) v.findViewById(R.id.txtSecondscreen);

            txtthirdline = (TextView) v.findViewById(R.id.thirdLine);
            button = (Button) v.findViewById(R.id.button7);


            //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout = (LinearLayout) v.findViewById(R.id.linearLayout);

        }
    }



    public int calendarID(String eventtitle){

        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 2);
        event.put(CalendarContract.Events.TITLE, studiengang);
        event.put(CalendarContract.Events.DESCRIPTION, "Fachhochschule Bielefeld");
        event.put(CalendarContract.Events.DTSTART, calDate.getTimeInMillis());
        event.put(CalendarContract.Events.DTEND, calDate.getTimeInMillis() + (90 * 60000));
        event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
        event.put(CalendarContract.Events.HAS_ALARM, 0); // 0 for false, 1 for true
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        Uri baseUri;

        if (Build.VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");

        } else {
            baseUri = Uri.parse("content://calendar/events");
        }

        context.getContentResolver().insert(baseUri, event);


        int result = 0;
        String projection[] = { "_id", "title" };
        Cursor cursor = context.getContentResolver().query(baseUri, null, null, null,
                null);

        if (cursor.moveToFirst()) {

            String calName;
            String calID;

            int nameCol = cursor.getColumnIndex(projection[1]);
            int idCol = cursor.getColumnIndex(projection[0]);
            do {
                calName = cursor.getString(nameCol);
                calID = cursor.getString(idCol);

                if (calName != null && calName.contains(eventtitle)) {
                    result = Integer.parseInt(calID);
                }

            } while (cursor.moveToNext());
            cursor.close();

        }
        return (result);
    }

}