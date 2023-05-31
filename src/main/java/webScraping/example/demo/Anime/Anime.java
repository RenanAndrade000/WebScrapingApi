package webScraping.example.demo.Anime;


import lombok.Data;

import java.util.UUID;


public class Anime {
    private String id;
    private String nome;
    private String imgUrl;
    private String link;
    private String recomend;

    public Anime(String nome,String imgUrl){
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.imgUrl = imgUrl;

    }

    public String getRecomend() {
        return recomend;
    }

    public void setRecomend(String recomend) {
        this.recomend = recomend;
    }

    public Anime() {
        this.id = UUID.randomUUID().toString();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
