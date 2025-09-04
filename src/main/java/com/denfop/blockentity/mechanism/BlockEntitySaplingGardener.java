package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSaplingGardener;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSaplingGardener;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntitySaplingGardener extends BlockEntityInventory implements BlockEntityUpgrade {

    private static final int RADIUS = 4;
    public final Inventory slot;
    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1)
    );
    private ComponentVisibleArea visible;

    public BlockEntitySaplingGardener(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.sapling_gardener, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof IPlantable && ((IPlantable) ((BlockItem) stack.getItem()).getBlock()).getPlantType(
                        level,
                        getBlockPos()) == PlantType.PLAINS;
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Transformer, EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemInput
        );
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        visible.aabb = searchArea;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.sapling_gardener;
    }

    @Override
    public ContainerMenuSaplingGardener getGuiContainer(final Player var1) {
        return new ContainerMenuSaplingGardener(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSaplingGardener((ContainerMenuSaplingGardener) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 40 == 0) {
            plantSaplingsInRadius();
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

    private void plantSaplingsInRadius() {
        for (int x = -RADIUS; x <= RADIUS; x += 2) {
            for (int z = -RADIUS; z <= RADIUS; z += 2) {
                BlockPos targetPos = pos.offset(x, 0, z);

                if (canPlantSaplingAt(targetPos) && this.energy.canUseEnergy(25)) {
                    ItemStack saplingStack = this.slot.get(0);
                    if (!saplingStack.isEmpty()) {
                        this.energy.useEnergy(25);
                        plantSapling(targetPos, saplingStack);
                        if (this.energy.getEnergy() < 25) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean canPlantSaplingAt(BlockPos pos) {
        BlockState stateAt = level.getBlockState(pos);
        if (!stateAt.isAir()) {
            return false;
        }

        BlockState below = level.getBlockState(pos.below());
        Block blockBelow = below.getBlock();

        return blockBelow == Blocks.DIRT || blockBelow == Blocks.GRASS_BLOCK;
    }

    private void plantSapling(BlockPos targetPos, ItemStack saplingStack) {
        if (saplingStack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof net.minecraft.world.level.block.SaplingBlock) {
                BlockState stateToPlace = block.defaultBlockState();
                level.setBlock(targetPos, stateToPlace, 3);
                saplingStack.shrink(1);
            }
        }
    }


}
