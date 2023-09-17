package com.denfop.api.recipe;

import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public class RecipeInfo {

    private final ItemStack stack;
    private final double col;

    public RecipeInfo(ItemStack stack, double col) {
        this.stack = stack;
        this.col = col;
    }

    public RecipeInfo(NBTTagCompound tagCompound) {
        this.stack = new ItemStack(tagCompound.getCompoundTag("stack"));
        this.col = tagCompound.getDouble("matter");
    }
    public RecipeInfo(CustomPacketBuffer packetBuffer) {
        this.col = packetBuffer.readDouble();
        try {
            this.stack = (ItemStack) DecoderHandler.decode(packetBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public CustomPacketBuffer getPacket(){
        CustomPacketBuffer packetBuffer = new CustomPacketBuffer();
        packetBuffer.writeDouble(this.col);
        try {
            EncoderHandler.encode(packetBuffer, this.stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packetBuffer;
    }
    public double getCol() {
        return col;
    }

    public ItemStack getStack() {
        return stack;
    }

    public NBTTagCompound writeCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound stackNBT = new NBTTagCompound();
        stack.writeToNBT(stackNBT);
        tag.setTag("stack", stackNBT);
        tag.setDouble("matter", col);
        return tag;
    }

}
