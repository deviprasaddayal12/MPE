package com.dewii.mpeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FileModel extends RootModel {
    private String fileName;
    private String filePath;
    private String extraFileInfo;

    public FileModel() {

    }

    public FileModel(String fileName, String filePath, String extraFileInfo) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.extraFileInfo = extraFileInfo;
    }

    protected FileModel(Parcel in) {
        super(in);
        fileName = in.readString();
        filePath = in.readString();
        extraFileInfo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeString(extraFileInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<FileModel> CREATOR = new Parcelable.Creator<FileModel>() {
        @Override
        public FileModel createFromParcel(Parcel in) {
            return new FileModel(in);
        }

        @Override
        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtraFileInfo() {
        return extraFileInfo;
    }

    public void setExtraFileInfo(String extraFileInfo) {
        this.extraFileInfo = extraFileInfo;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", extraFileInfo='" + extraFileInfo + '\'' +
                '}';
    }
}
