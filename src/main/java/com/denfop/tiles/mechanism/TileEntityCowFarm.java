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
import com.denfop.gui.GuiChickenFarm;
import com.denfop.gui.GuiCowFarm;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
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

public class TileEntityCowFarm extends TileEntityInventory  implements IUpgradableBlock {

    public final InvSlot slotSeeds;
    public final InvSlotOutput output;
    private static final int RADIUS = 4;
    public final Energy energy;
    public final InvSlotOutput output1;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    AxisAlignedBB searchArea = new AxisAlignedBB(
            pos.add(-RADIUS, -RADIUS, -RADIUS),
            pos.add(RADIUS, RADIUS, RADIUS)
    );

    public TileEntityCowFarm() {
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.cow_farm;
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
    public ContainerCowFarm getGuiContainer(final EntityPlayer var1) {
        return new ContainerCowFarm(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCowFarm(getGuiContainer(var1));
    }

    private static final int MAX_COWS = 20;

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<EntityCow> cows = getEntitiesWithinAABB(EntityCow.class, searchArea, EntitySelectors.NOT_SPECTATING);








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
    }




    private void breedCows(List<EntityCow> cows) {
        for (int i = 0; i < cows.size(); i++) {
            for (int j = i + 1; j < cows.size(); j++) {
                EntityCow cow1 = cows.get(i);
                EntityCow cow2 = cows.get(j);

                if (cow1.getGrowingAge() == 0 && this.slotSeeds.get().getCount() >= 2 && !cow1.isInLove() && !cow2.isInLove() && cow2.getGrowingAge() == 0 && cow1.getLoveCause() == null && cow2.getLoveCause() == null) {
                    cow1.setInLove(null);
                    cow2.setInLove(null);
                    slotSeeds.get().shrink(2);
                    break;
                }
            }
        }
    }

    private void killOldCows(List<EntityCow> cows) {
        for (int i = cows.size() - 1; i >= MAX_COWS; i--) {
            EntityCow sheep = cows.get(i);
            sheep.setDead();
            this.output.add(new ItemStack(Items.BEEF, 1));
            if (world.rand.nextBoolean()) {
                this.output.add(new ItemStack(Items.LEATHER, world.rand.nextInt(2) + 1));
            }
        }
    }

}
