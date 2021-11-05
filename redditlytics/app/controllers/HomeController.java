package controllers;

import play.mvc.*;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private String data;
    KeyResultsController results = new KeyResultsController();
    Word word = new Word();

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result getSearchResult(String data) {
        return ok(results.getData(data));
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result getWordStats(String data) {
        return ok(views.html.word_stats.render(word.bodyData(data)));
    }

    public Result getSubreddit(String data){
        return ok(views.html.subreddit.render(results.getSubredditData(data)));
    }
}