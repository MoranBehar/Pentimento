package com.example.pentimento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter<Photo> {

    private int photoLayout;

    public PhotoAdapter(Context appContext, ArrayList<Photo> gallery, int photoLayout) {
        super(appContext, 0, gallery);
        this.photoLayout = photoLayout;
    }


    @NonNull
    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater
                    .from(getContext())
                    .inflate(photoLayout, parent, false);
        }

        Photo currentPhotoPosition = getItem(position);
        ImageView newImage = currentItemView.findViewById(R.id.ivNewImage);

        if (currentPhotoPosition.getPhoto() != null) {
            newImage.setImageBitmap(currentPhotoPosition.getPhoto());
        }

        if (currentPhotoPosition instanceof SharedPhoto) {
            TextView sharedBy = currentItemView.findViewById(R.id.tvSharedBy);
            sharedBy.setText(((SharedPhoto) currentPhotoPosition).getSharedBy().getName());

            TextView sharedOn = currentItemView.findViewById(R.id.tvSharedOn);
            sharedOn.setText(((SharedPhoto) currentPhotoPosition).getSharedOn());
        }

        // then return the recyclable view
        return currentItemView;
    }
}
