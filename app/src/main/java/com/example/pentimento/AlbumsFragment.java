package com.example.pentimento;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class AlbumsFragment extends Fragment {

    private GridView gvAlbums;
    private AlbumAdapter adapter;
    private ArrayList<Album> albums;

    private AlbumsManager alManager;

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
        alManager = AlbumsManager.getInstance();

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

        AlbumPhotosFragment albumPhotosFragment = new AlbumPhotosFragment();

        // Create a Bundle object to pass the album position
        Bundle args = new Bundle();
        args.putString("albumPosition", String.valueOf(position));
        albumPhotosFragment.setArguments(args);

        // Transition to the album photos
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, albumPhotosFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }
}