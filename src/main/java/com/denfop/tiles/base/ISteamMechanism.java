package com.denfop.tiles.base;

import com.denfop.api.audio.EnumTypeAudio;

public interface ISteamMechanism {

    void initiate(int soundEvent);

    EnumTypeAudio getTypeAudio();

}
