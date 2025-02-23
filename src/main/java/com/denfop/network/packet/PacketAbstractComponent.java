package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.componets.AbstractComponent;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PacketAbstractComponent implements IPacket {


    public PacketAbstractComponent() {
    }

    public PacketAbstractComponent(
            TileEntityBlock te,
            String componentName,
            EntityPlayerMP player,
            CustomPacketBuffer data
    ) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        try {
            buffer.writeByte(this.getId());
            EncoderHandler.encode(buffer, te.getPos(), false);
            buffer.writeString(componentName);
            buffer.writeBytes(data);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer, player);
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final BlockPos pos1;
        try {
            pos1 = DecoderHandler.decode(is, BlockPos.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String componentName = is.readString();


        final byte[] data = new byte[is.writerIndex()-is.readerIndex()];
        is.readBytes(data);
        IUCore.proxy.requestTick(false, () -> {
            World world = Minecraft.getMinecraft().world;

                TileEntity teRaw = world.getTileEntity(pos1);
                if (teRaw instanceof TileEntityBlock) {
                    TileEntityBlock tile = (TileEntityBlock) teRaw;
                    AbstractComponent component = tile.getComp(componentName);

                    if (component != null) {
                        try {

                            component.onNetworkUpdate(new CustomPacketBuffer(data));
                        } catch (IOException var6) {
                            throw new RuntimeException(var6);
                        }
                    }
                }

        });
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
