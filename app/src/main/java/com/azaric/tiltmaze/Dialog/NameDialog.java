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

/**
 * Created by Stefan on 1/16/17.
 */
public class NameDialog extends DialogFragment {

    EditText playerName;

    GameActivity myActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        myActivity = (GameActivity) getActivity();

        playerName =  new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        playerName.setLayoutParams(lp);

        builder.setMessage("Unesite vaše ime:")
                .setTitle("Pobedili ste!")
                .setView(playerName)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myActivity
                                .getDbOperationsHelper()
                                .insert(
                                        playerName.getText().toString(),
                                        myActivity.getController().getNameOfPolygonToLoad(),
                                        myActivity.getScore());
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
