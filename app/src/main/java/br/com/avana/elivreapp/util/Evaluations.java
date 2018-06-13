package br.com.avana.elivreapp.util;

import java.util.ArrayList;
import java.util.List;

import br.com.avana.elivreapp.model.Avaliacao;

public class Evaluations {

    public static List<Avaliacao> getEvaluationlist(){

        List<Avaliacao> avaliacoes = new ArrayList<Avaliacao>();
        avaliacoes.add(new Avaliacao(Avaliacao.ANGRY_FACE, Avaliacao.ANGRY_FACE_DESC));
        avaliacoes.add(new Avaliacao(Avaliacao.NEUTRAL_FACE, Avaliacao.NEUTRAL_FACE_DESC));
        avaliacoes.add(new Avaliacao(Avaliacao.HAPPY_FACE, Avaliacao.HAPPY_FACE_DESC));
        avaliacoes.add(new Avaliacao(Avaliacao.FREE, Avaliacao.FREE_DESC));

        return avaliacoes;
    }
}
