package com.example.android.golfapp.Data;

import java.util.Comparator;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "golfscores")
public class GolfRecord {

    @PrimaryKey(autoGenerate =  true)
    private int id;
    private String name;
    private String course;
    private int par;
    private int score;
    private java.util.Date date;
    //Used when we create new GolfRecord objects. No id as it is auto generated
    @Ignore
    public GolfRecord(String name, String course, int par, int score, java.util.Date date) {
        this.name = name;
        this.course = course;
        this.par = par;
        this.score = score;
        this.date = date;
    }
    //Room uses this constructor to load the GolfRecord objects after the id has been auto generated
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

    public static class NameComparator implements Comparator<GolfRecord> {
        @Override
        public int compare(GolfRecord golfRecord, GolfRecord t1) {
            return golfRecord.getName().compareTo(t1.getName());
        }
    }

    public static class ScoreComparator implements Comparator<GolfRecord> {
        @Override
        public int compare(GolfRecord golfRecord, GolfRecord t1) {
            if (t1.getScore() == golfRecord.getScore()) {
                return 0;
            } else if (golfRecord.getScore() > t1.getScore()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static class DateComparator implements Comparator<GolfRecord> {
        @Override
        public int compare(GolfRecord golfRecord, GolfRecord t1) {
            return golfRecord.getDate().compareTo(t1.getDate());
        }
    }
}
