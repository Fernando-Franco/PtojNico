package com.almost.peruibemelhor.Activitys.chat;

import android.content.Intent;
import android.os.Bundle;

import com.almost.peruibemelhor.Activitys.main.FoldersFragment;
import com.almost.peruibemelhor.Activitys.main.FollowFragment;
import com.almost.peruibemelhor.Adapter.ViewPagerAdapter;
import com.almost.peruibemelhor.Entity.Folder;
import com.almost.peruibemelhor.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChatActivity";

    private String currentFolder;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    ImageButton folder_close;
    CircleImageView folder_image;
    TextView folder_title;

    DatabaseReference refChat;
    DatabaseReference refFolder;

    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        folder_close = findViewById(R.id.folderClose);
        folder_image = findViewById(R.id.folderImage);
        folder_title = findViewById(R.id.folderTitle);

        folder_close.setOnClickListener(this);

        it = getIntent();
        currentFolder = it.getStringExtra("idFolder");
        Log.w(TAG, "CurrentFolder: "+currentFolder);


        refFolder = FirebaseDatabase.getInstance().getReference("Folder").child(currentFolder);
        refFolder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Folder folder = dataSnapshot.getValue(Folder.class);
                assert folder != null;
                folder_title.setText(folder.getTitle());
                Picasso.get().load(folder.getPhoto()).into(folder_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setTabs();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.folderClose:
                finish();
        }
    }

    private void setTabs() {
        mTabLayout = (TabLayout)findViewById(R.id.tabsChat);
        mViewPager = (ViewPager)findViewById(R.id.viewpagerChat);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatFragment(), "Chat");
        viewPagerAdapter.addFragment(new ProfileFolderFragment(currentFolder), "Pasta");
        viewPagerAdapter.addFragment(new BlankFragment(), "Ações");

        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
