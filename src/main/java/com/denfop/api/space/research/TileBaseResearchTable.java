package com.denfop.api.space.research;

import com.denfop.api.space.EnumLevels;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.FakePlayer;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.space.research.event.ResearchTableLoadEvent;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileBaseResearchTable extends TileEntityInventory implements IResearchTable {

    public Map<IBody, SpaceOperation> map;
    public List<SpaceOperation> fakeBodySpaceOperationMap;
    public IContainerBlock storageBlocks;
    public EnumLevels level;
    private FakePlayer player;

    public TileBaseResearchTable() {
        this.map = new HashMap<>();
        this.player = null;
        this.storageBlocks = null;
        this.fakeBodySpaceOperationMap = new ArrayList<>();
        this.level = null;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        MinecraftForge.EVENT_BUS.post(new ResearchTableLoadEvent(this.getWorld(), this));
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof EntityPlayer) {
            this.player = new FakePlayer(placer.getName(), placer.getEntityData());
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        return super.writeToNBT(nbt);
    }

    @Override
    public Map<IBody, SpaceOperation> getSpaceBody() {
        return SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(this.player);
    }

    @Override
    public FakePlayer getPlayer() {
        return this.player;
    }

    @Override
    public List<SpaceOperation> getSpaceFakeBody() {
        return SpaceNet.instance.getFakeSpaceSystem().getSpaceOperationMap(this.player);
    }

    @Override
    public IContainerBlock getContainerBlock() {
        return this.storageBlocks;
    }

    @Override
    public void setContainerBlock(final IContainerBlock containerBlock) {
        this.storageBlocks = containerBlock;
    }

    @Override
    public EnumLevels getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final EnumLevels level) {
        this.level = level;
    }

}
