package com.almost.peruibemelhor.Activitys.chat;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almost.peruibemelhor.Entity.Folder;
import com.almost.peruibemelhor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFolderFragment extends Fragment {
    private String currentFolder;

    private CircleImageView image;
    private TextView title;


    public ProfileFolderFragment(String id) {
        currentFolder = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_folder, container, false);

        image = root.findViewById(R.id.ProfileFolderImage);
        title = root.findViewById(R.id.ProfileFolderTitle);

        DatabaseReference refFolder =
                FirebaseDatabase.getInstance().getReference("Folder").child(currentFolder);
        refFolder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Folder folder = dataSnapshot.getValue(Folder.class);
                assert folder != null;
                title.setText(folder.getTitle());
                Picasso.get().load(folder.getPhoto()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

}
