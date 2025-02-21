package com.powerutils;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class PowerConfig {


    public static boolean multi;

    public static void loadConfig(File config, boolean client) {
        loadNormalConfig(config, client);

    }

    private static void loadNormalConfig(final File configFile, final boolean client) {
        final Configuration config = new Configuration(configFile);
        try {
            config.load();
            multi = config.get("MultiEnergy", "Enable", false).getBoolean(false);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }


}
