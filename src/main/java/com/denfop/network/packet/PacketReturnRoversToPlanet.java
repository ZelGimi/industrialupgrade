package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.rovers.Rovers;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import crazypants.enderio.base.scheduler.Space;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.UUID;

public class PacketReturnRoversToPlanet implements IPacket {

    public PacketReturnRoversToPlanet() {

    }

    public PacketReturnRoversToPlanet(TileEntityResearchTableSpace base, EntityPlayer player, IBody iBody) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, base.getWorld());
            EncoderHandler.encode(customPacketBuffer, base.getPos());
            EncoderHandler.encode(customPacketBuffer, player.getUniqueID());
            customPacketBuffer.writeBoolean(iBody != null);
            if (iBody != null) {
                customPacketBuffer.writeString(iBody.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 43;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            World world = (World) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUniqueID().equals(uuid)){
                boolean hasBody = customPacketBuffer.readBoolean();
                if (hasBody){
                    String body = customPacketBuffer.readString();
                    IBody body1 = SpaceNet.instance.getBodyFromName(body);
                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof  TileEntityResearchTableSpace){
                        TileEntityResearchTableSpace tileEntityResearchTableSpace = (TileEntityResearchTableSpace) tile;
                        if (tileEntityResearchTableSpace.getPlayer().equals(uuid) ){
                            SpaceNet.instance.getResearchSystem().returnOperation(body1,tileEntityResearchTableSpace);
                       }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
