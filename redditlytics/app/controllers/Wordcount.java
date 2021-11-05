package controllers;

public class Wordcount{
    String key;
    Integer value;

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