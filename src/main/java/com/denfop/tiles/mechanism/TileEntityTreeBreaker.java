package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTreeBreaker;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiTreeBreaker;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityTreeBreaker extends TileEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    public final InvSlotOutput slot;
    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;

    public TileEntityTreeBreaker(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.tree_breaker,pos,state);
        this.slot = new InvSlotOutput(this, 18);
        this.energy = this.addComponent(Energy.asBasicSink(this, 4000, 5));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.tree_breaker;
    }

    @Override
    public ContainerTreeBreaker getGuiContainer(final Player var1) {
        return new ContainerTreeBreaker(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiTreeBreaker((ContainerTreeBreaker) menu);
    }


    private void breakTreesInRadius() {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                BlockPos targetPos = pos.offset(x, 0, z);
                BlockState state = level.getBlockState(targetPos);
                Block block = state.getBlock();

                if ((block instanceof RotatedPillarBlock) && this.energy.getEnergy() >= 100) {
                    breakTree(targetPos);
                    this.energy.useEnergy(100);
                    return;
                }
            }
        }
    }

    private void breakTree(BlockPos startPos) {
        List<BlockPos> blocksToBreak = new ArrayList<>();
        findConnectedTreeBlocks(startPos, blocksToBreak);

        for (BlockPos blockPos : blocksToBreak) {
            BlockState state = level.getBlockState(blockPos);
            Block block = state.getBlock();

            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, blockPos, null);
            for (ItemStack drop : drops) {
                this.slot.add(drop);
            }

            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    private void findConnectedTreeBlocks(BlockPos startPos, List<BlockPos> blocksToBreak) {
        if (blocksToBreak.contains(startPos)) return;

        BlockState state = level.getBlockState(startPos);
        Block block = state.getBlock();

        if (block instanceof RotatedPillarBlock || block instanceof LeavesBlock) {
            blocksToBreak.add(startPos);
            for (BlockPos offset : BlockPos.betweenClosed(startPos.offset(-1, -1, -1), startPos.offset(1, 1, 1))) {
                findConnectedTreeBlocks(offset.immutable(), blocksToBreak);
            }
        }
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.sapling_gardener.info"));
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
    }



    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 40 == 0) {
            this.breakTreesInRadius();
        }
    }

}
