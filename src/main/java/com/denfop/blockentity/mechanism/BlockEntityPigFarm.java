package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuPigFarm;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPigFarm;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityPigFarm extends BlockEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    private static final int MAX_PIGS = 20;
    public final Inventory slotSeeds;
    public final InventoryOutput output;
    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    private final ComponentVisibleArea visible;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1)
    );

    public BlockEntityPigFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.pig_farm, pos, state);
        this.slotSeeds = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == Items.CARROT;
            }
        };
        this.output = new InventoryOutput(this, 9);
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));

        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
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
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.pig_farm;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;
    }

    @Override
    public ContainerMenuPigFarm getGuiContainer(final Player var1) {
        return new ContainerMenuPigFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenPigFarm((ContainerMenuPigFarm) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<Pig> pigs = level.getEntitiesOfClass(Pig.class, searchArea);


            if (pigs.size() < MAX_PIGS) {
                breedPigs(pigs);
            }


            killOldPigs(pigs);
        }

    }


    private void breedPigs(List<Pig> pigs) {
        for (int i = 0; i < pigs.size(); i++) {
            for (int j = i + 1; j < pigs.size(); j++) {
                Pig pig1 = pigs.get(i);
                Pig pig2 = pigs.get(j);

                if (!pig1.isBaby() && !pig2.isBaby() &&
                        !pig1.isInLove() && !pig2.isInLove() &&
                        this.slotSeeds.get(0).getCount() >= 2) {

                    pig1.setInLove(null);
                    pig2.setInLove(null);
                    this.slotSeeds.get(0).shrink(2);
                    break;
                }
            }
        }
    }

    private void killOldPigs(List<Pig> pigs) {
        for (int i = pigs.size() - 1; i >= MAX_PIGS; i--) {
            Pig pig = pigs.get(i);
            pig.remove(Entity.RemovalReason.KILLED);

            this.output.add(new ItemStack(Items.PORKCHOP, 1));
        }
    }


}
