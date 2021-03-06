package com.stoyan.favouritemovies.object;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.Override;
import java.lang.String;


public class Movie implements Parcelable {

    public static boolean musicCreated = false;
    public static MediaPlayer mediaPlayer;
    public String dataString;

    public Movie(String data) {
        dataString = data;
    }

    private Movie(Parcel in) {
        dataString = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dataString);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
