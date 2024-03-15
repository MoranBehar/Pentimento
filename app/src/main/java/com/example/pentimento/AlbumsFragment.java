package com.example.pentimento;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class AlbumsFragment extends Fragment {

    private GridView gvAlbums;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        gvAlbums = view.findViewById(R.id.gvAlbums);
        gvAlbums.setOnItemClickListener(albumSelectedEvent());

        return view;
    }


    private  AdapterView.OnItemClickListener albumSelectedEvent() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                albumClicked(position);
            }
        };
    }

    private void albumClicked(int position) {

        Intent intent = new Intent(this.getContext(), GalleryFragment.class);
        intent.putExtra("albumPosition", position);
        startActivity(intent);
    }
}