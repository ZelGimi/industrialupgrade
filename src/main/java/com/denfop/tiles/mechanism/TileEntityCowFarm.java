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
import com.denfop.container.ContainerCowFarm;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiCowFarm;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Cow;
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

public class TileEntityCowFarm extends TileEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    private static final int MAX_COWS = 20;
    public final InvSlot slotSeeds;
    public final InvSlotOutput output;
    public final Energy energy;
    public final InvSlotOutput output1;
    public final InvSlotUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS, RADIUS, RADIUS)
    );

    public TileEntityCowFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.cow_farm,pos,state);
        this.slotSeeds = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == Items.WHEAT;
            }
        };
        this.output = new InvSlotOutput(this, 9);
        this.output1 = new InvSlotOutput(this, 4);
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.cow_farm;
    }



    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    @Override
    public ContainerCowFarm getGuiContainer(final Player var1) {
        return new ContainerCowFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiCowFarm((ContainerCowFarm) menu);
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
