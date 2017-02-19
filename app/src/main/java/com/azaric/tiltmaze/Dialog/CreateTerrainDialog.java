package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.azaric.tiltmaze.NewTerrainActivity;
import com.azaric.tiltmaze.StatisticsActivity;

/**
 * Created by Stefan on 1/16/17 | 00:00.
 * Created in project with name: "Tiltmaze"
 */
public class CreateTerrainDialog extends DialogFragment {

    NewTerrainActivity myActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myActivity = (NewTerrainActivity) getActivity();

        builder.setMessage("Da li zelite da sačuvate trenutni poligon?")
                .setTitle("Brisanje jedne statistike")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        if(myActivity.isSavePossible())
                            myActivity.save();
                        else
                            Toast.makeText(myActivity,"Nije moguće da se sačuva! Ubacite sve elemente.",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
