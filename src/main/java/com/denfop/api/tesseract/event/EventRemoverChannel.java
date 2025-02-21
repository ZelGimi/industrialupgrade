package com.denfop.api.tesseract.event;

import com.denfop.api.tesseract.Channel;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class EventRemoverChannel extends WorldEvent {

    private final Channel channel;

    public EventRemoverChannel(Channel channel, final World world) {
        super(world);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

}
