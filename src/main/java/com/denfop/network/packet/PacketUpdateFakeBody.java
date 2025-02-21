package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.fakebody.FakeAsteroid;
import com.denfop.api.space.fakebody.FakePlanet;
import com.denfop.api.space.fakebody.FakeSatellite;
import com.denfop.api.space.fakebody.IFakeAsteroid;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.IFakePlanet;
import com.denfop.api.space.fakebody.IFakeSatellite;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketUpdateFakeBody implements IPacket {

    public PacketUpdateFakeBody() {

    }

    ;

    public PacketUpdateFakeBody(TileEntityResearchTableSpace tileEntityResearchTableSpace, IFakeBody fakeBody) {
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
        packetBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(packetBuffer, tileEntityResearchTableSpace.getWorld());
            EncoderHandler.encode(packetBuffer, tileEntityResearchTableSpace.getPos());
            if (fakeBody instanceof IFakePlanet) {
                packetBuffer.writeByte(0);
            }
            if (fakeBody instanceof IFakeSatellite) {
                packetBuffer.writeByte(1);
            }
            if (fakeBody instanceof IFakeAsteroid) {
                packetBuffer.writeByte(2);
            }
            if (fakeBody == null) {
                packetBuffer.writeByte(3);
            }
            if (fakeBody != null) {
                EncoderHandler.encode(packetBuffer, fakeBody.writeNBTTagCompound(new NBTTagCompound()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getServer().sendPacket(packetBuffer);
    }

    @Override
    public byte getId() {
        return 41;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            World world = (World) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            byte id = customPacketBuffer.readByte();
            if (entityPlayer.getEntityWorld().provider.getDimension() == world.provider.getDimension()) {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileEntityResearchTableSpace) {

                    if (id == 0) {
                        NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(customPacketBuffer);
                        FakePlanet fakePlanet = new FakePlanet(nbt);
                        ((TileEntityResearchTableSpace) tile).fakeBody = fakePlanet;
                    }
                    if (id == 1) {
                        NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(customPacketBuffer);
                        FakeSatellite fakePlanet = new FakeSatellite(nbt);
                        ((TileEntityResearchTableSpace) tile).fakeBody = fakePlanet;
                    }
                    if (id == 2) {
                        NBTTagCompound nbt = (NBTTagCompound) DecoderHandler.decode(customPacketBuffer);
                        FakeAsteroid fakePlanet = new FakeAsteroid(nbt);
                        ((TileEntityResearchTableSpace) tile).fakeBody = fakePlanet;
                    }
                    if (id == 3){
                        ((TileEntityResearchTableSpace) tile).fakeBody = null;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
