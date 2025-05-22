package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
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
import com.denfop.container.ContainerTreeBreaker;
import com.denfop.gui.GuiTreeBreaker;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public TileEntityTreeBreaker() {
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
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.tree_breaker;
    }

    @Override
    public ContainerTreeBreaker getGuiContainer(final EntityPlayer var1) {
        return new ContainerTreeBreaker(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiTreeBreaker(getGuiContainer(var1));
    }


    private void breakTreesInRadius() {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                BlockPos targetPos = pos.add(x, 0, z);
                Block block = world.getBlockState(targetPos).getBlock();
                if (block instanceof BlockLog && this.energy.getEnergy() >= 100) {
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
            IBlockState state = world.getBlockState(blockPos);
            Block block = state.getBlock();
            List<ItemStack> drops = block.getDrops(world, blockPos, state, 0);
            for (ItemStack drop : drops) {
                this.slot.add(drop);
            }
            world.setBlockToAir(blockPos);
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

    private void findConnectedTreeBlocks(BlockPos startPos, List<BlockPos> blocksToBreak) {
        if (blocksToBreak.contains(startPos)) {
            return;
        }

        Block block = world.getBlockState(startPos).getBlock();
        if (block instanceof BlockLog || block instanceof BlockLeaves) {
            blocksToBreak.add(startPos);
            for (BlockPos offset : BlockPos.getAllInBox(startPos.add(-1, -1, -1), startPos.add(1, 1, 1))) {
                findConnectedTreeBlocks(offset, blocksToBreak);
            }
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            this.breakTreesInRadius();
        }
    }

}
