package com.example.starmusicapp;

public class DataClass {



    public DataClass(String songurl, String songname, String songauth, String imageurl) {
        this.songurl = songurl;
        this.songname = songname;
        this.songauth = songauth;
        this.imageurl = imageurl;
        /*this.key = key;*/
    }

    public String getSongurl() {return songurl;}

    public String getSongname() {return songname;}

    public String getSongauth() {return songauth;}

    public String getImageurl() {return imageurl;}

    public String getKey() {return key;}
    public void setKey(String key) {this.key = key;}


    private String songurl;
    private String songname;
    private String songauth;
    private String imageurl;
    private String key;
    public DataClass(){
    }

}
