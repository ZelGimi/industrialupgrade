package com.denfop.blockentity.base;

import com.denfop.api.sound.EnumTypeAudio;

public interface ISteamMechanism {

    void initiate(int soundEvent);

    EnumTypeAudio getTypeAudio();

}
