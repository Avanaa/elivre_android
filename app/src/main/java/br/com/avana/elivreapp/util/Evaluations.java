package br.com.avana.elivreapp.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.model.Avaliacao;

public class Evaluations {

    public static List<Avaliacao> getEvaluationlist(Context context){

        List<Avaliacao> avaliacoes = new ArrayList<Avaliacao>();
        avaliacoes.add(new Avaliacao(Avaliacao.ANGRY_FACE, context.getString(R.string.evaluation_angry)));
        avaliacoes.add(new Avaliacao(Avaliacao.NEUTRAL_FACE, context.getString(R.string.evaluation_neutral)));
        avaliacoes.add(new Avaliacao(Avaliacao.HAPPY_FACE, context.getString(R.string.evaluation_happy)));
        avaliacoes.add(new Avaliacao(Avaliacao.FREE, context.getString(R.string.evaluation_free)));

        return avaliacoes;
    }
}
