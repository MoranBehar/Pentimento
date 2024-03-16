package com.example.pentimento;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AlbumsFragment extends Fragment {

    private GridView gvAlbums;
    private AlbumAdapter adapter;
    private ArrayList<Album> albums;

   private DBManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        initAlbums(view);

        return view;
    }

    private void initAlbums(View view){

        albums = new ArrayList<Album>();
        dbManager = DBManager.getInstance();
        adapter = new AlbumAdapter(getContext(), albums);

        dbManager.getUserAlbums(new DBActionResult<ArrayList>() {
            @Override
            public void onSuccess(ArrayList data) {
                albums.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {

            }
        });


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
        //TODO - set album as position in an array and set the all album as defalt,
        // others will be opened by a click

        Intent intent = new Intent(this.getContext(), GalleryFragment.class);
        intent.putExtra("albumPosition", position);
        startActivity(intent);
    }
}