package com.denfop.componets;

import com.denfop.api.sound.AudioFixer;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.utils.Localization;

public class ComponentSoundButton extends ComponentButton {

    private final AudioFixer audioFixer;

    public ComponentSoundButton(final BlockEntityBase entityBlock, final int event, AudioFixer audioFixer) {
        super(entityBlock, event);
        this.audioFixer = audioFixer;
    }

    @Override
    public void ClickEvent() {
        super.ClickEvent();
    }

    public String getText() {
        if (audioFixer.getEnable()) {
            return Localization.translate("iu.sound.enable");
        } else {
            return Localization.translate("iu.sound.disable");
        }
    }

    public AudioFixer getAudioFixer() {
        return audioFixer;
    }

}
