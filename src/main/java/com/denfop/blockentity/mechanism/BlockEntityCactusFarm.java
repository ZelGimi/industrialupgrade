package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
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
import com.denfop.containermenu.ContainerMenuCactusFarm;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenCactusFarm;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityCactusFarm extends BlockEntityInventory implements BlockEntityUpgrade {

    private static final int RADIUS = 4;
    public final InventoryOutput slot;
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

    public BlockEntityCactusFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.cactus_farm, pos, state);
        this.slot = new InventoryOutput(this, 9);
        this.energy = this.addComponent(Energy.asBasicSink(this, 4000, 5));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Transformer, EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract
        );
    }

    @Override
    public ContainerMenuCactusFarm getGuiContainer(final Player var1) {
        return new ContainerMenuCactusFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenCactusFarm((ContainerMenuCactusFarm) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.cactus_farm;
    }

    private void breakCactusInRadius() {
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int z = -RADIUS; z <= RADIUS; z++) {
                BlockPos targetPos = pos.offset(x, 1, z);
                BlockState state = level.getBlockState(targetPos);
                Block block = state.getBlock();

                if (block instanceof CactusBlock && this.energy.getEnergy() >= 100) {
                    breakCactus(targetPos);
                    this.energy.useEnergy(100);
                    return;
                }
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;
    }

    private void breakCactus(BlockPos startPos) {
        BlockPos currentPos = startPos;
        while (!level.getBlockState(currentPos).isAir()) {
            BlockState state = level.getBlockState(currentPos);
            Block block = state.getBlock();

            if (block instanceof CactusBlock) {
                List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, currentPos, null);

                for (ItemStack drop : drops) {
                    this.slot.add(drop);
                }

                level.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
            } else {
                break;
            }

            currentPos = currentPos.above();
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
            this.breakCactusInRadius();
        }
    }

}
