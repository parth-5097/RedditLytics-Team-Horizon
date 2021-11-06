package controllers;

import play.mvc.*;
import models.UserProfile;
/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private String data;

    public HomeController() {
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result getSearchResult(String key) {
        KeyResultsController results = new KeyResultsController();
        return ok(results.getData(key));
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result getWordStats(String a) {
        Word word = new Word();
        return ok(views.html.word_stats.render(word.bodyData(a)));
    }

    public Result getUserProfile(String username) {
        UserProfile results = new UserProfile();
        return ok(views.html.user_profile.render(results.getData(username)));
    }

}