package com.dewii.mpeapp.constants;

public final class App {

    private App() {
    }

    public static final class Usage {
        public static final int ANY = 0x08;
        public static final int LOCATION = 0x09;
        public static final int CAMERA = 0x0A;
        public static final int GALLERY = 0x0B;
        public static final int BROWSER = 0x0C;
        public static final int STORAGE = 0x0D;
        public static final int CALL = 0x0E;

        private Usage() {
        }
    }

    public static final class SystemIntent {
        public static final int CAMERA = 0x0D;
        public static final int GALLERY = 0x0E;
        public static final int BROWSER = 0x0F;

        private SystemIntent() {
        }
    }

    public static final class Directory {
        public static final int ROOT = 0;
        public static final int DATABASE = 1;
        public static final int BACK_UP = 2;

        private Directory() {
        }
    }

    public static final class Intent {

        private Intent() {
        }

        public static  final class Extra {


            private Extra() {
            }
        }

        public static  final class Value {


            private Value() {
            }
        }

        public static  final class Result {


            private Result() {
            }
        }
    }
}
