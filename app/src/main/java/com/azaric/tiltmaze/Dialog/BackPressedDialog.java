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


/**
 * Created by Stefan on 15-Jan-17.
 */

public class BackPressedDialog extends DialogFragment {

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

        builder.setMessage("Unesi ime crteža:")
                .setTitle("Dialog za čuvanje crteža")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((GameActivity)getActivity())
                                .getController()
                                .saveDrawing(input.getText().toString(), getActivity());
                        getActivity().finish();
                        //TODO: update ListView sa novim crtezom!
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
