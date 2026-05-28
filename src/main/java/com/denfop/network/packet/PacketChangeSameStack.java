package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
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

public class PacketChangeSameStack implements IPacket {

    public PacketChangeSameStack() {

    }


    public PacketChangeSameStack(BlockEntityPatternMonitor base, boolean input, int slotId, SameStack sameStack, boolean item_to_fluid) {
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

    public PacketChangeSameStack(BlockEntityPreCraft base, boolean input, int slotId, SameStack sameStack, boolean item_to_fluid) {
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

    @Override
    public byte getId() {
        return 125;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            if (level.getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                boolean input = customPacketBuffer.readBoolean();
                boolean item_to_fluid = customPacketBuffer.readBoolean();
                int slotId = customPacketBuffer.readInt();
                SameStack sameStack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer));
                if (item_to_fluid) {
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
                } else {
                    if (sameStack.getStack().isEmpty()) {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        FluidHandlerFix.getFluidHandler(stack).fill(sameStack.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                        sameStack.setStack(stack);
                    }
                    if (sameStack.getStack().getItem() == IUItem.reinforcedFluidCell.getItem()) {
                        ModUtils.nbt(sameStack.getStack()).remove("type_recipe");
                    }
                }
                controller.modeCraft = 1;

                if (input) {
                    controller.inputItems.set(slotId, sameStack.getStack());
                    controller.inputItems.sameStackList.set(slotId, sameStack);
                    controller.inputItems.booleanList.set(slotId, item_to_fluid);
                } else {

                    controller.outputItems.set(slotId, sameStack.getStack());
                    controller.outputItems.sameStackList.set(slotId, sameStack);
                    controller.outputItems.booleanList.set(slotId, item_to_fluid);
                }
            }
            if (level.getBlockEntity(pos) instanceof BlockEntityPreCraft controller && controller.network != null) {
                boolean input = customPacketBuffer.readBoolean();
                boolean item_to_fluid = customPacketBuffer.readBoolean();
                int slotId = customPacketBuffer.readInt();
                SameStack sameStack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer));
                if (item_to_fluid) {
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
                } else {
                    if (sameStack.getStack().isEmpty()) {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        FluidHandlerFix.getFluidHandler(stack).fill(sameStack.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                        sameStack.setStack(stack);
                    }
                    if (sameStack.getStack().getItem() == IUItem.reinforcedFluidCell.getItem()) {
                        ModUtils.nbt(sameStack.getStack()).remove("type_recipe");
                    }
                }
                controller.inputItems.set(slotId, sameStack.getStack());
                controller.inputItems.sameStackList.set(slotId, sameStack);
                controller.inputItems.booleanList.set(slotId, item_to_fluid);
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

