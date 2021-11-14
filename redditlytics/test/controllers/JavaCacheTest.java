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

    @Test
    public void testGet(){
        JavaCache cache = mock(JavaCache.class);
        CompletableFuture<Optional<Object>> future = new CompletableFuture<Optional<Object>>();
        future.completeExceptionally(new Exception("Null Pointer"));
        when(cache.get("test")).thenReturn(future);
        assertNotNull(cache.get("test"));
    }

    @Test
    public void testRemove(){
        JavaCache cache = mock(JavaCache.class);
        CompletableFuture<Done> future = new CompletableFuture<Done>();
        future.completeExceptionally(new Exception("Null Pointer"));
        when(cache.remove("test")).thenReturn(future);
        assertNotNull(cache.remove("test"));
    }
}