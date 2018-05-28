package br.com.avana.elivreapp.dao;

import android.app.Activity;
import android.nfc.Tag;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.avana.elivreapp.model.PostModel;

public class PostDAO {

    private final DatabaseReference myRef;

    public PostDAO(){

        myRef = FirebaseDatabase.getInstance().getReference("Posts");

        myRef.child("Posts").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PostModel> posts = new ArrayList<PostModel>();
                PostModel post = dataSnapshot.getValue(PostModel.class);
                posts.add(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("WARNING", "loadPost:Cancelled", databaseError.toException());
            }
        });
    }

    public void save(PostModel post){
        if (post.getId() != null){
            update(post);
        } else {
            post.setId(myRef.push().getKey());
            myRef.child(post.getId()).setValue(post);
        }
    }

    public void update(PostModel post){
        if (post.getId() == null){
            return;
        } else {
            myRef.child(post.getId()).setValue(post);
        }
    }

    public void delete(PostModel post){
        if (post.getId() == null){
            return;
        } else {
            myRef.child(post.getId()).removeValue();
        }
    }
}

