package models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WordCountTest {

    Wordcount test = new Wordcount("Jigar",22);
    @Test
    public void getKeyTest() {
        assertEquals("Jigar",test.getKey());
    }

    @Test
    public void getValueTest() {
        assertEquals(22,test.getValue());
    }
}