package com.simplequarries;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class SQConfig {


    public static void loadConfig(File config, boolean client) {
        loadNormalConfig(config, client);

    }

    private static void loadNormalConfig(final File configFile, final boolean client) {
        final Configuration config = new Configuration(configFile);
        try {
            config.load();


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }


}
