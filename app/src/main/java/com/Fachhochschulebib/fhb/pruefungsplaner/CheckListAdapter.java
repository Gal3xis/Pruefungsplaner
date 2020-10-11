package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// CheckListAdapter
//
// autor:
// inhalt:  erstellt die Checkliste für die Wahl der Studiengänge
// zugriffsdatum: 01.10.20
//
//
//////////////////////////////

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    public List<String> studiengangList;
    public List<Boolean> auswahlList;
    String selectedStudiengang;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CheckListAdapter(List<String> studiengaenge,
                     List<Boolean> ausgewaehlt) {
        studiengangList = studiengaenge;
        auswahlList = ausgewaehlt;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CheckListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.checkliste, parent, false);
        ViewHolder vh = new ViewHolder(v);

        context = v.getContext();

        SharedPreferences sharedPrefSelectedStudiengang = context.
                getSharedPreferences("validation", MODE_PRIVATE);
        selectedStudiengang  = sharedPrefSelectedStudiengang.
                getString("selectedStudiengang","0");

        return vh;
    }

    private boolean addFavorite(int position) {
        if(!studiengangList.get(position).equals(selectedStudiengang)) {
            auswahlList.set(position,
                    !auswahlList.get(position));
        } else {
            Toast.makeText(context, context.getString(R.string.favorite_main_course),
                    Toast.LENGTH_SHORT).show();
        }

        return auswahlList.get(position);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Initialisierung der Komponenten
        holder.nameStudiengang.setText(studiengangList.get(position));
        holder.checkBoxStudiengang.setChecked(auswahlList.get(position));

        holder.checkBoxStudiengang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Speichere die auswahl in der Liste
                holder.checkBoxStudiengang.setChecked(addFavorite(position));
            }
        });

        holder.nameStudiengang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Speichere die auswahl in der Liste
                holder.checkBoxStudiengang.setChecked(addFavorite(position));
            }
        });

    }

    //Item anzahl
    @Override
    public int getItemCount() {
        return studiengangList.size();
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
        private TextView nameStudiengang;
        private CheckBox checkBoxStudiengang;

        private ViewHolder(View v) {
            super(v);
            nameStudiengang = (TextView) v.findViewById(R.id.studiengangName);
            checkBoxStudiengang = (CheckBox) v.findViewById(R.id.checkBox);
        }
    }
}