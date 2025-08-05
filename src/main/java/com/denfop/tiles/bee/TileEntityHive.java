package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.IBee;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.datagen.DamageTypes;
import com.denfop.items.energy.ItemNet;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.List;

public class TileEntityHive extends TileEntityBlock {

    private final IBee bee;

    public TileEntityHive(IBee bee, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
        this.bee = bee;
    }


    @Override
    public boolean canEntityDestroy(final Entity entity) {
        if ((entity instanceof Player)) {
            Player player = (Player) entity;
            return player.getMainHandItem().getItem() instanceof ItemNet;
        }
        return false;
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        ItemStack stack = new ItemStack(IUItem.jarBees.getStack(0));
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putInt("bee_id", bee.getId());
        nbt.putInt("swarm", WorldBaseGen.random.nextInt(bee.getMaxSwarm() / 2) + 15);
        return Collections.singletonList(stack);
    }

    @Override
    public List<ItemStack> getAuxDrops(final int fortune) {
        return super.getAuxDrops(fortune);
    }

    public void onClicked(Player player) {

        player.hurt(new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.beeObject)), 5);
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
