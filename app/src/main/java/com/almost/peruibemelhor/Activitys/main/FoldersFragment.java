package com.almost.peruibemelhor.Activitys.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almost.peruibemelhor.Adapter.FolderAdapter;
import com.almost.peruibemelhor.Entity.Folder;
import com.almost.peruibemelhor.Model.FoldersViewModel;
import com.almost.peruibemelhor.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FoldersFragment extends Fragment {
    private final String TAG = "FoldersFragment";

    private RecyclerView recyclerView;

    private ArrayList<Folder> folders = new ArrayList<>();
    private ArrayList<Folder> following = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_folders, container, false);

        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerViewAllFolders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FoldersViewModel model = ViewModelProviders.of(this).get(FoldersViewModel.class);
        model.getFolderList().observe(this, folderList -> {
            // update UIasd
            folders = folderList;
            updateUI();
        });

        model.getFollowFolderList().observe(this, followList -> {
            for (Folder folder : folders) {
                folder.setFollowing(false);
                for (String followFolder : followList) {
                    if (folder.getId_folder().equals(followFolder)) {
                        folder.setFollowing(true);
                    }
                }
            }
            updateUI();
        });

        return root;
    }

    private void updateUI(){
        FolderAdapter adapterFolder = new FolderAdapter(getContext(), folders);
        recyclerView.setAdapter(adapterFolder);
    }
}
