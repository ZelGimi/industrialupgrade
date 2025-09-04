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
import com.denfop.containermenu.ContainerMenuSheepFarm;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSheepFarm;
import com.denfop.utils.Localization;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;

import static com.denfop.utils.ModUtils.getVecFromVec3i;

public class BlockEntitySheepFarm extends BlockEntityInventory implements IUpgradableBlock {

    public static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (p_29841_) -> {
        p_29841_.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        p_29841_.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        p_29841_.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        p_29841_.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        p_29841_.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        p_29841_.put(DyeColor.LIME, Blocks.LIME_WOOL);
        p_29841_.put(DyeColor.PINK, Blocks.PINK_WOOL);
        p_29841_.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        p_29841_.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        p_29841_.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        p_29841_.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        p_29841_.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        p_29841_.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        p_29841_.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        p_29841_.put(DyeColor.RED, Blocks.RED_WOOL);
        p_29841_.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });
    private static final int RADIUS = 4;
    private static final int MAX_SHEEP = 20;
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


    public BlockEntitySheepFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.sheep_farm, pos, state);
        this.slotSeeds = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == Items.WHEAT;
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
    public ContainerMenuSheepFarm getGuiContainer(final Player var1) {
        return new ContainerMenuSheepFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSheepFarm((ContainerMenuSheepFarm) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.sheep_farm;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.sheep_farm.info"));
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
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<Sheep> sheepList = level.getEntitiesOfClass(Sheep.class, searchArea);

            shearSheep(sheepList);


            if (sheepList.size() < MAX_SHEEP) {
                breedSheep(sheepList);
            }


            if (sheepList.size() > MAX_SHEEP) {
                killExcessSheep(sheepList);
            }
        }
    }

    private void killExcessSheep(List<Sheep> sheepList) {
        for (int i = sheepList.size() - 1; i >= MAX_SHEEP; i--) {
            Sheep sheep = sheepList.get(i);
            sheep.discard(); // Заменяет setDead()
            this.output.add(new ItemStack(Items.MUTTON, 1));
        }
    }

    private void breedSheep(List<Sheep> sheepList) {
        for (int i = 0; i < sheepList.size(); i++) {
            for (int j = i + 1; j < sheepList.size(); j++) {
                Sheep sheep1 = sheepList.get(i);
                Sheep sheep2 = sheepList.get(j);

                if (sheep1.getAge() == 0 &&
                        sheep2.getAge() == 0 &&
                        !sheep1.isInLove() &&
                        !sheep2.isInLove() &&
                        !this.slotSeeds.isEmpty() &&
                        this.slotSeeds.get(0).getCount() >= 2) {

                    sheep1.setInLove(null);
                    sheep2.setInLove(null);
                    this.slotSeeds.get(0).shrink(2);
                    break;
                }
            }
        }
    }

    private void shearSheep(List<Sheep> sheepList) {
        for (Sheep sheep : sheepList) {
            if (!sheep.isSheared() && sheep.isAlive()) {
                sheep.setSheared(true);
                DyeColor color = sheep.getColor();
                int count = level.random.nextInt(2) + 1;
                this.output.add(new ItemStack(ITEM_BY_DYE.get(color), count));
            }
        }
    }
}
