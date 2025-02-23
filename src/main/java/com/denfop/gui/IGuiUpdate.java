package com.denfop.gui;

import com.denfop.network.packet.CustomPacketBuffer;

public interface IGuiUpdate {

    void readField(String string, CustomPacketBuffer packetBuffer);

}
