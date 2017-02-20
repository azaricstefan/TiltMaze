package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.azaric.tiltmaze.MainActivity;
import com.azaric.tiltmaze.R;
import com.azaric.tiltmaze.StatisticsActivity;

/**
 * Created by Stefan on 1/16/17 | 00:00.
 * Created in project with name: "Tiltmaze"
 */
public class MainDialogItemLongClick extends DialogFragment {

    MainActivity myActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myActivity = (MainActivity)getActivity();

        builder.setMessage(getString(R.string.MainDialogItemLongClick_message) + myActivity.getPolygonName() + "?")
                .setTitle(R.string.MainDialogItemLongClick_Deleted_title)
                .setPositiveButton(R.string.Dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myActivity
                                .deletePolygon(myActivity.getPolygonName());
                        myActivity.updateListView();
                        Toast.makeText(
                                myActivity,
                                getString(R.string.MainDialogItemLongClick_Deleted) + myActivity.getPolygonName(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton(R.string.Dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        //getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
