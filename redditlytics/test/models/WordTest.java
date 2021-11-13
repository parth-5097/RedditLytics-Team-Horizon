package models;

import java.io.IOException;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;
import java.util.*;

public class WordTest{


    @Test
    public void testBodyData(){
        Word w = new Word();
        List<models.Wordcount> wordCount = w.bodyData("{\"selftext\":\"Running run This is jigar jigar\",\"author\":\"MiniLoona\",\"created_utc\":1636457205,\"title\":\"This is my outfit for our first date so you can breed me in your car before it even start\",\"subreddit\":\"BreedingMaterial\"}%5097%{\"selftext\":\"This is jigar running\",\"author\":\"misscooke\",\"created_utc\":1636457199,\"title\":\"40% OFF ≡ƒÆª ≡ƒññ TOP 3% ≡ƒÿê 25 YEAR OLD PETITE CAR | ENDURO | MX GIRL ≡ƒÆÖ\",\"subreddit\":\"Blonde\"}");

        assertEquals("jigar",wordCount.get(0).getKey());
        assertEquals(3,wordCount.get(0).getValue());
        assertEquals("run",wordCount.get(1).getKey());
        assertEquals(2,wordCount.get(1).getValue());
    }

    @Test
    public void testCatchBodyData() throws IOException{
        Word w = new Word();
        w.bodyData("jdnkjcdjncjdnkjcnkd");
    }
}