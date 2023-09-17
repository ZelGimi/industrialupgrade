package com.denfop.render.transport;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;

import java.util.List;

public class DataCable {

    private  IBakedModel bakedModel;
    private  ModelBaseCable modelCables;
    private  byte connect;
    private  ItemStack itemStack;

    public DataCable(byte connect, ModelBaseCable modelCables, ItemStack itemStack, IBakedModel bakedModel){
        this.connect = connect;
        this.modelCables=modelCables;
        this.itemStack=itemStack;
        this.bakedModel=bakedModel;
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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ModelBaseCable getModelCables() {
        return modelCables;
    }

    public void setConnect(final byte connect) {
        this.connect = connect;
    }

    public void setItemStack(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setModelCables(final ModelBaseCable modelCables) {
        this.modelCables = modelCables;
    }

}
