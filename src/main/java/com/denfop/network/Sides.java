package com.denfop.network;

import net.minecraftforge.fml.common.FMLCommonHandler;

public final class Sides<T> {

    private final T clientInstance;
    private final T serverInstance;

    public Sides(String serverClass, String clientClass) {
        try {
            if (FMLCommonHandler.instance().getSide().isClient()) {
                this.clientInstance = (T) Class.forName(clientClass).newInstance();
            } else {
                this.clientInstance = null;
            }

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
