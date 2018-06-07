package br.com.avana.elivreapp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.model.Avaliacao;

public class EvaluationAdapter extends BaseAdapter {

    private List<Avaliacao> avaliacoes;
    private Activity activity;

    public EvaluationAdapter(List<Avaliacao> avaliacoes, Activity activity) {
        this.avaliacoes = avaliacoes;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return avaliacoes.size();
    }

    @Override
    public Avaliacao getItem(int position) {
        return avaliacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = activity.getLayoutInflater().inflate(R.layout.item_evaluation, parent, false);

        Avaliacao item = getItem(position);
        ImageView img = convertView.findViewById(R.id.item_evaluation_img);
        TextView txt = convertView.findViewById(R.id.item_evaluation_txt);

        switch (item.getAvaliacao()){

            case Avaliacao.ANGRY_FACE:
                img.setImageResource(R.drawable.ic_angry_face_color);
                txt.setText(item.getDescricao());
                break;
            case Avaliacao.NEUTRAL_FACE:
                img.setImageResource(R.drawable.ic_poker_face_color);
                txt.setText(item.getDescricao());
                break;
            case Avaliacao.HAPPY_FACE:
                img.setImageResource(R.drawable.ic_laughing_face_color);
                txt.setText(item.getDescricao());
                break;
            case Avaliacao.FREE:
                img.setImageResource(R.drawable.ic_free_color);
                txt.setText(item.getDescricao());
                break;
        }

        return convertView;
    }
}
