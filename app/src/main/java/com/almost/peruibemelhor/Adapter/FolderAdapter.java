package com.almost.peruibemelhor.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.almost.peruibemelhor.Activitys.chat.ChatActivity;
import com.almost.peruibemelhor.Entity.Folder;
import com.almost.peruibemelhor.R;
import com.almost.peruibemelhor.Util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private final String TAG = "FoldersAdapter";
    private User nr = new User();

    private final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
    private final String id_user = nr.getUserUID();

    private final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dataPreferences = database.child("User").child(id_user).child("following");

    Context context;
    ArrayList<Folder> folders;

    public FolderAdapter(Context c, ArrayList<Folder> f){
        context = c;
        folders = f;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_folder, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(folders.get(position).getTitle());
        holder.description.setText(folders.get(position).getDescription());

        Picasso.get().load(folders.get(position).getPhoto()).into(holder.image);

        if(folders.get(position).isFollowing()) {
            holder.follow.setBackgroundResource(R.drawable.follow_button);
            holder.follow.setText(R.string.follow);
        }else{
            holder.follow.setBackgroundResource(R.drawable.unfollow_button);
            holder.follow.setText(R.string.unfollow);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, ChatActivity.class);

                it.putExtra("idFolder", folders.get(position).getId_folder());
                context.startActivity(it);
            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!folders.get(position).isFollowing()){
                    dataPreferences.child(folders.get(position).getId_folder()).setValue(true);
                    folders.get(position).setFollowing(true);
                    notifyItemChanged(position);
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Deixar de Seguir");
                    alertDialogBuilder
                            .setMessage("Pressione SIM para deixar de seguir essa pasta!")
                            .setCancelable(true)
                            .setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dataPreferences.child(folders.get(position).getId_folder()).setValue(false);
                                    folders.get(position).setFollowing(false);
                                    notifyItemChanged(position);
                                }
                            })
                            .setNegativeButton("Não",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        CircleImageView image;

        Button follow;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);

            follow = itemView.findViewById(R.id.follow);
        }
    }
}
