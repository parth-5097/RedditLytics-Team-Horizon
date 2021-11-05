package controllers;

import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private String data;
    public HomeController(){
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(String key){
        KeyResultsController kk = new KeyResultsController();
        return ok(kk.getData(key));
    }
    public Result index_1(){
        return ok(views.html.index.render());
    }
}
