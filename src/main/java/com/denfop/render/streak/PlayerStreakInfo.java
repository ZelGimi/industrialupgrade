package com.denfop.render.streak;

import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStreakInfo {

    private boolean render;
    private boolean renderPlayer;
    private RGB rgb;
    private boolean rainbow;

    public PlayerStreakInfo(RGB rgb, boolean rainbow, boolean render, boolean renderPlayer) {
        this.rgb = rgb;
        this.rainbow = rainbow;
        this.render = render;
        this.renderPlayer = renderPlayer;
    }

    public PlayerStreakInfo(NBTTagCompound nbtTagCompound) {
        this.rgb = new RGB(nbtTagCompound.getShort("red"), nbtTagCompound.getShort("green"), nbtTagCompound.getShort("blue"));
        this.rainbow = nbtTagCompound.getBoolean("rainbow");
        this.render = nbtTagCompound.getBoolean("render");
        this.renderPlayer = nbtTagCompound.getBoolean("renderPlayer");
    }

    public PlayerStreakInfo(CustomPacketBuffer customPacketBuffer) {
        this.rgb = new RGB((short) (customPacketBuffer.readByte()+128), (short) (customPacketBuffer.readByte()+128), (short) (customPacketBuffer.readByte()+128));
        this.rainbow = customPacketBuffer.readBoolean();
        this.render = customPacketBuffer.readBoolean();
        this.renderPlayer = customPacketBuffer.readBoolean();
    }

    public NBTTagCompound writeNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("red", rgb.getRed());
        nbt.setShort("blue", rgb.getBlue());
        nbt.setShort("green", rgb.getGreen());
        nbt.setBoolean("rainbow", rainbow);
        nbt.setBoolean("render", render);
        nbt.setBoolean("renderPlayer", render);
        return nbt;
    }

    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
        packetBuffer.writeByte(rgb.getRed()-128);
        packetBuffer.writeByte(rgb.getBlue()-128);
        packetBuffer.writeByte(rgb.getGreen()-128);
        packetBuffer.writeBoolean(this.rainbow);
        packetBuffer.writeBoolean(this.render);
        packetBuffer.writeBoolean(this.renderPlayer);
        return packetBuffer;
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public void setRainbow(final boolean rainbow) {
        this.rainbow = rainbow;
    }

    public RGB getRgb() {
        return rgb;
    }

    public void setRgb(final RGB rgb) {
        this.rgb = rgb;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(final boolean render) {
        this.render = render;
    }

    public boolean isRenderPlayer() {
        return renderPlayer;
    }

    public void setRenderPlayer(final boolean renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

}
