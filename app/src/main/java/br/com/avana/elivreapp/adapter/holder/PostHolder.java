package br.com.avana.elivreapp.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.avana.elivreapp.R;

public class PostHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView local;
    private TextView data;
    private TextView usuario;

    public PostHolder(View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.item_img);
        this.local = itemView.findViewById(R.id.item_local);
        this.data = itemView.findViewById(R.id.item_data);
        this.usuario = itemView.findViewById(R.id.item_usuario_nome);
    }

    public void setImage(int resId) {
        this.image.setImageResource(resId);
    }

    public void setLocal(String local) {
        this.local.setText(local);
    }

    public void setData(String data) {
        this.data.setText(data);
    }

    public void setUsuario(String usuario) {
        this.usuario.setText(usuario);
    }
}
