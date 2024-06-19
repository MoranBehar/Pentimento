package com.example.pentimento;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class AlbumsFragment extends Fragment {

    private static final String TAG = "AlbumsFragment";
    private GridView gvAlbums;
    private AlbumAdapter adapter;

    private AlbumsManager alManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        initAlbums(view);

        return view;
    }

    private void initAlbums(View view){

        alManager = AlbumsManager.getInstance();

        adapter = new AlbumAdapter(getContext(), alManager.getAlbumsList());

        gvAlbums = view.findViewById(R.id.gvAlbums);
        gvAlbums.setAdapter(adapter);
        gvAlbums.setOnItemClickListener(albumSelectedEvent());
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
        Intent intent = new Intent(this.getContext(), AlbumPhotosActivity.class);
        Album clickedAlbum = (Album) gvAlbums.getItemAtPosition(position);
        intent.putExtra("albumId", clickedAlbum.getId());

        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}