package models;

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
import java.util.Date;
import java.util.List;

public class WordTest{
    Word w = new Word();

    @Test
    public void testBodyData(){
        List<models.Wordcount> wordCount = w.bodyData("{\"selftext\":\"Hey there! So I am an apple employee and I am trying to figure out what the discount Sales Code is that I need to put in in order to get the discount to receive 75% off of\n" +
                "two lines. Can anyone help with this please?? ≡ƒñ₧≡ƒñ₧≡ƒñ₧\\n\\nThank you all so much!\",\"author\":\"DaddysLittleOne_1\",\"created_utc\":1636427397,\"title\":\"Apple Employee\",\"subreddit\":\"tmobi\n" +
                "le\"}%5097%{\"selftext\":\"Arrival 4K (paramountmovies.com, VUDU, Apple) - $5\\n\\nFull Metal Jacket 4K MA - $5\\n\\nThe Martian 4K MA - $5\\n\\nPayPal only, comment if interested.\",\"author\":\"Li\n" +
                "nknZelda96\",\"created_utc\":1636427388,\"title\":\"(SELLING) 4K CODES\",\"subreddit\":\"DigitalCodeSELL\"}%5097%{\"selftext\":\"I have the 14 inch MBP and it seems that my clicks on the trackpad ar\n" +
                "e not always recognized. I often have to double click for it to recognize a single click which can get frustrating especially when working with spreadsheets but it also happened in Saf\n" +
                "ari and apps running natively on Apple Silicon. Tap to click is turned off. \\n\\nAnybody with the same issue or know can tell me what I'm doing wrong..? \\n\\n&amp;#x200B;\\n\\nThanks\",\"aut\n" +
                "hor\":\"oddly_no\",\"created_utc\":1636427219,\"title\":\"MBP 14 inch trackpad not recognizing click\",\"subreddit\":\"MacOS\"}\n" +
                "{\"selftext\":\"Hey there! So I am an apple employee and I am trying to figure out what the discount Sales Code is that I need to put in in order to get the discount to receive 75% off of\n" +
                "two lines. Can anyone help with this please?? ≡ƒñ₧≡ƒñ₧≡ƒñ₧\\n\\nThank you all so much!\",\"author\":\"DaddysLittleOne_1\",\"created_utc\":1636427397,\"title\":\"Apple Employee\",\"subreddit\":\"tmobi\n" +
                "le\"}%5097%{\"selftext\":\"Arrival 4K (paramountmovies.com, VUDU, Apple) - $5\\n\\nFull Metal Jacket 4K MA - $5\\n\\nThe Martian 4K MA - $5\\n\\nPayPal only, comment if interested.\",\"author\":\"Li\n" +
                "nknZelda96\",\"created_utc\":1636427388,\"title\":\"(SELLING) 4K CODES\",\"subreddit\":\"DigitalCodeSELL\"}%5097%{\"selftext\":\"I have the 14 inch MBP and it seems that my clicks on the trackpad ar\n" +
                "e not always recognized. I often have to double click for it to recognize a single click which can get frustrating especially when working with spreadsheets but it also happened in Saf\n" +
                "ari and apps running natively on Apple Silicon. Tap to click is turned off. \\n\\nAnybody with the same issue or know can tell me what I'm doing wrong..? \\n\\n&amp;#x200B;\\n\\nThanks\",\"aut\n" +
                "hor\":\"oddly_no\",\"created_utc\":1636427219,\"title\":\"MBP 14 inch trackpad not recognizing click\",\"subreddit\":\"MacOS\"}");

        System.out.println(wordCount);
    }
}