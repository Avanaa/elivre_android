package br.com.avana.elivreapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.Objects;

public class PostModel implements Serializable, ClusterItem {

    private String id;
    private String dataString;
    private String title;
    private String snippet;
    private Avaliacao avaliacao;
    private String usuario;
    private double lat;
    private double lng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
        this.title = avaliacao.getDescricao();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostModel postModel = (PostModel) o;
        return Objects.equals(getId(), postModel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(getLat(), getLng());
    }

    @Override
    public String getTitle() {
        return getAvaliacao().getDescricao();
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
