package models;


/**
 * <p>The WordCount model is developed to keep data of word and its count which will be calculated in <class>Word</class> file.</p>
 * <p>The getters functions are used in html view file to get data from model.</p>
 * @author Jigar Borad
 */
public class Wordcount{
    public String key;
    public Integer value;

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public Wordcount(String a, Integer b){
        this.key=a;
        this.value=b;
    }
}