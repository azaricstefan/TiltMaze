package com.azaric.tiltmaze.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.azaric.tiltmaze.GameActivity;
import com.azaric.tiltmaze.MainActivity;
import com.azaric.tiltmaze.NewTerrainActivity;
import com.azaric.tiltmaze.R;
import com.azaric.tiltmaze.StatisticsActivity;

/**
 * Created by Stefan on 1/16/17 | 00:00.
 * Created in project with name: "Tiltmaze"
 */
public class GameNameDialog extends DialogFragment {

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

        playerName.setText(getDefaultName());

        final float score = myActivity.getScore();
        builder.setMessage(R.string.GameNameMessage)
                .setTitle("You have won!" + "SCORE: " + score)
                .setView(playerName)
                .setPositiveButton(R.string.Dialog_OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        myActivity
                                .getDbOperationsHelper()
                                .insert(
                                        playerName.getText().toString(),
                                        filterTmp(myActivity.getController().getNameOfPolygonToLoad()),
                                        score);
                        getActivity().finish();
                        startStatisticsActivity();
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

    public void startStatisticsActivity(){
        String polygonName = filterTmp(myActivity.getController().getNameOfPolygonToLoad());
        Intent intent = new Intent(myActivity, StatisticsActivity.class);
        intent.putExtra(StatisticsActivity.STATISTICS_SINGLE_TRACK,polygonName);
        startActivity(intent);
    }

    public String filterTmp(String name){
        String[] full = name.split(":");
        if(full.length > 1)
            return full[1];
        return name;
    }

    public String getDefaultName(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(myActivity);
        return sharedPreferences.getString(myActivity.getString(R.string.preference_player_name), getString(R.string.dummy_name));
    }
}
