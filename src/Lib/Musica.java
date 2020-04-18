package Lib;

import java.util.List;

public class Musica {
    private int id;
    private String titulo;
    private String interprete;
    private int ano;
    private List<String> etiquetas;
    private int numeroDownloads;

    public Musica(String titulo, String interprete, int ano, List<String> etiquetas){
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.etiquetas = etiquetas;
        this.numeroDownloads = 0;
    }

    public int getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(int numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInterprete() {
        return interprete;
    }

    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String toString(){
        return "ID: " + this.id + ", TITULO: " + this.titulo +
                ", INTERPRETE: " + this.interprete + ", ANO: " +
                this.ano + ", ETIQUETAS: " + this.etiquetas +
                ", DOWNLOADS: " + this.numeroDownloads;
    }
}
