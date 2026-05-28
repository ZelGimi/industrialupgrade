package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPreCraft;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.io.IOException;

public class PacketUpdatePreCraft implements IPacket {

    public PacketUpdatePreCraft() {

    }


    public PacketUpdatePreCraft(BlockEntityPreCraft base, boolean input, int slotId, SameStack sameStack, boolean item_to_fluid) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, base.getLevel());
            EncoderHandler.encode(customPacketBuffer, base.getPos());
            customPacketBuffer.writeBoolean(input);
            customPacketBuffer.writeBoolean(item_to_fluid);
            customPacketBuffer.writeInt(slotId);
            EncoderHandler.encode(customPacketBuffer, sameStack.writeToNBT());
        } catch (IOException ignored) {
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    public PacketUpdatePreCraft(Player player, BlockPos pos, int i, SameStack sameStack) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, player.getLevel());
            EncoderHandler.encode(customPacketBuffer, pos);
            customPacketBuffer.writeInt(i);
            EncoderHandler.encode(customPacketBuffer, sameStack.writeToNBT());
        } catch (IOException ignored) {
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 121;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            BlockEntityPreCraft preCraft = (BlockEntityPreCraft) level.getBlockEntity(pos);
            if (level.getBlockEntity(pos) instanceof BlockEntityPreCraft controller && controller.network != null) {
                int slotId = customPacketBuffer.readInt();
                SameStack sameStack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer));
                if (!sameStack.isItem() && sameStack.isFluid()) {
                    ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                    ModUtils.nbt(stack).putBoolean("type_recipe", true);
                    FluidHandlerFix.getFluidHandler(stack).fill(sameStack.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                    sameStack.setStack(stack);
                } else if (sameStack.isFluid() && sameStack.isItem()) {
                    ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                    ModUtils.nbt(stack).putBoolean("type_recipe", true);
                    FluidHandlerFix.getFluidHandler(stack).fill(sameStack.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                    sameStack.setStack(stack);
                }
                controller.inputItems.integerList.set(slotId, sameStack.getAmount(false));

                controller.inputItems.set(slotId, sameStack.getStack());
                controller.inputItems.sameStackList.set(slotId, sameStack);

                controller.updateCraft();
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

