package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.azaric.tiltmaze.NewTerrainActivity;
import com.azaric.tiltmaze.R;

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

        builder.setMessage(R.string.Dialog_create_terrain)
                .setTitle(R.string.Dialog_create_terrain_title)
                .setPositiveButton(R.string.Dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        if(myActivity.isSavePossible())
                            myActivity.save();
                        else
                            Toast.makeText(myActivity, R.string.Dialog_create_terrain_toast,Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.Dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
