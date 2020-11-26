package com.example.sleeptracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RatingDialog extends AppCompatDialogFragment {
    private EditText ratingInput;
    private RatingDialogListener listener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rating_dialog, null);

        builder.setView(view)
               .setTitle("Rate your sleep?")
               .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                   }
               })
               .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       int rating = Integer.parseInt(ratingInput.getText().toString());
                       listener.apply(rating);
                   }
               });

        ratingInput = view.findViewById(R.id.rating);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RatingDialogListener) context;
    }

    public interface RatingDialogListener {
        void apply(int rating);
    }
}
