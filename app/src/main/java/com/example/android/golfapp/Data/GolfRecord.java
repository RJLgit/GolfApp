package com.example.android.golfapp.Data;

import java.sql.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GolfScores")
public class GolfRecord {

    @PrimaryKey(autoGenerate =  true)
    private int id;
    private String name;
    private String course;
    private int par;
    private int score;
    private java.util.Date date;

    public GolfRecord(int id, String name, String course, int par, int score, Date date) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.par = par;
        this.score = score;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }
}
