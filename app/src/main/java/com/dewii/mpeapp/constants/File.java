package com.dewii.mpeapp.constants;

import com.dewii.mpeapp.BuildConfig;

public final class File {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    public static final String FILE_FROM = "FILE_FROM";
    public static final String FILE_NAME = "FILE_NAME";
    public static final String FILE_PATH = "FILE_PATH";

    private File() {
    }

    public static final class BackUpFile {
        public static final String DATABASE = "Databases";

        private BackUpFile() {
        }

        public static String getTxtFile(boolean addMillis, String firstName) {
            return addMillis ? firstName + "_" + System.currentTimeMillis() + Extension.txt
                    : firstName + Extension.txt;
        }
    }

    public static final class From {
        public static final String MOVIES = "MOVIES";
        public static final String SPOILERS = "SPOILERS";
        public static final String PROFILE = "PROFILE";

        private From() {
        }
    }

    public static final class Category {
        public static final String application = "application";
        public static final String audio = "audio";
        public static final String image = "image";
        public static final String text = "text";
        public static final String video = "video";

        private Category() {
        }
    }

    public static final class Mime {
        // Text Mime
        public static final String txt = "text/plain";

        // Image Mime
        public static final String bmp = "image/bmp";
        public static final String cgm = "image/cgm";
        public static final String gif = "image/gif";
        public static final String jpeg = "image/jpeg";
        public static final String jpg = "image/jpg";
        public static final String mdi = "image/vnd.ms-modi";
        public static final String psd = "image/vnd.adobe.photoshop";
        public static final String png = "image/png";
        public static final String svg = "image/svg";

        // Audio Mime
        public static final String adp = "audio/adpcm";
        public static final String aac = "audio/x-aac";
        public static final String mpga = "audio/mpeg";
        public static final String mp4a = "audio/mp4";
        public static final String oga = "audio/ogg";
        public static final String wav = "audio/x-wav";
        public static final String mp3 = "audio/mp3";

        // Video Mime
        public static final String a3gp = "video/3gpp";
        public static final String a3g2 = "video/3gpp2";
        public static final String avi = "video/x-msvideo";
        public static final String xlv = "video/x-flv";
        public static final String m4v = "video/x-m4v";
        public static final String mp4 = "video/mp4";

        // Document Mime
        public static final String pdf = "application/pdf";
        public static final String dvi = "application/x-dvi";
        public static final String karbon = "application/vnd.kdee.karbon";
        public static final String mdb = "application/x-msaccess";
        public static final String xls = "application/vnd.ms-excel";
        public static final String pptx = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        public static final String docx = "application/vnd.openxmlformats-officedocument.spreadsheet.template";
        public static final String ppt = "application/vnd.ms-powerpoint";
        public static final String doc = "application/vnd.ms-word";
        public static final String oxt = "application/vnd.openofficeorg.extension";
        public static final String rar = "application/x-rar-compressed";
        public static final String zip = "application/zip";

        // For all
        public static final String all = "file/*";

        private Mime() {
        }
    }

    public static final class Extension {
        // Text Extensions
        public static final String txt = "txt";

        private Extension() {
        }
    }
}
