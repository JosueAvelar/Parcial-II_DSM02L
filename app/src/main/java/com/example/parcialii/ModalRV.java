package com.example.parcialii;

import android.os.Parcel;
import android.os.Parcelable;

public class ModalRV implements Parcelable {
    // creating variables for our different fields.
    private String courseName;
    private String courseDescription;
    private String coursePrice;
    private String bestSuitedFor;
    private String courseImg;
    private String courseLink;
    private String courseId;


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    // creating an empty constructor.
    public ModalRV() {

    }

    protected ModalRV(Parcel in) {
        courseName = in.readString();
        courseId = in.readString();
        courseDescription = in.readString();
        coursePrice = in.readString();
        bestSuitedFor = in.readString();
        courseImg = in.readString();
        courseLink = in.readString();
    }

    public static final Creator<ModalRV> CREATOR = new Creator<ModalRV>() {
        @Override
        public ModalRV createFromParcel(Parcel in) {
            return new ModalRV(in);
        }

        @Override
        public ModalRV[] newArray(int size) {
            return new ModalRV[size];
        }
    };

    // creating getter and setter methods.
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getBestSuitedFor() {
        return bestSuitedFor;
    }

    public void setBestSuitedFor(String bestSuitedFor) {
        this.bestSuitedFor = bestSuitedFor;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }


    public ModalRV(String courseId, String courseName, String courseDescription, String coursePrice, String bestSuitedFor, String courseImg, String courseLink) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.courseDescription = courseDescription;
        this.coursePrice = coursePrice;
        this.bestSuitedFor = bestSuitedFor;
        this.courseImg = courseImg;
        this.courseLink = courseLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(courseId);
        dest.writeString(courseDescription);
        dest.writeString(coursePrice);
        dest.writeString(bestSuitedFor);
        dest.writeString(courseImg);
        dest.writeString(courseLink);
    }
}
