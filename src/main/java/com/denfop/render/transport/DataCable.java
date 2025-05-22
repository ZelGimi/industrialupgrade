package com.denfop.render.transport;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;

public class DataCable {

    private IBakedModel bakedModel;
    private byte connect;
    private ItemStack itemStack;

    public DataCable(byte connect, ItemStack itemStack, IBakedModel bakedModel) {
        this.connect = connect;
        this.itemStack = itemStack;
        this.bakedModel = bakedModel;
    }

    public IBakedModel getBakedModel() {
        return bakedModel;
    }

    public void setBakedModel(final IBakedModel bakedModel) {
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
