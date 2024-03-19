package com.example.pentimento;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class AlbumListAdapter extends AlbumAdapter {
    private List<Album> albumList;

    public AlbumListAdapter(Context context, List<Album> albums) {
        super(context, albums);
        this.albumList = albums;
    }

    @NonNull
    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        Album album = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_item_add_list, parent, false);
        }

        // Lookup view for data population
        ImageView ivAlbumCover = convertView.findViewById(R.id.ivAlbumCover);
        TextView tvAlbumName = convertView.findViewById(R.id.tvAlbumName);

        // Fill the data into the template view using the data object
        tvAlbumName.setText(album.getTitle());

        album.getCoverImage(new Album.getCoverImageResult() {
            @Override
            public void onImageReady(Bitmap bitmap) {
                ivAlbumCover.setImageBitmap(bitmap);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}
