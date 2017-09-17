package com.ik.readthenews.repository.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Article implements Parcelable{
    private String PUBLISHER;
    private String HOSTNAME;
    private String CATEGORY;
    private long TIMESTAMP;
    private String TITLE;
    @PrimaryKey
    private int ID;
    private String URL;

    public Article(){

    }
    protected Article(Parcel in) {
        PUBLISHER = in.readString();
        HOSTNAME = in.readString();
        CATEGORY = in.readString();
        TIMESTAMP = in.readLong();
        TITLE = in.readString();
        ID = in.readInt();
        URL = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getPUBLISHER() {
        return this.PUBLISHER;
    }

    public void setPUBLISHER(String PUBLISHER) {
        this.PUBLISHER = PUBLISHER;
    }

    public String getHOSTNAME() {
        return this.HOSTNAME;
    }

    public void setHOSTNAME(String HOSTNAME) {
        this.HOSTNAME = HOSTNAME;
    }

    public String getCATEGORY() {
        return this.CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public long getTIMESTAMP() {
        return this.TIMESTAMP;
    }

    public void setTIMESTAMP(long TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public String getTITLE() {
        return this.TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PUBLISHER);
        dest.writeString(HOSTNAME);
        dest.writeString(CATEGORY);
        dest.writeLong(TIMESTAMP);
        dest.writeString(TITLE);
        dest.writeInt(ID);
        dest.writeString(URL);
    }
}
