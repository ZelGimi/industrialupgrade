package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.IBee;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.items.energy.ItemNet;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

public class TileEntityHive extends TileEntityBlock {

    private final IBee bee;

    public TileEntityHive(IBee bee) {
        this.bee = bee;
    }


    @Override
    public boolean canEntityDestroy(final Entity entity) {
        if ((entity instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) entity;
            return player.getHeldItemMainhand().getItem() instanceof ItemNet;
        }
        return false;
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        ItemStack stack = new ItemStack(IUItem.jarBees);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("bee_id", bee.getId());
        nbt.setInteger("swarm", WorldBaseGen.random.nextInt(bee.getMaxSwarm() / 2) + 15);
        return Collections.singletonList(stack);
    }

    @Override
    public List<ItemStack> getAuxDrops(final int fortune) {
        return super.getAuxDrops(fortune);
    }

    public void onClicked(EntityPlayer player) {
        player.attackEntityFrom(IUDamageSource.bee, 5);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return null;
    }

    @Override
    public BlockTileEntity getBlock() {
        return null;
    }

}
