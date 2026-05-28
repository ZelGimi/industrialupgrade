package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.blockentity.storage.BlockEntityPreCraft;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.utils.FluidHandlerFix;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.io.IOException;

public class PacketChangeSameStack implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketChangeSameStack() {

    }


    public PacketChangeSameStack(BlockEntityPatternMonitor base, boolean input, int slotId, SameStack sameStack, boolean item_to_fluid) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(base.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, base.getLevel());
            EncoderHandler.encode(customPacketBuffer, base.getPos());
            customPacketBuffer.writeBoolean(input);
            customPacketBuffer.writeBoolean(item_to_fluid);
            customPacketBuffer.writeInt(slotId);
            EncoderHandler.encode(customPacketBuffer, sameStack.writeToNBT(base.registryAccess()));
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
    }

    public PacketChangeSameStack(BlockEntityPreCraft base, boolean input, int slotId, SameStack sameStack, boolean item_to_fluid) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(base.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, base.getLevel());
            EncoderHandler.encode(customPacketBuffer, base.getPos());
            customPacketBuffer.writeBoolean(input);
            customPacketBuffer.writeBoolean(item_to_fluid);
            customPacketBuffer.writeInt(slotId);
            EncoderHandler.encode(customPacketBuffer, sameStack.writeToNBT(base.registryAccess()));
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
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
                SameStack sameStack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer), controller.registryAccess());
                if (item_to_fluid) {
                    if (!sameStack.isItem() && sameStack.isFluid()) {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
                        FluidHandlerFix.getFluidHandler(stack).fill(sameStack.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                        sameStack.setStack(stack);
                    } else if (sameStack.isFluid() && sameStack.isItem()) {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
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
                        sameStack.getStack().remove(DataComponentsInit.TYPE_RECIPE);
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
                SameStack sameStack = SameStack.readFromNBT((CompoundTag) DecoderHandler.decode(customPacketBuffer), controller.registryAccess());
                if (item_to_fluid) {
                    if (!sameStack.isItem() && sameStack.isFluid()) {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
                        FluidHandlerFix.getFluidHandler(stack).fill(sameStack.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
                        sameStack.setStack(stack);
                    } else if (sameStack.isFluid() && sameStack.isItem()) {
                        ItemStack stack = new ItemStack(IUItem.reinforcedFluidCell.getItem());
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
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
                        sameStack.getStack().remove(DataComponentsInit.TYPE_RECIPE);
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

