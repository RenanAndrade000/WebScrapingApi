package webScraping.example.demo.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import webScraping.example.demo.Anime.Anime;
import webScraping.example.demo.Anime.Recomendations;
import webScraping.example.demo.Anime.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
public class Requisition {
    public String url = "https://animefire.net";
    @CrossOrigin(origins = "*")
    @GetMapping("/last-release")
    public List<Anime> home() throws Exception{

        Document doc = Jsoup.connect(url).get();
        Elements images = doc.select(".card-img-top");
        Elements animeLinks = doc.select("article.card a");
       // Elements recomendation = doc.select("#body-content > div > div:nth-child(5) > div > div.owl-stage-outer > div > div:nth-child(1) > div > article > a");
        List<Anime> animeList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            Element image = images.get(i);
            Element animeLink = animeLinks.get(i);
            //Element recomend = recomendation.get(i);

            Anime anime = new Anime();
            anime.setId(UUID.randomUUID().toString());
            anime.setImgUrl(image.attr("data-src"));
            anime.setNome(image.attr("alt"));
            String linkAnime = sendVideo(animeLink.attr("href"));
            anime.setLink(linkAnime);



            //anime.setRecomend(recomend.attr("href"));

            animeList.add(anime);
        }
        return animeList;
    }
    @CrossOrigin(origins = "*")
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
    @CrossOrigin(origins = "*")
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
    public String sendVideo(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements video = doc.select("video");
        //System.out.println("Elemento de vídeo encontrado: " + video);
        String aux = video.attr("data-video-src");


        String link = null;
        try {
            URL url1 = new URL(aux);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            // Definir o User-Agent
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 OPR/98.0.0.0");
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            String jsonResponse = response.toString();
            //System.out.println("JSON response:\n" + jsonResponse);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray dataArray = jsonObject.getAsJsonArray("data");
            JsonObject firstObject = dataArray.get(0).getAsJsonObject();
            link = firstObject.get("src").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return link;
    }



}
