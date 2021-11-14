package controllers;

import akka.Done;
import play.cache.AsyncCacheApi;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.Optional;


public abstract class JavaCache implements AsyncCacheApi {
    private HashMap<String, Object> cache = new HashMap();

    @Override
    public <T> CompletionStage<Optional<T>> get(String key) {
        return CompletableFuture.completedFuture((Optional<T>) this.cache.get(key));
    }

    public CompletionStage<Done> set(String key, String value) {
        return CompletableFuture.supplyAsync(
                () -> {
                    this.cache.put(key, value);
                    return Done.getInstance();
                }
        );
    }

    @Override
    public CompletionStage<Done> remove(String key) {
        return CompletableFuture.supplyAsync(
                () -> {
                    this.cache.remove(key);
                    return Done.getInstance();
                }
        );
    }
}