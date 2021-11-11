package controllers;

import play.mvc.*;
import models.*;

import java.util.*;
import java.util.concurrent.*;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private String key;
    private String data;
    KeyResults results = new KeyResults();
    NLP sa = new NLP();
    Word word = new Word();
    UserProfile profile = new UserProfile();

    public HomeController() {
        sa.init();
//        ArrayList<String> tweets = new ArrayList<String>();
//        NLP sa = new NLP();
//        tweets.add("Multiple text classification datasets from NLP-progress\n" +
//                "Multiple sentiment analysis datasets from NLP-progress\n" +
//                "Yelp Data Set Challenge (8 million reviews of businesses from over 1 million users across 10 cities)\n" +
//                "Kaggle Data Sets with text content (Kaggle is a company that hosts machine learning competitions)\n" +
//                "Labeled Twitter data sets from (1) the SemEval 2018 Competition and (2) Sentiment 140 project\n" +
//                "Amazon Product Review Data from UCSD. This is a very large and rich data set with review text, ratings, votes, product metdata, etc. The full dataset is extremely large - some of the smaller subsets provided may be better for class projects.\n" +
//                "IMDB Moview Review Data with 50,000 movie reviews and binary sentiment labels\n" +
//                "Well-known Movie review data for sentiment analysis, from Pang and Lee, Cornell\n" +
//                "Product review data from Johns Hopkins University  (goal is to predict ratings on scale of 1 to 5)"+
//                "Multiple text classification datasets from NLP-progress\n" +
//                "Multiple sentiment analysis datasets from NLP-progress\n" +
//                "Yelp Data Set Challenge (8 million reviews of businesses from over 1 million users across 10 cities)\n" +
//                "Kaggle Data Sets with text content (Kaggle is a company that hosts machine learning competitions)\n" +
//                "Labeled Twitter data sets from (1) the SemEval 2018 Competition and (2) Sentiment 140 project\n" +
//                "Amazon Product Review Data from UCSD. This is a very large and rich data set with review text, ratings, votes, product metdata, etc. The full dataset is extremely large - some of the smaller subsets provided may be better for class projects.\n" +
//                "IMDB Moview Review Data with 50,000 movie reviews and binary sentiment labels\n" +
//                "Well-known Movie review data for sentiment analysis, from Pang and Lee, Cornell\n" +
//                "Product review data from Johns Hopkins University  (goal is to predict ratings on scale of 1 to 5)"+
//                "Multiple text classification datasets from NLP-progress\n" +
//                "Multiple sentiment analysis datasets from NLP-progress\n" +
//                "Yelp Data Set Challenge (8 million reviews of businesses from over 1 million users across 10 cities)\n" +
//                "Kaggle Data Sets with text content (Kaggle is a company that hosts machine learning competitions)\n" +
//                "Labeled Twitter data sets from (1) the SemEval 2018 Competition and (2) Sentiment 140 project\n" +
//                "Amazon Product Review Data from UCSD. This is a very large and rich data set with review text, ratings, votes, product metdata, etc. The full dataset is extremely large - some of the smaller subsets provided may be better for class projects.\n" +
//                "IMDB Moview Review Data with 50,000 movie reviews and binary sentiment labels\n" +
//                "Well-known Movie review data for sentiment analysis, from Pang and Lee, Cornell\n" +
//                "Product review data from Johns Hopkins University  (goal is to predict ratings on scale of 1 to 5)");
//        sa.init();
//        for(String tweet : tweets) {
//            System.out.println(tweet + " : " + sa.findSentiment(tweet));
//        }
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public CompletionStage<Result> getSearchResult(String key) {
        this.key = key;
        return CompletableFuture
                .supplyAsync(() -> results.getData(key))
                .thenApply(i -> ok(i));
    }

    public CompletionStage<Result> getSentimentResult(Http.Request request){
        JsonNode json = request.body().asJson();
        return CompletableFuture
                .supplyAsync(() -> sa.findSentiment(String.valueOf(json.get("text"))))
                .thenApply(i -> {
                    return ok(String.valueOf(i));
                });
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public CompletionStage<Result> getWordStats(String a) {
        if (data != null && this.key == a) {
            return CompletableFuture
                    .supplyAsync(() -> word.bodyData(data))
                    .thenApply(i -> ok(views.html.word_stats.render(i)));
        } else {
            this.key = a;
            data = results.getData(a);
            return CompletableFuture
                    .supplyAsync(() -> word.bodyData(data))
                    .thenApply(i -> ok(views.html.word_stats.render(i)));
        }
    }

    public CompletionStage<Result> getUserProfile(String username) {
        return CompletableFuture
                .supplyAsync(() -> profile.getData(username))
                .thenApply(i -> {
                    List<UserData> userdata = i;
                    String author = userdata.get(0).getAuthor();
                    long totalAwardsReceived = userdata.get(0).getTotal_awards_received();
                    return ok(views.html.user_profile.render(userdata, author, totalAwardsReceived));
                });
    }

    public CompletionStage<Result> getSubreddit(String word) {
        return CompletableFuture
                .supplyAsync(() -> results.getSubredditData(word))
                .thenApply(i -> ok(views.html.subreddit.render(i)));
    }

}