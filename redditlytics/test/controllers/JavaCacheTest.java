package controllers;
import org.junit.Test;
import org.junit.Before;
import akka.Done;
import play.cache.AsyncCacheApi;
import java.util.concurrent.*;
import javax.inject.Inject;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import java.util.*;
import java.io.IOException;

public class JavaCacheTest {
    @Test
    public void testSet(){
        JavaCache cache = mock(JavaCache.class,Mockito.CALLS_REAL_METHODS);
        assertNotNull(cache.set("test","GG"));
    }
}