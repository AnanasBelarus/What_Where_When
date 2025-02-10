package models;

import java.util.ArrayList;
import java.util.Collection;

public class Championship extends ArrayList<Round> {
    private String championshipName;
    private String about;
    private String date;
    private String editor;

    public Championship(Collection<? extends Round> c, String championshipName, String about, String date, String editor) {
        super(c);
        this.championshipName = championshipName;
        this.about = about;
        this.date = date;
        this.editor = editor;
    }

    public String getChampionshipName() {
        return championshipName;
    }

    public void setChampionshipName(String championshipName) {
        this.championshipName = championshipName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Override
    public String toString() {
        return "Championship{" +
                "championshipName='" + championshipName + '\'' +
                ", about='" + about + '\'' +
                ", date='" + date + '\'' +
                ", editor='" + editor + '\'' +
                '}'+super.toString();
    }
}
