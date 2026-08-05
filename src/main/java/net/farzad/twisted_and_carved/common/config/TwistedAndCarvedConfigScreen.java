package net.farzad.twisted_and_carved.common.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class TwistedAndCarvedConfigScreen extends MidnightConfig {
    public static final String SERVER = "server";

    @Server @Entry(category = SERVER, min = 0.0f) public static float falchion_damage = 3;

}
