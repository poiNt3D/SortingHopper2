package me.sothatsit.usefulsnippets;

import java.util.logging.Level;
import java.util.logging.Logger;

public class L {
    private static Logger mclogger = Logger.getLogger("minecraft");

    public static void i(String tag, String msg){
        mclogger.log(Level.INFO, tag + ": " + msg);
    }

    public static void w(String tag, String msg) {
        mclogger.log(Level.WARNING, tag + ": " + msg);
    }

    public static void d(String tag, String msg){
        mclogger.log(Level.CONFIG, tag + ": " + msg);
    }

    public static void v(String tag, String msg) {
        mclogger.log(Level.FINE, tag + ": " + msg);
    }

    public static void e(String tag, String msg) {
        mclogger.log(Level.SEVERE, tag + ": " + msg);
    }
}
