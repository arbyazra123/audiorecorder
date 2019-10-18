package com.arbyazra.audiorecorder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AudioData implements Parcelable {
    private String outputname;
    private String oncreated;
    private String name;

    public AudioData(Parcel in) {
        outputname = in.readString();
        oncreated = in.readString();
        name = in.readString();
    }

    public AudioData() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(outputname);
        dest.writeString(oncreated);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AudioData> CREATOR = new Creator<AudioData>() {
        @Override
        public AudioData createFromParcel(Parcel in) {
            return new AudioData(in);
        }

        @Override
        public AudioData[] newArray(int size) {
            return new AudioData[size];
        }
    };

    public String getOutputname() {
        return outputname;
    }

    public void setOutputname(String outputname) {
        this.outputname = outputname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOncreated() {
        return oncreated;
    }

    public void setOncreated(String oncreated) {
        this.oncreated = oncreated;
    }
}
