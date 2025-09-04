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
import com.denfop.containermenu.ContainerMenuCowFarm;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenCowFarm;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.denfop.utils.ModUtils.getVecFromVec3i;

public class BlockEntityCowFarm extends BlockEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    private static final int MAX_COWS = 20;
    public final Inventory slotSeeds;
    public final InventoryOutput output;
    public final Energy energy;
    public final InventoryOutput output1;
    public final InventoryUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    private final ComponentVisibleArea visible;
    AABB searchArea = new AABB(
            getVecFromVec3i(pos.offset(-RADIUS, -RADIUS, -RADIUS)),
            getVecFromVec3i(pos.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1))
    );

    public BlockEntityCowFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.cow_farm, pos, state);
        this.slotSeeds = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == Items.WHEAT;
            }
        };
        this.output = new InventoryOutput(this, 9);
        this.output1 = new InventoryOutput(this, 4);
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
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.cow_farm;
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;

    }

    @Override
    public ContainerMenuCowFarm getGuiContainer(final Player var1) {
        return new ContainerMenuCowFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenCowFarm((ContainerMenuCowFarm) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<Cow> cows = level.getEntitiesOfClass(Cow.class, searchArea);


            if (cows.size() < MAX_COWS) {
                breedCows(cows);
            }


            killOldCows(cows);
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.cow_farm.info"));
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
    }


    private void breedCows(List<Cow> cows) {
        for (int i = 0; i < cows.size(); i++) {
            for (int j = i + 1; j < cows.size(); j++) {
                Cow cow1 = cows.get(i);
                Cow cow2 = cows.get(j);

                if (cow1.getAge() == 0 &&
                        cow2.getAge() == 0 &&
                        !cow1.isInLove() &&
                        !cow2.isInLove() &&
                        cow1.getLoveCause() == null &&
                        cow2.getLoveCause() == null &&
                        !slotSeeds.isEmpty() &&
                        slotSeeds.get(0).getCount() >= 2) {

                    cow1.setInLove(null);
                    cow2.setInLove(null);
                    slotSeeds.get(0).shrink(2);
                    break;
                }
            }
        }
    }

    private void killOldCows(List<Cow> cows) {
        for (int i = cows.size() - 1; i >= MAX_COWS; i--) {
            Cow cow = cows.get(i);
            cow.discard();
            output.add(new ItemStack(Items.BEEF, 1));
            if (level.random.nextBoolean()) {
                output.add(new ItemStack(Items.LEATHER, level.random.nextInt(2) + 1));
            }
        }
    }


}
