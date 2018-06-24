package br.com.avana.elivreapp.model;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    public static final int HAPPY_FACE = 1;
    public static final int NEUTRAL_FACE = 2;
    public static final int ANGRY_FACE = 3;
    public static final int FREE = 4;

    private int avaliacao;
    private String descricao;

    public Avaliacao() {}

    public Avaliacao(int avaliacao, String descricao) {
        this.avaliacao = avaliacao;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getAvaliacao() {
        return avaliacao;
    }
}
