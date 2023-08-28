package com.denfop.api.audio;


import net.minecraft.util.SoundEvent;

public interface IAudioFixer {

    EnumTypeAudio getType();

    void setType(EnumTypeAudio type);

    SoundEvent getSound();

    void initiate(int soundEvent);

    boolean getEnable();

}
