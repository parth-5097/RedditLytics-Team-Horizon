package models;

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