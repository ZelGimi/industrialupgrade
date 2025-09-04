package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.componets.Fluids;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;

public class PacketDrainFluidPipette implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketDrainFluidPipette() {

    }

    public PacketDrainFluidPipette(BlockEntity te, String tank, int amount) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(32, te.getLevel().registryAccess());
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, te, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.writeString(tank);
        buffer.writeInt(amount);
        this.buffer = buffer;
        buffer.flip();
        IUCore.network.getClient().sendPacket(this);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 103;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        final Object teDeferred;
        try {
            teDeferred = DecoderHandler.decodeDeferred(is, BlockEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BlockEntityBase te = DecoderHandler.getValue(teDeferred);
        Fluids fluids = te.getComp(Fluids.class);
        if (fluids != null) {
            String nameTank = is.readString();
            int amount = is.readInt();
            FluidTank tank = fluids.getFluidTank(nameTank);
            FluidStack fluidStack = tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            ItemStack stack = entityPlayer.containerMenu.getCarried();
            FluidHandlerFix.getFluidHandler(stack).fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }


}
