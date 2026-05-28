package com.denfop.api.space.dimension;

import com.denfop.api.space.BaseSpaceSystem;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;

public final class SpaceBootstrap {

    private static volatile boolean initialized = false;

    private SpaceBootstrap() {
    }

    public static synchronized void ensureSpaceCatalogInitialized() {
        if (initialized && SpaceNet.instance != null && SpaceInit.sun != null) {
            return;
        }
        if (SpaceNet.instance == null) {
            SpaceNet.instance = new BaseSpaceSystem();
        }
        if (SpaceInit.sun == null) {
            SpaceInit.reset();
            SpaceInit.init();
            SpaceInit.jsonInit();
        }
        initialized = true;
        SpaceBodyCatalog.invalidate();
        SpaceBodyProfiles.invalidate();
    }
}
