package com.denfop.api.audio;

import ic2.api.network.INetworkTileEntityEventListener;

public interface IAudioFixer extends INetworkTileEntityEventListener {

    EnumTypeAudio getType();

    void setType(EnumTypeAudio type);

    void initiate(int soundEvent);

    void changeSound();

    boolean getEnable();

}
