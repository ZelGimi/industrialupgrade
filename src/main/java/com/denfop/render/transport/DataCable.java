package com.denfop.render.transport;


import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class DataCable {

    private BakedModel bakedModel;
    private byte connect;
    private ItemStack itemStack;

    public DataCable(byte connect, ItemStack itemStack, BakedModel bakedModel) {
        this.connect = connect;
        this.itemStack = itemStack;
        this.bakedModel = bakedModel;
    }

    public BakedModel getBakedModel() {
        return bakedModel;
    }

    public void setBakedModel(final BakedModel bakedModel) {
        this.bakedModel = bakedModel;
    }

    public byte getConnect() {
        return connect;
    }

    public void setConnect(final byte connect) {
        this.connect = connect;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }


}
