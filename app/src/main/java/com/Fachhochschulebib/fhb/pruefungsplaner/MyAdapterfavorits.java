package com.Fachhochschulebib.fhb.pruefungsplaner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.Fachhochschulebib.fhb.pruefungsplaner.data.PruefplanEintrag;

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
    private List<String> moduleUndStudiengangsList;
    private List<String> prueferUndSemesterList;
    private List<String> ppIdList;
    private List<String> datumsList;
    private List<String> raumList;
    private String studiengang;
    private String name;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterfavorits(List<String> moduleListParam,
                             List<String> studiengangListParam,
                             List<String> datumsListParam,
                             List<String> pruefPlanIdListParam,
                             List<String> raumListParam) {
        moduleUndStudiengangsList = moduleListParam;
        datumsList = datumsListParam;
        prueferUndSemesterList = studiengangListParam;
        ppIdList = pruefPlanIdListParam;
        raumList = raumListParam;
    }

    public void add(int position, String item, String studiengang) {
        moduleUndStudiengangsList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        moduleUndStudiengangsList.remove(position);
        notifyItemRemoved(position);
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
        name = moduleUndStudiengangsList.get(holder.getAdapterPosition());
        holder.txtHeader.setText(name);
        //Prüfitem von der Favoritenliste löschen
        holder.ivicon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase datenbank =  AppDatabase.getAppDatabase(v.getContext());
                List<PruefplanEintrag> ppeList = datenbank.userDao().getAll2();
                // second parameter is necessary ie.,
                // Value to return if this preference does not exist.
                for (PruefplanEintrag eintrag: ppeList){
                    if(eintrag.getFavorit()){
                        if(eintrag.getID().equals(ppIdList.get(position))){
                            datenbank.userDao()
                                     .update(false,Integer.valueOf(ppIdList.get(position)));
                            remove(holder.getAdapterPosition());

                            // Start Merlin Gürtler
                            Toast.makeText(v.getContext(), "Entfernt", Toast.LENGTH_SHORT).show();
                            // Ende Merlin Gürtler

                        }
                    }
                }
            }
        });

        holder.txtFooter.setText("Prüfer: " + prueferUndSemesterList.get(position).toString());
        name = moduleUndStudiengangsList.get(position);
        String[] modulname = name.split(" ");
        studiengang = "";
        int b;
        for (b = 0; b < (modulname.length - 1); b++) {
            studiengang = (studiengang + " " + modulname[b]);

        }

        // Start Merlin Gürtler
        // erhalte den ausgewählten Studiengang
        SharedPreferences sharedPrefSelectedStudiengang = context.
                getSharedPreferences("validation",Context.MODE_PRIVATE);
        String selectedStudiengang [] = sharedPrefSelectedStudiengang.
                getString("selectedStudiengang","0").split(" ");

        String colorElectiveModule = "#7FFFD4";

        if(!selectedStudiengang[selectedStudiengang.length - 1].equals(modulname[modulname.length - 1]))
        {
            // Lege die Farben für die Wahlmodule fest
            GradientDrawable backGroundGradient = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {Color.parseColor(colorElectiveModule),
                            Color.parseColor(colorElectiveModule)});
            backGroundGradient.setCornerRadius(40);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.layout.setBackgroundDrawable(backGroundGradient);
            } else {
                holder.layout.setBackground(backGroundGradient);
            }
        }

        // Ende Merlin Gürtler

        //darstellen der Informationen für das Prüfitem
        String[] splitDatumUndUhrzeit = datumsList.get(position).split(" ");
        String[] splitTagMonatJahr = splitDatumUndUhrzeit[0].split("-");
        holder.txtthirdline.setText("Uhrzeit: "
                                    + splitDatumUndUhrzeit[1].substring(0, 5).toString()
                                    + " Datum: "
                                    + splitTagMonatJahr[2].toString() + "."
                                    + splitTagMonatJahr[1].toString() + "."
                                    + splitTagMonatJahr[0].toString());
        final String[] splitPrueferUndSemester
                = prueferUndSemesterList.get(position).split(" ");
        holder.txtFooter.setText("Prüfer: "
                                + splitPrueferUndSemester[0] + ", " + splitPrueferUndSemester[1]
                                + "  Semester: " + splitPrueferUndSemester[2]);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return moduleUndStudiengangsList.size();
    }

    public String giveString(int position) {

        try {
            String name = moduleUndStudiengangsList.get(position);
            String[] modulname = name.split(" ");
            studiengang = "";
            int b;
            for (b = 0; b < (modulname.length - 1); b++) {
                studiengang = (studiengang + " " + modulname[b]);

            }

            String[] aufteilung1 = datumsList.get(position).split(" ");
            String[] aufteilung2 = aufteilung1[0].split("-");
            //holder.txtthirdline.setText("Uhrzeit: " + aufteilung1[1].substring(0, 5).toString());
            final String[] sa = prueferUndSemesterList.get(position).split(" ");
            //AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
            String s = ("Informationen zur Prüfung \n \n " +
                        "Studiengang: " + modulname[modulname.length - 1] +
                        "\n Modul: " + studiengang +
                        "\n Erstprüfer: " + sa[0] +
                        " \n Zweitprüfer: " + sa[1] +
                        "\n Datum: " + aufteilung2[2].toString() + "."
                                     + aufteilung2[1].toString() + "."
                                     + aufteilung2[0].toString() +
                        " \n Uhrzeit: " + aufteilung1[1].substring(0, 5).toString() +" Uhr" +
                        " \n Raum: "+ raumList.get(position) +"\n "+ "\n \n \n \n \n \n ");

            return (s);
        }catch(Exception e){
            Log.d("Fehler Adapterfavorits",
                    "Fehler bei ermittlung der weiteren Informationen");

        }
        return ("0"); //????
    }

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