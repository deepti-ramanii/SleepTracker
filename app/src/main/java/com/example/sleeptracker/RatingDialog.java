package com.example.sleeptracker;

import androidx.appcompat.app.AppCompatDialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;

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
               .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        listener.applyRating(-1);
                    }
               })
               .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       listener.applyRating(-1);
                   }
               })
               .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       if(ratingInput.getText().toString().length() < 1) {
                           listener.applyRating(0);
                       } else {
                           int rating = Integer.parseInt(ratingInput.getText().toString());
                           listener.applyRating(Math.min(rating, 10));
                       }
                   }
               });

        ratingInput = view.findViewById(R.id.rating);
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        return alert;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RatingDialogListener) context;
    }

    public interface RatingDialogListener {
        void applyRating(int rating);
    }
}
