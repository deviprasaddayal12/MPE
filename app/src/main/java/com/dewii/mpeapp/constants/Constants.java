package com.dewii.mpeapp.constants;

import com.dewii.mpeapp.BuildConfig;

public final class Constants {
    private Constants() {

    }

    public static final String APP_ID = BuildConfig.APPLICATION_ID;

    public static final class Api {

        private Api() {
            // last = 0
        }
    }

    public static final class Broadcast {
        public static final String NETWORK_STATE_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

        private Broadcast() {

        }
    }
}
