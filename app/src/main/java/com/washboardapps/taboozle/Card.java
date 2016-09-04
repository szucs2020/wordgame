package com.washboardapps.taboozle;

/**
 * Created by Christian on 15/02/2015.
 */
public class Card {

    private int ID;
    private String title;
    private String word1;
    private String word2;
    private String word3;
    private String word4;
    private String word5;
    private int category;
    private int cycle;
    private int called;
    private int correct;
    private int skipped;
    private int buzzed;
    private float difficulty;
    private int flag;

    //Getters
    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public String getWord1() {
        return word1;
    }
    public String getWord2() {
        return word2;
    }
    public String getWord3() {
        return word3;
    }
    public String getWord4() {
        return word4;
    }
    public String getWord5() {
        return word5;
    }
    public int getCategory() {
        return category;
    }
    public int getCycle() {
        return cycle;
    }
    public int getCalled() {
        return called;
    }
    public int getCorrect() {
        return correct;
    }
    public int getSkipped() {
        return skipped;
    }
    public int getBuzzed() {
        return buzzed;
    }
    public float getDifficulty() {
        return difficulty;
    }
    public int getFlag() {
        return flag;
    }

    //Setters
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setWord1(String word1) {
        this.word1 = word1;
    }
    public void setWord2(String word2) {
        this.word2 = word2;
    }
    public void setWord3(String word3) {
        this.word3 = word3;
    }
    public void setWord4(String word4) {
        this.word4 = word4;
    }
    public void setWord5(String word5) {
        this.word5 = word5;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }
    public void setCalled(int called) {
        this.called = called;
    }
    public void setCorrect(int correct) {
        this.correct = correct;
    }
    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }
    public void setBuzzed(int buzzed) {
        this.buzzed = buzzed;
    }
    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
