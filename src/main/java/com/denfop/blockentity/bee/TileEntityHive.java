package com.denfop.blockentity.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.Bee;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datagen.DamageTypes;
import com.denfop.items.energy.ItemNet;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.List;

import static com.denfop.blockentity.bee.BlockEntityApiary.hasDamagePlayer;

public class TileEntityHive extends BlockEntityBase {

    private final Bee bee;

    public TileEntityHive(Bee bee, MultiBlockEntity tileBlock, BlockPos pos, BlockState state) {
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
        stack.set(DataComponentsInit.BEE, bee.getId());
        stack.set(DataComponentsInit.SWARM, WorldBaseGen.random.nextInt(bee.getMaxSwarm() / 2) + 15);
        return Collections.singletonList(stack);
    }

    @Override
    public List<ItemStack> getAuxDrops(final int fortune) {
        return super.getAuxDrops(fortune);
    }

    public void onClicked(Player player) {
        if (hasDamagePlayer)
            player.hurt(player.damageSources().source(DamageTypes.beeObject), 5);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return null;
    }

    @Override
    public BlockTileEntity getBlock() {
        return null;
    }

}
