package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PacketRadiationUpdateValue implements IPacket {

    public PacketRadiationUpdateValue() {

    }

    public PacketRadiationUpdateValue(EntityPlayer player, double value) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        buffer.writeByte(this.getId());
        buffer.writeDouble(value);
        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }

    @Override
    public byte getId() {
        return 99;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            customPacketBuffer.readDouble();
            return;
        }
        final NBTTagCompound nbt = entityPlayer.getEntityData();
        nbt.setDouble("radiation", customPacketBuffer.readDouble());
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
