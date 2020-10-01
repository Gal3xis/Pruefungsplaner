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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    public List<String> studiengangList;
    public List<Boolean> auswahlList;

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

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nameStudiengang.setText(studiengangList.get(position));

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