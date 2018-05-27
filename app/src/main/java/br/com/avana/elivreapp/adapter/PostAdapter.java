package br.com.avana.elivreapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.adapter.holder.PostHolder;
import br.com.avana.elivreapp.model.PostModel;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private List<PostModel> posts;

    public PostAdapter(List<PostModel> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        PostModel post = this.posts.get(position);

        //Geocoder geo = new Geocoder(context, Locale.getDefault());

        //holder.setLocal();
        holder.setData(post.getDataHora().toString());
        holder.setUsuario(post.getUsuario());

        switch(post.getAvaliacao()){

            case FELIZ:
                holder.setImage(R.drawable.ic_happy_face);
                break;

            case CU:
                holder.setImage(R.drawable.ic_poker_face);
                break;

            case PUTO:
                holder.setImage(R.drawable.ic_angry_face);
                break;

            case LIVRE:
                holder.setImage(R.drawable.ic_free);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.posts != null ? this.posts.size() : 0;
    }
}
