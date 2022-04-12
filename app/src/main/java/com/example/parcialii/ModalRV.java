package com.example.parcialii;

import android.os.Parcel;
import android.os.Parcelable;

public class ModalRV implements Parcelable {
    private static String courseName;
    private static String courseDescription;
    private static String coursePrice;
    private static String bestSuitedFor;
    private static String courseImg;
    private static String courseLink;
    private static String courseId;


    public static String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


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

    public static String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public static String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public static String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public static String getBestSuitedFor() {
        return bestSuitedFor;
    }

    public void setBestSuitedFor(String bestSuitedFor) {
        this.bestSuitedFor = bestSuitedFor;
    }

    public static String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public static String getCourseLink() {
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
