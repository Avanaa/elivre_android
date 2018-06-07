package br.com.avana.elivreapp.model;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    public static final int HAPPY_FACE = 1;
    public static final int NEUTRAL_FACE = 2;
    public static final int ANGRY_FACE = 3;
    public static final int FREE = 4;

    public static final String HAPPY_FACE_DESC = "Flanelinhas carinhosos";
    public static final String NEUTRAL_FACE_DESC = "Estou com cara de cu";
    public static final String ANGRY_FACE_DESC = "Fui estuprado pelos flanelinhas";
    public static final String FREE_DESC = "Livre pra caralho";

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
