package com.denfop.componets;

import com.denfop.Localization;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.tiles.base.TileEntityBlock;

public class ComponentSoundButton extends ComponentButton {

    private final IAudioFixer audioFixer;

    public ComponentSoundButton(final TileEntityBlock entityBlock, final int event, IAudioFixer audioFixer) {
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

    public IAudioFixer getAudioFixer() {
        return audioFixer;
    }

}
