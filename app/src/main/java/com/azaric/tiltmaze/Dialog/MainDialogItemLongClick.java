package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.azaric.tiltmaze.MainActivity;
import com.azaric.tiltmaze.StatisticsActivity;

/**
 * Created by Stefan on 1/16/17.
 */
public class MainDialogItemLongClick extends DialogFragment {

    MainActivity myActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myActivity = (MainActivity)getActivity();

        builder.setMessage("Da li zelite da obrisete poligon: " + myActivity.getPolygonName() + "?")
                .setTitle("Brisanje poligona")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myActivity
                                .deletePolygon(myActivity.getPolygonName());
                        myActivity.updateListView();
                        Toast.makeText(
                                myActivity,
                                "Uspešno obrisan poligon: " + myActivity.getPolygonName(),
                                Toast.LENGTH_LONG)
                                .show();
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
