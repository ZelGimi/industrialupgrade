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
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPigFarm;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPigFarm;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
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

public class TileEntityPigFarm extends TileEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    private static final int MAX_PIGS = 20;
    public final InvSlot slotSeeds;
    public final InvSlotOutput output;
    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    private final ComponentVisibleArea visible;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS+1, RADIUS+1, RADIUS+1)
    );

    public TileEntityPigFarm(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.pig_farm,pos,state);
        this.slotSeeds = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == Items.CARROT;
            }
        };
        this.output = new InvSlotOutput(this, 9);
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.pig_farm;
    }



    @Override
    public void onLoaded() {
        super.onLoaded();
        visible.aabb = searchArea;

    }

    @Override
    public ContainerPigFarm getGuiContainer(final Player var1) {
        return new ContainerPigFarm(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiPigFarm((ContainerPigFarm) menu);
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
