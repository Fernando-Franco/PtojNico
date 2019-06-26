package com.almost.peruibemelhor.Model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.almost.peruibemelhor.Entity.Folder;
import com.almost.peruibemelhor.Util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoldersViewModel extends ViewModel {
    private final String TAG = "FoldersViewModel";
    private User nr = new User();

    private final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private final String id_user = nr.getUserUID();

    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference UserRef = database.child("User");
    final DatabaseReference FolderRef = database.child("Folder");

    private MutableLiveData<ArrayList<Folder>> livedataFolders;
    private MutableLiveData<ArrayList<String>> livedatafollowFolders;

    List<Folder> folders;
    List<String> followIdFolders;
    List<Folder> followFolders;

    public LiveData<ArrayList<Folder>> getFolderList() {
        if (livedataFolders == null) {
            livedataFolders = new MutableLiveData<>();
            loadFolders();
        }
        return livedataFolders;
    }

    public LiveData<ArrayList<String>> getFollowFolderList() {
        if (livedatafollowFolders == null) {
            livedatafollowFolders = new MutableLiveData<ArrayList<String>>();
            getFollowing();
        }
        return livedatafollowFolders;
    }

    private void loadFolders() {
        // do async operation to fetch users
        FolderRef.keepSynced(true);
        FolderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                folders = new ArrayList<>();

                for(DataSnapshot dataSnapshotl : dataSnapshot.getChildren()){
                    Folder folder = dataSnapshotl.getValue(Folder.class);
                    folders.add(folder);
                }

                livedataFolders.setValue((ArrayList<Folder>) folders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "FirebaseError: "+databaseError.getMessage());
            }
        });
    }

    private void getFollowing() {
        // Get User ID Folders Following
        DatabaseReference followingRef = UserRef.child(id_user).child("following");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followIdFolders = new ArrayList<>();

                for(DataSnapshot dataSnapshotl : dataSnapshot.getChildren()){
                    Boolean isFollow = dataSnapshotl.getValue(Boolean.class);
                    if(isFollow) {
                        followIdFolders.add(dataSnapshotl.getKey());
                    }
                }

                livedatafollowFolders.setValue((ArrayList<String>) followIdFolders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "FirebaseError: "+databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
