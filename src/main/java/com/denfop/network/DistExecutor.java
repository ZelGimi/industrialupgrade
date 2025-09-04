package com.denfop.network;

import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public class DistExecutor {
    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        switch (FMLEnvironment.dist) {
            case CLIENT:
                return clientTarget.get().get();
            case DEDICATED_SERVER:
                return serverTarget.get().get();
            default:
                throw new IllegalArgumentException("UNSIDED?");
        }
    }
}
