package com.denfop.api.sound;


import net.minecraft.sounds.SoundEvent;

public interface AudioFixer {

    EnumTypeAudio getTypeAudio();

    void setType(EnumTypeAudio type);

    SoundEvent getSound();

    void initiate(int soundEvent);

    boolean getEnable();

}
