package br.com.avana.elivreapp.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.avana.elivreapp.model.PostModel;

public class PostDAO {

    private final DatabaseReference myRef;

    public PostDAO(){
        myRef = FirebaseDatabase.getInstance().getReference("Posts");
    }

    public void save(PostModel post){
        if (post.getId() != null){
            update(post);
        } else {
            myRef.push().setValue(post);
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

