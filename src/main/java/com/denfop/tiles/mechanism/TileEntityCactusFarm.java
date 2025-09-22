package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerCactusFarm;
import com.denfop.gui.GuiCactusFarm;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCactusFarm extends TileEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    public final InventoryOutput slot;
    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;

    public TileEntityCactusFarm() {
        this.slot = new InventoryOutput(this, 9);
        this.energy = this.addComponent(Energy.asBasicSink(this, 4000, 5));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract
        );
    }

    @Override
    public ContainerCactusFarm getGuiContainer(final EntityPlayer var1) {
        return new ContainerCactusFarm(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCactusFarm(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.cactus_farm;
    }

    private void breakCactusInRadius() {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                BlockPos targetPos = pos.add(x, 1, z);
                Block block = world.getBlockState(targetPos).getBlock();
                if (block instanceof BlockCactus && this.energy.getEnergy() >= 100) {
                    breakCactus(targetPos);
                    this.energy.useEnergy(100);
                    return;
                }
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

    private void breakCactus(BlockPos startPos) {
        BlockPos currentPos = startPos;
        while (!world.isAirBlock(currentPos)) {
            IBlockState state = world.getBlockState(currentPos);
            if (state.getBlock() instanceof BlockCactus) {
                List<ItemStack> drops = state.getBlock().getDrops(world, currentPos, state, 0);
                for (ItemStack drop : drops) {
                    this.slot.add(drop);
                }
                world.setBlockToAir(currentPos);
            } else {
                break;
            }
            currentPos = currentPos.up();
        }
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            this.breakCactusInRadius();
        }
    }

}
