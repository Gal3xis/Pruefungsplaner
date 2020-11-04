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
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public List<String> moduls;
    private List<String> examinerAndSemester;
    private List<String> moduleList;
    private List<String> date;
    private List<String> roomAdapter;
    private List<String> examForm;
    private List<String> statusList;
    private List<String> statusHintList;
    private boolean save;
    private String modulName;
    private TextView txtSecondScreen;
    static boolean favcheck = true;
    private Context context;
    // private Intent calIntent;
    private RecyclerView.LayoutManager currentLayout;
    private List<String> planId;
    private GregorianCalendar calDate = new GregorianCalendar();

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> passedModuls,
                     List<String> passedExaminerAndSemester,
                     List<String> passedDate,
                     List<String> modul,
                     List<String> passedId,
                     List<String> passedExamForm,
                     RecyclerView.LayoutManager mLayout,
                     List<String> passedRoom,
                     List<String> passedStatus,
                     List<String> passedStatusHint) {
        moduls = passedModuls;
        date = passedDate;
        examinerAndSemester = passedExaminerAndSemester;
        moduleList = modul;
        planId = passedId;
        roomAdapter = passedRoom;
        examForm = passedExamForm;
        currentLayout = mLayout;
        statusList = passedStatus;
        statusHintList = passedStatusHint;
    }

    public void add(int position, String item) {
        moduls.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        moduls.remove(position);
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
        String name = moduls.get(position);

        // Start Merlin Gürtler
        // erhalte den ausgewählten Studiengang
        SharedPreferences sharedPreferencesSelectedCourse = context.
                getSharedPreferences("validation", Context.MODE_PRIVATE);
        String[] selectedCourse = sharedPreferencesSelectedCourse.
                getString("selectedCourse", "0").split(" ");

        String colorElectiveModule = "#7FFFD4";
        String[] course = name.split(" ");

        // Ende Merlin Gürtler

        if (!selectedCourse[selectedCourse.length - 1].equals(course[course.length - 1])) {
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
                modulName = "";

                for (int b = 0; b < (course.length - 1); b++) {
                    modulName = (modulName + " " + course[b]);
                }

                //Datenbank und Pruefplan laden
                AppDatabase database = AppDatabase.getAppDatabase(context);
                List<TestPlanEntry> ppeList = database.userDao().getAll();

                // Überprüfung, ob Prüfitem favorisiert wurde
                //  Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                //                  Toast.LENGTH_SHORT).show();
                save = false;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (position >= 0) {
                            int pruefid = Integer.valueOf(planId.get(position));

                            for (TestPlanEntry entry : ppeList) {
                                if (Integer.valueOf(entry.getID()).equals(pruefid)) {
                                    // Start Merlin Gürtler
                                    // Setze die Farbe des Icons
                                    holder.statusIcon.setColorFilter(Color.parseColor(entry.getColor()));

                                    //if (eintrag.getStatus().equals("final")) {
                                    //    holder.statusIcon.setColorFilter(Color.parseColor("#228B22"));
                                    //}
                                    // Ende Merlin Gürtler

                                    if (entry.getFavorit()) {
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
            Toast.makeText(v.getContext(),
                    statusHintList.get(position),
                    Toast.LENGTH_SHORT).show();
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
        String[] splitDay = date.get(position).split(" ");
        if (position > 0) {
            String[] splitDayBefore = date.get(position - 1).split(" ");

            //Vergleich der beiden Tage
            //wenn ungleich, dann blaue box mit Datumseintrag
            if (splitDay[0].toString().equals(splitDayBefore[0].toString())) {
                holder.button.setHeight(0);
            }
        }

        //Darstellen der Werte in der Prüfitem Komponente
        String[] splitMonthDayYear = splitDay[0].split("-");
        holder.txtthirdline.setText(context.getString(R.string.time) + splitDay[1].substring(0, 5).toString());
        holder.button.setText(splitMonthDayYear[2].toString() + "."
                + splitMonthDayYear[1].toString() + "."
                + splitMonthDayYear[0].toString());
        final String[] splitExaminerAndSemester = examinerAndSemester.get(position).split(" ");
        holder.txtFooter.setText(context.getString(R.string.prof)
                + splitExaminerAndSemester[0] + ", "
                + splitExaminerAndSemester[1]
                + context.getString(R.string.semester) + splitExaminerAndSemester[2]);
        //holder.txtthirdline.setText("Semester: " + Semester5.toString());
    }

    //Methode zum Darstellen der "weiteren Informationen"
    public String giveString(int position) {
        String name = moduls.get(position);
        String[] course = name.split(" ");
        modulName = "";
        int b;
        for (b = 0; b < (course.length - 1); b++) {
            modulName = (modulName + " " + course[b]);
        }
        String room2 = roomAdapter.get(position);
        String[] division1 = date.get(position).split(" ");
        String[] division2 = division1[0].split("-");
        //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
        final String[] sa = examinerAndSemester.get(position).split(" ");

        //String mit dem Inhalt für weitere Informationen
        String s = (context.getString(R.string.information) +
                context.getString(R.string.course) + course[course.length - 1]
                + context.getString(R.string.modul) + modulName
                + context.getString(R.string.firstProf) + sa[0]
                + context.getString(R.string.secondProf) + sa[1]
                + context.getString(R.string.date) + division2[2].toString() + "."
                + division2[1].toString() + "."
                + division2[0].toString()
                + context.getString(R.string.clockTime) + division1[1].substring(0, 5).toString() +
                context.getString(R.string.clock)
                + context.getString(R.string.room) + room2
                + context.getString(R.string.form) + examForm.get(position) + "\n \n \n \n \n \n ");

        return (s);
    }
    public void deleteFromFavorites(int position, ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                favcheck = false;

                //Datenbank und Pruefplan laden
                AppDatabase database = AppDatabase.getAppDatabase(context);
                List<TestPlanEntry> ppeList1 = database.userDao().getAll();

                //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
                //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                // Toast.LENGTH_SHORT).show();

                    for (TestPlanEntry entry : ppeList1) {
                        if ((entry.getID().toString()
                                .equals(planId.get(position)) &
                                (entry.getFavorit()))) {
                            database.userDao()
                                    .update(false,
                                            Integer.valueOf(planId.get(position)));
                        }
                    }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Start Merlin Gürtler
                        //Entferne den Eintrag aus dem Calendar falls vorhanden
                            CheckGoogleCalendar cal = new CheckGoogleCalendar();
                            cal.setCtx(context);
                            if (!cal.checkCal(Integer.valueOf(planId.get(position)))) {
                                cal.deleteEntry(Integer.valueOf(planId.get(position)));
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
                AppDatabase database = AppDatabase.getAppDatabase(context);
                List<TestPlanEntry> ppeList1 = database.userDao().getAll();

                //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
                //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
                // Toast.LENGTH_SHORT).show();

                //Speichern des Prüfitem als Favorit
                    // Toast.makeText(v.getContext(), "137", Toast.LENGTH_SHORT).show();
                    for (TestPlanEntry entry : ppeList1) {
                        if ((entry.getID().toString()
                                .equals(planId.get(position)) &
                                (!entry.getFavorit()))) {
                            database.userDao()
                                    .update(true,
                                            Integer.valueOf(planId.get(position)));
                        }
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int pruefid = Integer.valueOf(planId.get(position));
                        for (TestPlanEntry entry : ppeList1) {
                            if (Integer.valueOf(entry.getID()).equals(pruefid)) {
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
                            boolean save2 = false;

                            for (int counter = 0; counter < checkGooglecalender.length(); counter++) {
                                String ss1 = String.valueOf(checkGooglecalender.charAt(counter));
                                if (ss1.equals(String.valueOf(1))) {
                                    save2 = true;
                                }
                            }

                            //Hinzufügen der Prüfungen zum Google Kalender
                            CheckGoogleCalendar checkEntry = new CheckGoogleCalendar();
                            checkEntry.setCtx(context);

                            //Abfrage des geklickten Items
                            if (checkEntry.checkCal(Integer.valueOf(planId.get(position)))) {
                                if (save2) {

                                    //Ermitteln benötigter Variablen
                                    String[] splitDateAndTime = date.get(position).split(" ");

                                    String[] splitDayMonthYear = splitDateAndTime[0].split("-");

                                    holder.txtthirdline
                                            .setText(context.getString(R.string.time)
                                                    + splitDateAndTime[1].substring(0, 5).toString());
                                    holder.button
                                            .setText(splitDayMonthYear[2].toString() + "."
                                                    + splitDayMonthYear[1].toString() + "."
                                                    + splitDayMonthYear[0].toString());
                                    final String[] sa = examinerAndSemester.get(position).split(" ");
                                    holder.txtFooter
                                            .setText(context.getString(R.string.prof) + sa[0] + ", " + sa[1]
                                                    + context.getString(R.string.semester) + sa[2]);
                                    String name1 = moduls.get(position);
                                    String[] modulname1 = name1.split(" ");
                                    modulName = "";
                                    int b;
                                    for (b = 0; b < (modulname1.length - 1); b++) {
                                        modulName = (modulName + " " + modulname1[b]);
                                    }

                                    int timeStart
                                            = Integer.valueOf(splitDateAndTime[1].substring(0, 2));
                                    int timeEnd
                                            = Integer.valueOf(splitDateAndTime[1].substring(4, 5));
                                    calDate = new GregorianCalendar(
                                            Integer.valueOf(splitDayMonthYear[0]),
                                            (Integer.valueOf(splitDayMonthYear[1]) - 1),
                                            Integer.valueOf(splitDayMonthYear[2]),
                                            timeStart, timeEnd);

                                    //Methode zum Speichern im Kalender
                                    int calendarid = calendarID(modulName);

                                    //Funktion im Google-Kalender, um PrüfID und calenderID zu speichern
                                    checkEntry.insertCal(Integer.valueOf(planId.get(position)),
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
        AppDatabase database = AppDatabase.getAppDatabase(context);
        List<TestPlanEntry> ppeList1 = database.userDao().getAll();

        //Überprüfung ob Prüfitem Favorisiert wurde und angeklickt
        //Toast.makeText(v.getContext(),String.valueOf(userdaten.size()),
        // Toast.LENGTH_SHORT).show();
        save = false;

        for (TestPlanEntry entry : ppeList1) {
            if ((entry.getID().toString()
                    .equals(planId.get(position)) &
                    (entry.getFavorit()))) {
                save = true;
                break;
                // Toast.makeText(v.getContext(), "129", Toast.LENGTH_SHORT).show();
            }
        }

        return save;
    }

    //Item anzahl
    @Override
    public int getItemCount() {
        return moduls.size();
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
        event.put(CalendarContract.Events.TITLE, modulName);
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