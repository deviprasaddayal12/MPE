package com.dewii.mpeapp.constants;

public final class Status {
    private Status() {

    }

    public static final class Login {
        private Login() {

        }

        public static final int REQUIRE_SIGN_UP = 0;
        public static final int REQUIRE_SIGN_IN_AND_CREDS = 1;
        public static final int REQUIRE_SIGN_IN_NOT_CREDS = 2;
        public static final int REQUIRE_NOTHING = 3;
    }

    public static final class Request {
        private Request() {

        }

        public static final int API_HIT = 1;
        public static final int API_SUCCESS = 2;
        public static final int API_ERROR = 3;
    }

    public static final class Response {
        private Response() {

        }

        public static final int SUCCESS = 200;
    }
}
