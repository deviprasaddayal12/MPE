package com.dewii.mpeapp.constants;

import com.dewii.mpeapp.utils.AppUtils;

public enum Directory {
    ROOT(App.Directory.ROOT, AppUtils.appName())
    , DATABASE(App.Directory.DATABASE, "Database")
    , BACK_UP(App.Directory.BACK_UP, "Backups")
    ;

    private int dirId;
    private String dirName;

    Directory(int dirId, String dirName) {
        this.dirId = dirId;
        this.dirName = dirName;
    }

    public int getDirId() {
        return dirId;
    }

    public String getDirName() {
        return dirName;
    }
}
