package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.azaric.tiltmaze.StatisticsActivity;

/**
 * Created by Stefan on 1/16/17 | 00:00.
 * Created in project with name: "Tiltmaze"
 */
public class StatisticsDialogResetSingle extends DialogFragment {

    StatisticsActivity myActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myActivity = (StatisticsActivity)getActivity();

        builder.setMessage("Da li zelite da obrisete statistiku?")
                .setTitle("Brisanje jedne statistike")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myActivity
                                .getDbOperationsHelper()
                                .resetStatistic(myActivity.getNameOfPolygon());
                        getActivity().finish();
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
