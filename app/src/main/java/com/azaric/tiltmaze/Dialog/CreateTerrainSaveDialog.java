package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.azaric.tiltmaze.GameActivity;
import com.azaric.tiltmaze.MainActivity;
import com.azaric.tiltmaze.NewTerrainActivity;
import com.azaric.tiltmaze.StatisticsActivity;


/**
 * Created by Stefan on 15-Jan-17 | 23:28.
 * Created in project with name: "Tiltmaze"
 */

public class CreateTerrainSaveDialog extends DialogFragment {

    EditText input;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        input =  new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(lp);

        builder.setMessage("Unesi ime poligona:")
                .setTitle("Dialog za čuvanje poligona")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((NewTerrainActivity)getActivity())
                                .getPolygon()
                                .savePolygon(input.getText().toString(), getActivity());
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        //getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
