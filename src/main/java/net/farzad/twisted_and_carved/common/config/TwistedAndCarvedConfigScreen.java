package net.farzad.twisted_and_carved.common.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class TwistedAndCarvedConfigScreen extends MidnightConfig {
    public static final String SERVER = "server";

    @Comment(category = SERVER) public static String woah;
    @Server @Entry(category = SERVER) public static float dmg = 5;

}
