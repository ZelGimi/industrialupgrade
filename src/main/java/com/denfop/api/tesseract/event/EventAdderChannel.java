package com.denfop.api.tesseract.event;

import com.denfop.api.tesseract.Channel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;

public class EventAdderChannel extends LevelEvent {

    private final Channel channel;

    public EventAdderChannel(Channel channel, final Level world) {
        super(world);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

}
