package webScraping.example.demo.Controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import webScraping.example.demo.Anime.Anime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class Requisition {
    public String url = "https://animefire.net";

    @GetMapping("/req")
    public List<Anime> home() throws Exception{

        Document doc = Jsoup.connect(url).get();
        Elements images = doc.select(".card-img-top");
        Elements animeLinks = doc.select("article.card a");
        Elements recomendation = doc.select("#body-content > div > div:nth-child(5) > div > div.owl-stage-outer > div > div:nth-child(1) > div > article > a");
        List<Anime> animeList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            Element image = images.get(i);
            Element animeLink = animeLinks.get(i);
            Element recomend = recomendation.get(i);

            Anime anime = new Anime();
            anime.setId(UUID.randomUUID().toString());
            anime.setImgUrl(image.attr("data-src"));
            anime.setNome(image.attr("alt"));
            anime.setLink(animeLink.attr("href"));
            anime.setRecomend(recomend.attr("href"));

            animeList.add(anime);
        }
        return animeList;
    }

    @GetMapping("/test")
    public List recomend() throws IOException {
        String url = "https://animefire.net";
        Document doc = Jsoup.connect(url).get();
        Elements recomendation = doc.select("divArticleLancamentos .containerAnimes a");
        List<String> test = new ArrayList<>();

        for(int i = 0; i < recomendation.size();i++){

            Element recomend = recomendation.get(i);
            test.add(recomend.attr("href").toString());

        }
        return test;
    }



}
