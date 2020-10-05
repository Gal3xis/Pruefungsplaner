package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// MyAdapter Recycleview
//
// autor:
// inhalt:  unterteilung von allen Prüfungen in einzelne tabellen und darstellung
// zugriffsdatum: 11.12.19
//
//
//////////////////////////////

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

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
    private List<String> statusList;
    private List<String> statusHintList;
    private boolean speicher;
    private String modulname;
    private TextView txtSecondScreen;
    static boolean favcheck = true;
    private Context context;
    // private Intent calIntent;
    private RecyclerView.LayoutManager aktuelleslayout;
    private List<String> pruefplanid;
    private GregorianCalendar calDate = new GregorianCalendar();

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> module,
                     List<String> prueferUndSemester,
                     List<String> datum,
                     List<String> modul,
                     List<String> ppid,
                     List<String> pruefformList,
                     RecyclerView.LayoutManager mLayout,
                     List<String> raum,
                     List<String> status,
                     List<String> statusHint) {
        uebergebeneModule = module;
        Datum = datum;
        prueferUSemster = prueferUndSemester;
        moduleList = modul;
        pruefplanid = ppid;
        raumAdapter = raum;
        pruefform = pruefformList;
        aktuelleslayout = mLayout;
        statusList = status;
        statusHintList = statusHint;
    }

    public void add(int position, String item) {
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

        // Start Merlin Gürtler
        // erhalte den ausgewählten Studiengang
        SharedPreferences sharedPrefSelectedStudiengang = context.
                getSharedPreferences("validation", Context.MODE_PRIVATE);
        String[] selectedStudiengang = sharedPrefSelectedStudiengang.
                getString("selectedStudiengang", "0").split(" ");

        String colorElectiveModule = "#7FFFD4";
        String[] studiengang = name.split(" ");

        // Ende Merlin Gürtler

        if (!selectedStudiengang[selectedStudiengang.length - 1].equals(studiengang[studiengang.length - 1])) {
            // Lege die Farben für die Wahlmodule fest
            GradientDrawable backGroundGradient = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Color.parseColor(colorElectiveModule),
                            Color.parseColor(colorElectiveModule)});
            backGroundGradient.setCornerRadius(40);

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.layout.setBackgroundDrawable(backGroundGradient);
            } else {
                holder.layout.setBackground(backGroundGradient);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                modulname = "";

                for (int b = 0; b < (studiengang.length - 1); b++) {
                    modulname = (modulname + " " + studiengang[b]);
                }

                //Datenbank und Pruefplan laden
                AppDatabase datenbank = AppDatabase.getAppDatabase(context);
                List<PruefplanEintrag> ppeList = datenbank.userDao().getAll2();

                // Überprüfung, ob Prüfitem favorisiert wurde
                //  Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                //                  Toast.LENGTH_SHORT).show();
                speicher = false;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (position >= 0) {
                            int pruefid = Integer.valueOf(pruefplanid.get(position));

                            for (PruefplanEintrag eintrag : ppeList) {
                                if (Integer.valueOf(eintrag.getID()).equals(pruefid)) {
                                    // Start Merlin Gürtler
                                    // Setze die Farbe des Icons
                                    if (eintrag.getStatus().equals("discussion")) {
                                        holder.statusIcon.setColorFilter(Color.parseColor("#F0E68C"));
                                    }

                                    if (eintrag.getStatus().equals("proposal")) {
                                        holder.statusIcon.setColorFilter(Color.parseColor("#CD5C5C"));
                                    }

                                    //if (eintrag.getStatus().equals("final")) {
                                    //    holder.statusIcon.setColorFilter(Color.parseColor("#228B22"));
                                    //}
                                    // Ende Merlin Gürtler

                                    if (eintrag.getFavorit()) {
                                        holder.ivicon.setColorFilter(Color.parseColor("#06ABF9"));
                                        // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }//for
                        }
                    }
                });
            }
        }).start();

        holder.txtHeader.setText(name);

        // Start Merlin Gürtler

        // Gibt die Statusmeldung aus
        holder.statusIcon.setOnClickListener(v -> {
            if (statusList.get(position).equals("discussion")) {
                Toast.makeText(v.getContext(),
                        statusHintList.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            if (statusList.get(position).equals("proposal")) {
                Toast.makeText(v.getContext(),
                        statusHintList.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            if (statusList.get(position).equals("public")) {
                Toast.makeText(v.getContext(),
                        statusHintList.get(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Ende Merlin Gürtler

        holder.ivicon.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isFavorite = checkFavorite(position);
                    // toggelt den Favoriten
                    if(!isFavorite) {
                        addToFavorites(position, holder);
                    } else {
                        deleteFromFavorites(position, holder);
                    }
                }
            }).start();
        });

        //Aufteilung nach verschiedenen Tagen
        String[] splitTage = Datum.get(position).split(" ");
        if (position > 0) {
            String[] splitTagePositionVorher = Datum.get(position - 1).split(" ");

            //Vergleich der beiden Tage
            //wenn ungleich, dann blaue box mit Datumseintrag
            if (splitTage[0].toString().equals(splitTagePositionVorher[0].toString())) {
                holder.button.setHeight(0);
            }
        }

        //Darstellen der Werte in der Prüfitem Komponente
        String[] splitMonatJahrTage = splitTage[0].split("-");
        holder.txtthirdline.setText(context.getString(R.string.time) + splitTage[1].substring(0, 5).toString());
        holder.button.setText(splitMonatJahrTage[2].toString() + "."
                + splitMonatJahrTage[1].toString() + "."
                + splitMonatJahrTage[0].toString());
        final String[] splitPrueferUndSemester = prueferUSemster.get(position).split(" ");
        holder.txtFooter.setText(context.getString(R.string.prof)
                + splitPrueferUndSemester[0] + ", "
                + splitPrueferUndSemester[1]
                + context.getString(R.string.semester) + splitPrueferUndSemester[2]);
        //holder.txtthirdline.setText("Semester: " + Semester5.toString());
    }

    //Methode zum Darstellen der "weiteren Informationen"
    public String giveString(int position) {
        String name = uebergebeneModule.get(position);
        String[] studiengang = name.split(" ");
        modulname = "";
        int b;
        for (b = 0; b < (studiengang.length - 1); b++) {
            modulname = (modulname + " " + studiengang[b]);
        }
        String raum2 = raumAdapter.get(position);
        String[] aufteilung1 = Datum.get(position).split(" ");
        String[] aufteilung2 = aufteilung1[0].split("-");
        //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
        final String[] sa = prueferUSemster.get(position).split(" ");

        //String mit dem Inhalt für weitere Informationen
        String s = (context.getString(R.string.information) +
                context.getString(R.string.course) + studiengang[studiengang.length - 1]
                + context.getString(R.string.modul) + modulname
                + context.getString(R.string.firstProf) + sa[0]
                + context.getString(R.string.secondProf) + sa[1]
                + context.getString(R.string.date) + aufteilung2[2].toString() + "."
                + aufteilung2[1].toString() + "."
                + aufteilung2[0].toString()
                + context.getString(R.string.clockTime) + aufteilung1[1].substring(0, 5).toString() +
                context.getString(R.string.clock)
                + context.getString(R.string.room) + raum2
                + context.getString(R.string.form) + pruefform.get(position) + "\n \n \n \n \n \n ");

        return (s);
    }
    public void deleteFromFavorites(int position, ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favcheck = false;

                //Datenbank und Pruefplan laden
                AppDatabase datenbank1 = AppDatabase.getAppDatabase(context);
                List<PruefplanEintrag> ppeList1 = datenbank1.userDao().getAll2();

                //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
                //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                // Toast.LENGTH_SHORT).show();

                    for (PruefplanEintrag eintrag : ppeList1) {
                        if ((eintrag.getID().toString()
                                .equals(pruefplanid.get(position)) &
                                (eintrag.getFavorit()))) {
                            datenbank1.userDao()
                                    .update(false,
                                            Integer.valueOf(pruefplanid.get(position)));
                        }
                    }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Start Merlin Gürtler
                        //Entferne den Eintrag aus dem Calendar falls vorhanden
                            CheckGoogleCalendar cal = new CheckGoogleCalendar();
                            cal.setCtx(context);
                            if (!cal.checkCal(Integer.valueOf(pruefplanid.get(position)))) {
                                cal.deleteEntry(Integer.valueOf(pruefplanid.get(position)));
                            }

                            holder.ivicon.clearColorFilter();
                            Toast.makeText(context, context.getString(R.string.delete), Toast.LENGTH_SHORT).show();
                        // Ende Merlin Gürtler
                    }
                });
            }
        }).start();
    }

    public void addToFavorites(int position, ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favcheck = false;

                //Datenbank und Pruefplan laden
                AppDatabase datenbank1 = AppDatabase.getAppDatabase(context);
                List<PruefplanEintrag> ppeList1 = datenbank1.userDao().getAll2();

                //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
                //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                // Toast.LENGTH_SHORT).show();

                //Speichern des Prüfitem als Favorit
                    // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
                    for (PruefplanEintrag eintrag : ppeList1) {
                        if ((eintrag.getID().toString()
                                .equals(pruefplanid.get(position)) &
                                (!eintrag.getFavorit()))) {
                            datenbank1.userDao()
                                    .update(true,
                                            Integer.valueOf(pruefplanid.get(position)));
                        }
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int pruefid = Integer.valueOf(pruefplanid.get(position));
                        for (PruefplanEintrag eintrag : ppeList1) {
                            if (Integer.valueOf(eintrag.getID()).equals(pruefid)) {
                                holder.ivicon.setColorFilter(Color.parseColor("#06ABF9"));
                                // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
                            }
                        }

                        //Speichern des Prüfitem als Favorit
                            // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
                            //Überprüfung ob Pürfungen zum Google Kalender Hinzugefügt werden sollen
                            SharedPreferences GoogleCalenderWert
                                    = context.getSharedPreferences("json8", 0);
                            //Creating editor to store uebergebeneModule to shared preferences
                            SharedPreferences.Editor googlekalenderEditor = GoogleCalenderWert.edit();
                            googlekalenderEditor.apply();
                            String checkGooglecalender
                                    = GoogleCalenderWert.getString("jsondata2", "0");

                            // Überprüfung des Wertes, wenn strJson2 "true" ist dann ist der
                            // Google Kalender aktiviert
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

                            //Abfrage des geklickten Items
                            if (checkeintrag.checkCal(Integer.valueOf(pruefplanid.get(position)))) {
                                if (speicher2) {

                                    //Ermitteln benötigter Variablen
                                    String[] splitDatumUndUhrzeit = Datum.get(position).split(" ");

                                    String[] splitTagMonatJahr = splitDatumUndUhrzeit[0].split("-");

                                    holder.txtthirdline
                                            .setText(context.getString(R.string.time)
                                                    + splitDatumUndUhrzeit[1].substring(0, 5).toString());
                                    holder.button
                                            .setText(splitTagMonatJahr[2].toString() + "."
                                                    + splitTagMonatJahr[1].toString() + "."
                                                    + splitTagMonatJahr[0].toString());
                                    final String[] sa = prueferUSemster.get(position).split(" ");
                                    holder.txtFooter
                                            .setText(context.getString(R.string.prof) + sa[0] + ", " + sa[1]
                                                    + context.getString(R.string.semester) + sa[2]);
                                    String name1 = uebergebeneModule.get(position);
                                    String[] modulname1 = name1.split(" ");
                                    modulname = "";
                                    int b;
                                    for (b = 0; b < (modulname1.length - 1); b++) {
                                        modulname = (modulname + " " + modulname1[b]);
                                    }

                                    int uhrzeitStart
                                            = Integer.valueOf(splitDatumUndUhrzeit[1].substring(0, 2));
                                    int uhrzeitEnde
                                            = Integer.valueOf(splitDatumUndUhrzeit[1].substring(4, 5));
                                    calDate = new GregorianCalendar(
                                            Integer.valueOf(splitTagMonatJahr[0]),
                                            (Integer.valueOf(splitTagMonatJahr[1]) - 1),
                                            Integer.valueOf(splitTagMonatJahr[2]),
                                            uhrzeitStart, uhrzeitEnde);

                                    //Methode zum Speichern im Kalender
                                    int calendarid = calendarID(modulname);

                                    //Funktion im Google-Kalender, um PrüfID und calenderID zu speichern
                                    checkeintrag.insertCal(Integer.valueOf(pruefplanid.get(position)),
                                            calendarid);

                                }
                            }
                            Toast.makeText(context, context.getString(R.string.add), Toast.LENGTH_SHORT).show();
                        }
                });
            }
        }).start();
    }

    public boolean checkFavorite(int position) {
        favcheck = false;

        //Datenbank und Pruefplan laden
        AppDatabase datenbank1 = AppDatabase.getAppDatabase(context);
        List<PruefplanEintrag> ppeList1 = datenbank1.userDao().getAll2();

        //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
        //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
        // Toast.LENGTH_SHORT).show();
        speicher = false;

        for (PruefplanEintrag eintrag : ppeList1) {
            if ((eintrag.getID().toString()
                    .equals(pruefplanid.get(position)) &
                    (eintrag.getFavorit()))) {
                speicher = true;
                break;
                // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
            }
        }

        return speicher;
    }

    //Item anzahl
    @Override
    public int getItemCount() {
        return uebergebeneModule.size();
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
        public LinearLayout bigLayout;
        private ImageView ivicon;
        private ImageView statusIcon;
        private Button button;

        private ViewHolder(View v) {
            super(v);
            ivicon = (ImageView) v.findViewById(R.id.icon);
            statusIcon = (ImageView) v.findViewById(R.id.icon2);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtSecondScreen = (TextView) v.findViewById(R.id.txtSecondscreen);

            txtthirdline = (TextView) v.findViewById(R.id.thirdLine);
            button = (Button) v.findViewById(R.id.button7);

            //button.setLayoutParams(new LinearLayout.LayoutParams(
            //    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout = (LinearLayout) v.findViewById(R.id.linearLayout);
            bigLayout = (LinearLayout) v.findViewById(R.id.linearLayout6);
        }
    }

    public int calendarID(String eventtitle) {

        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 2);
        event.put(CalendarContract.Events.TITLE, modulname);
        event.put(CalendarContract.Events.DESCRIPTION, context.getString(R.string.fh_name));
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
        String projection[] = {"_id", "title"};
        Cursor cursor = context.getContentResolver()
                .query(baseUri, null,
                        null, null, null);

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