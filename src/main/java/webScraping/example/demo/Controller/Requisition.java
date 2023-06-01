package webScraping.example.demo.Controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import webScraping.example.demo.Anime.Anime;
import webScraping.example.demo.Anime.Recomendations;
import webScraping.example.demo.Anime.Search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class Requisition {
    public String url = "https://animefire.net";

    @GetMapping("/last-release")
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

    @GetMapping("/recomend")
    public List recomend() throws IOException, InterruptedException {
        String url = "https://animefire.net";
        Document doc = Jsoup.connect(url).get();
        // Pausar a execução por 3 segundos para permitir o carregamento dos elementos
        //Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        Elements recomendation = doc.select("article.containerAnimes > a");
        Elements recomendationImg = doc.select("article.containerAnimes img");
        List<Recomendations> recomendationsList = new ArrayList<>();

        for(int i = 0; i < recomendation.size();i++){
            Recomendations recomendations = new Recomendations();

            Element recomend = recomendation.get(i);
            Element recomendImg = recomendationImg.get(i);

            recomendations.setLink(recomend.attr("href").toString());
            recomendations.setName(recomendImg.attr("alt").toString());
            recomendations.setImg(recomendationImg.attr("data-src").toString());
            recomendations.setId(UUID.randomUUID().toString());

            recomendationsList.add(recomendations);
        }
        return recomendationsList;
    }

    @GetMapping("/search/{anime}")
    public List search(@PathVariable String anime) throws IOException {
        String url ="https://animefire.net/pesquisar/"+anime;

        Document doc = Jsoup.connect(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post();

        Elements searchResults = doc.select(".col-6.col-sm-4.col-md-3.col-lg-2.mb-1.minWDanime.divCardUltimosEps");
        Elements linkSearchResults = doc.select("article.card.cardUltimosEps a");
        Elements imgSearchResults = doc.select("article.card.cardUltimosEps img");
        List<Search> searchResult = new ArrayList<>();

        for(int i =0; i < searchResults.size();i++){
            Element search = searchResults.get(i);
            Element linkSearch = linkSearchResults.get(i);
            Element imgSearch = imgSearchResults.get(i);

            Search search1 = new Search();
            search1.setNome(search.attr("title"));
            search1.setLink(linkSearch.attr("href"));
            search1.setImgUrl(imgSearch.attr("data-src"));


            searchResult.add(search1);


        }
        return searchResult;


    }



}
