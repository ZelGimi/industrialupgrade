package com.denfop.api.space.colonies;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Timer;

import java.util.LinkedList;
import java.util.List;

public class InfoSends {

    List<Timer> timers = new LinkedList<>();

    public InfoSends() {
    }

    public InfoSends(CustomPacketBuffer customPacketBuffer) {
        int size = customPacketBuffer.readInt();
        for (int i = 0; i < size; i++) {
            timers.add(new Timer(customPacketBuffer));
        }
    }

    public void addTimer(Timer timer) {
        timers.add(timer);
    }

    public CustomPacketBuffer writeBuffer() {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeInt(timers.size());
        for (Timer timer : timers) {
            timer.writeBuffer(customPacketBuffer);
        }
        return customPacketBuffer;
    }

    public List<Timer> getTimers() {
        return timers;
    }

}
