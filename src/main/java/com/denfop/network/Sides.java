package com.denfop.network;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public final class Sides<T> {

    private final T clientInstance;
    private final T serverInstance;

    public Sides(String serverClass, String clientClass) {
        try {
            this.clientInstance = FMLEnvironment.dist == Dist.CLIENT ? (T) Class.forName(clientClass).newInstance() : null;
            this.serverInstance = (T) Class.forName(serverClass).newInstance();
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }


    public T getClient() {
        return clientInstance;
    }

    public T getServer() {
        return serverInstance;
    }

}
