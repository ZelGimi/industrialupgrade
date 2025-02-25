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
import com.denfop.container.ContainerChickenFarm;
import com.denfop.container.ContainerCowFarm;
import com.denfop.container.ContainerPigFarm;
import com.denfop.gui.GuiChickenFarm;
import com.denfop.gui.GuiCowFarm;
import com.denfop.gui.GuiPigFarm;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TileEntityPigFarm extends TileEntityInventory  implements IUpgradableBlock {

    public final InvSlot slotSeeds;
    public final InvSlotOutput output;
    private static final int RADIUS = 4;
    public final Energy energy;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    AxisAlignedBB searchArea = new AxisAlignedBB(
            pos.add(-RADIUS, -RADIUS, -RADIUS),
            pos.add(RADIUS, RADIUS, RADIUS)
    );

    public TileEntityPigFarm() {
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
    }
    public final InvSlotUpgrade upgradeSlot;
    private final ComponentUpgradeSlots componentUpgrade;
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
    public <T extends Entity> List<T> getEntitiesWithinAABB(Class <? extends T > clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T > filter)
    {
        List<T> list = Lists.newArrayList();
        this.chunks.forEach(chunk -> chunk.getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter));
        return list;
    }
    List<Chunk> chunks = new ArrayList<>();
    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote){
            final AxisAlignedBB aabb = searchArea.offset(pos);
            searchArea = aabb;
            int j2 = MathHelper.floor((aabb.minX - 2) / 16.0D);
            int k2 = MathHelper.ceil((aabb.maxX + 2) / 16.0D);
            int l2 = MathHelper.floor((aabb.minZ - 2) / 16.0D);
            int i3 = MathHelper.ceil((aabb.maxZ + 2) / 16.0D);
            for (int j3 = j2; j3 < k2; ++j3)
            {
                for (int k3 = l2; k3 < i3; ++k3)
                {
                    final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                    if (!chunks.contains(chunk)){
                        chunks.add(chunk);
                    }
                }
            }
        }
    }
    @Override
    public ContainerPigFarm getGuiContainer(final EntityPlayer var1) {
        return new ContainerPigFarm(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiPigFarm(getGuiContainer(var1));
    }

    private static final int MAX_PIGS = 20;

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<EntityPig> pigs = getEntitiesWithinAABB(EntityPig.class, searchArea, EntitySelectors.NOT_SPECTATING);


            if (pigs.size() < MAX_PIGS) {
                breedPigs(pigs);
            }


            killOldPigs(pigs);
        }

    }





    private void breedPigs(List<EntityPig> pigs) {
        for (int i = 0; i < pigs.size(); i++) {
            for (int j = i + 1; j < pigs.size(); j++) {
                EntityPig pig1 = pigs.get(i);
                EntityPig pig2 = pigs.get(j);

                if (pig1.getGrowingAge() == 0 && this.slotSeeds.get().getCount() >= 2 && !pig1.isInLove() && !pig2.isInLove() && pig2.getGrowingAge() == 0 && pig1.getLoveCause() == null && pig2.getLoveCause() == null) {
                    pig1.setInLove(null);
                    pig2.setInLove(null);
                    slotSeeds.get().shrink(2);
                    break;
                }
            }
        }
    }

    private void killOldPigs(List<EntityPig> pigs) {
        for (int i = pigs.size() - 1; i >= MAX_PIGS; i--) {
            EntityPig pig = pigs.get(i);
            pig.setDead();
            this.output.add(new ItemStack(Items.PORKCHOP, 1));
        }
    }

}
