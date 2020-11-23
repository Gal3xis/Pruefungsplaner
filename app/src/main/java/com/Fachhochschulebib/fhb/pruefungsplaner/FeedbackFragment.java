package com.Fachhochschulebib.fhb.pruefungsplaner;

//////////////////////////////
// TerminefragmentSuche
//
//
//
// autor:
// inhalt:  Ermöglicht die Suche nach Wahlmodulen und zur darstelllung an den Recycleview adapter übergeben
// zugriffsdatum: 01.09.20
//
//
//
//
//
//
//////////////////////////////

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Fachhochschulebib.fhb.pruefungsplaner.data.AppDatabase;
import com.Fachhochschulebib.fhb.pruefungsplaner.model.RetrofitConnect;

import static android.content.Context.MODE_PRIVATE;

public class FeedbackFragment extends Fragment {
    SharedPreferences mSharedPreferencesPPServerAdress;
    String serverAddress, relativePPlanURL;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.feedback, container, false);

        // Die UI Komponenten
        final RatingBar ratingBarUsability = v.findViewById(R.id.ratingBarUsability);
        final RatingBar ratingBarStability = v.findViewById(R.id.ratingBarStability);
        final RatingBar ratingBarFunctions = v.findViewById(R.id.ratingBarFuntions);
        final TextView feedBackInput = v.findViewById(R.id.feedBackInput);
        final Button buttonSend = v.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSharedPreferencesPPServerAdress
                        =  v.getContext().getSharedPreferences("Server_Address", MODE_PRIVATE);

                serverAddress
                        = mSharedPreferencesPPServerAdress.getString("ServerIPAddress", "0");

                relativePPlanURL
                        = mSharedPreferencesPPServerAdress.getString("ServerRelUrlPath", "0");
                //retrofit auruf
                RetrofitConnect retrofit = new RetrofitConnect(relativePPlanURL);

                // Initialisiere die Datenbank
                AppDatabase datenbank = AppDatabase.getAppDatabase(v.getContext());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Übergebe die Daten an Retrofit
                        retrofit.sendFeedBack(v.getContext(), datenbank, serverAddress,
                                ratingBarUsability.getRating(),ratingBarFunctions.getRating(),
                                ratingBarStability.getRating(),feedBackInput.getText().toString());

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // Sende eine Nachricht nachdem senden des Feedbacks
                                Toast.makeText(v.getContext(),
                                        v.getContext().getString(R.string.sendedFeedBack),
                                        Toast.LENGTH_SHORT).show();

                                Intent mainWindow = new Intent(v.getContext(), table.class);
                                startActivity(mainWindow);
                            }
                        });
                    }
                }).start();
            }
        });
        return v;
    }
}
