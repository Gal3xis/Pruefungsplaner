package com.Fachhochschulebib.fhb.pruefungsplaner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.data.TestPlanEntry;

import java.util.List;


//////////////////////////////
// MyAdapterfavoriten für Recycleview
//
// autor:
// inhalt: Anzeigen der favorisierten Prüfungen in einzelnen Tabellen.
// zugriffsdatum: 11.12.19, Aug. 2020
//
//
//////////////////////////////


public class MyAdapterfavorits extends RecyclerView.Adapter<MyAdapterfavorits.ViewHolder> {
    private List<String> moduleAndCourseList;
    private List<String> examinerAndSemester;
    private List<String> ppIdList;
    private List<String> datesList;
    private List<String> roomList;
    private List<String> formList;
    private String modulName;
    private String name;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterfavorits(List<String> moduleListParam,
                             List<String> courseListParam,
                             List<String> dateListParam,
                             List<String> pruefPlanIdListParam,
                             List<String> roomListParam,
                             List <String> formListParam) {
        moduleAndCourseList = moduleListParam;
        datesList = dateListParam;
        examinerAndSemester = courseListParam;
        ppIdList = pruefPlanIdListParam;
        roomList = roomListParam;
        formList = formListParam;
    }

    public void add(int position, String item) {
        moduleAndCourseList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        moduleAndCourseList.remove(position);
        notifyItemRemoved(position);
        deleteItemThread(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterfavorits.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.favoriten, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        // Merlin Gürtler für den globalen Context
        context = parent.getContext();

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        name = moduleAndCourseList.get(holder.getAdapterPosition());
        holder.txtHeader.setText(name);

        //Prüfitem von der Favoritenliste löschen
        holder.ivicon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        holder.txtFooter.setText(context.getString(R.string.prof) + examinerAndSemester.get(position).toString());
        name = moduleAndCourseList.get(position);
        String[] course = name.split(" ");
        modulName = "";
        int b;
        for (b = 0; b < (course.length - 1); b++) {
            modulName = (modulName + " " + course[b]);

        }

        // Start Merlin Gürtler
        // erhalte den ausgewählten Studiengang
        SharedPreferences sharedPreferencesCourse = context.
                getSharedPreferences("validation", Context.MODE_PRIVATE);
        String selectedCourse[] = sharedPreferencesCourse.
                getString("selectedCourse", "0").split(" ");

        String colorElectiveModule = "#7FFFD4";

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

        // Ende Merlin Gürtler

        //darstellen der Informationen für das Prüfitem
        String[] splitDateAndTime = datesList.get(position).split(" ");
        String[] splitDayMonthYear = splitDateAndTime[0].split("-");
        holder.txtthirdline.setText(context.getString(R.string.clockTime2)
                + splitDateAndTime[1].substring(0, 5).toString()
                + context.getString(R.string.date2)
                + splitDayMonthYear[2].toString() + "."
                + splitDayMonthYear[1].toString() + "."
                + splitDayMonthYear[0].toString());
        final String[] splitExaminerAndSemester
                = examinerAndSemester.get(position).split(" ");
        holder.txtFooter.setText(context.getString(R.string.prof)
                + splitExaminerAndSemester[0] + ", " + splitExaminerAndSemester[1]
                + context.getString(R.string.semester) + splitExaminerAndSemester[2]);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return moduleAndCourseList.size();
    }

    public String giveString(int position) {

        try {
            String name = moduleAndCourseList.get(position);
            String[] course = name.split(" ");
            modulName = "";
            int b;
            for (b = 0; b < (course.length - 1); b++) {
                modulName = (modulName + " " + course[b]);

            }

            String[] division1 = datesList.get(position).split(" ");
            String[] division2 = division1[0].split("-");
            //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
            final String[] sa = examinerAndSemester.get(position).split(" ");
            //AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
            String s = (context.getString(R.string.information) +
                    context.getString(R.string.course) + course[course.length - 1] +
                    context.getString(R.string.modul) + modulName +
                    context.getString(R.string.firstProf) + sa[0] +
                    context.getString(R.string.secondProf) + sa[1] +
                    context.getString(R.string.date) + division2[2].toString() + "."
                    + division2[1].toString() + "."
                    + division2[0].toString() +
                    context.getString(R.string.clockTime) + division1[1].substring(0, 5).toString() +
                    context.getString(R.string.clock) +
                    context.getString(R.string.room) + roomList.get(position) +
                    context.getString(R.string.form) + formList.get(position) + "\n " + "\n \n \n \n \n \n ");

            return (s);
        } catch (Exception e) {
            Log.d("Fehler Adapterfavorits",
                    "Fehler bei ermittlung der weiteren Informationen");

        }
        return ("0"); //????
    }

    // Start Merlin Gürtler
    // da die Funktion mehrmals genutzt wird ausglagern in Funktion
    private void deleteItemThread(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getAppDatabase(context);
                List<TestPlanEntry> ppeList = database.userDao().getFavorites(true);
                // second parameter is necessary ie.,
                // Value to return if this preference does not exist.
                for (TestPlanEntry entry : ppeList) {
                    if (entry.getID().equals(ppIdList.get(position))) {
                        database.userDao()
                                .update(false, Integer.valueOf(ppIdList.get(position)));

                        //Entferne den Eintrag aus dem Calendar falls vorhanden
                        CheckGoogleCalendar cal = new CheckGoogleCalendar();
                        cal.setCtx(context);
                        if (!cal.checkCal(Integer.valueOf(ppIdList.get(position)))) {
                            cal.deleteEntry(Integer.valueOf(ppIdList.get(position)));
                        }

                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
                                context.getString(R.string.delete), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    // Ende Merlin Gürtler

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public Integer zahl1;
        public TextView txtthirdline;
        public ImageView ivicon;
        public LinearLayout layout2;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            ivicon = (ImageView) v.findViewById(R.id.icon);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            txtthirdline = (TextView) v.findViewById(R.id.thirdLine);
            layout2 = (LinearLayout) v.findViewById(R.id.linearLayout);
            //button.setLayoutParams(new LinearLayout.LayoutParams(
            // LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        }
    }
}