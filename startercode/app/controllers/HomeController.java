package controllers;

import play.mvc.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import javax.inject.Inject;
import javax.inject.Singleton;
import static play.libs.Scala.asScala;
import play.libs.Json;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import models.SearchKey;
/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private final Form<SearchKey> searchForm;
    private MessagesApi messageApi;
    private String data;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public List allSearchResults = new ArrayList();
    @Inject
    public HomeController(FormFactory f,MessagesApi m){
        this.searchForm = f.form(SearchKey.class);
        this.messageApi = m;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index(Http.Request request) {
        return ok(views.html.index.render(this.allSearchResults,this.searchForm,request,this.messageApi.preferred(request)));
    }

    public Result createSearchData(Http.Request request){
        System.out.println(request);
        Form<SearchKey> form = this.searchForm.bindFromRequest(request);
        System.out.println(form);
        SearchKey ans = form.get();
        this.data = ans.getKey();
        KeyResults kk = new KeyResults();

            List<String> results = kk.getData(ans.getKey());
            for(String temp: results){
                this.allSearchResults.add(temp);
            }

        form = null;
        return ok(views.html.index.render(allSearchResults,this.searchForm,request,this.messageApi.preferred(request)));
    }
}
