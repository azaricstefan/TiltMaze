package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.azaric.tiltmaze.GameActivity;
import com.azaric.tiltmaze.R;
import com.azaric.tiltmaze.StatisticsActivity;

/**
 * Created by Stefan on 1/16/17 | 00:00.
 * Created in project with name: "Tiltmaze"
 */
public class StatisticsDialogResetAll extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setMessage(R.string.stats_dialog_delete_all_stats_msg)
                .setTitle(R.string.stats_dialog_delete_all_stats_title)
                .setPositiveButton(R.string.Dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((StatisticsActivity)getActivity())
                                .getDbOperationsHelper()
                                .resetAllStatistics();
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.Dialog_Cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
