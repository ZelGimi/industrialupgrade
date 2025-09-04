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
import com.denfop.containermenu.ContainerMenuChickenFarm;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenChickenFarm;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.denfop.utils.ModUtils.getVecFromVec3i;

public class BlockEntityChickenFarm extends BlockEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    private static final int MAX_CHICKENS = 12;
    public final Inventory slotSeeds;
    public final InventoryOutput output;
    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    private final ComponentVisibleArea visible;
    AABB searchArea = new AABB(
            getVecFromVec3i(pos.offset(-RADIUS, -RADIUS, -RADIUS)),
            getVecFromVec3i(pos.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1))
    );
    List<LevelChunk> chunks = new ArrayList<>();

    public BlockEntityChickenFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.chicken_farm, pos, state);
        this.slotSeeds = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == Items.WHEAT_SEEDS;
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

    @Override
    public ContainerMenuChickenFarm getGuiContainer(final Player var1) {
        return new ContainerMenuChickenFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenChickenFarm((ContainerMenuChickenFarm) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.chicken_farm;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.chicken_farm.info"));
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
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<Chicken> chickens = level.getEntitiesOfClass(Chicken.class, searchArea);
            collectEggs(searchArea);


            if (chickens.size() < MAX_CHICKENS) {
                breedChickens(chickens);
            }


            if (chickens.size() > MAX_CHICKENS) {
                killExcessChickens(chickens);
            }
        }
    }

    private void killExcessChickens(List<Chicken> chickens) {
        for (int i = chickens.size() - 1; i >= MAX_CHICKENS; i--) {
            Chicken chicken = chickens.get(i);
            chicken.discard();
            this.output.add(new ItemStack(Items.CHICKEN, 1));
            if (level.random.nextBoolean()) {
                this.output.add(new ItemStack(Items.FEATHER, level.random.nextInt(2) + 1));
            }
        }
    }


    private void breedChickens(List<Chicken> chickens) {
        for (int i = 0; i < chickens.size(); i++) {
            for (int j = i + 1; j < chickens.size(); j++) {
                Chicken chicken1 = chickens.get(i);
                Chicken chicken2 = chickens.get(j);

                if (!this.slotSeeds.isEmpty() &&
                        this.slotSeeds.get(0).getCount() >= 2 &&
                        chicken1.getAge() == 0 &&
                        chicken2.getAge() == 0 &&
                        !chicken1.isInLove() &&
                        !chicken2.isInLove()) {

                    chicken1.setInLove(null);
                    chicken2.setInLove(null);
                    this.slotSeeds.get(0).shrink(2);
                    break;
                }
            }
        }
    }


    private void collectEggs(AABB area) {
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area, e -> !e.isSpectator());

        for (ItemEntity item : items) {
            ItemStack stack = item.getItem();
            if (stack.getItem() == Items.EGG) {
                item.discard();
                this.output.add(stack);
            }
        }
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }

}
