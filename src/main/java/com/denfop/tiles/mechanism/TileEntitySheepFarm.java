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
import com.denfop.container.ContainerSheepFarm;
import com.denfop.gui.GuiSheepFarm;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import java.util.List;
import java.util.Set;

public class TileEntitySheepFarm extends TileEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 4;
    private static final int MAX_SHEEP = 20;
    public final InvSlot slotSeeds;
    public final InvSlotOutput output;
    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final ComponentUpgradeSlots componentUpgrade;
    AxisAlignedBB searchArea = new AxisAlignedBB(
            pos.add(-RADIUS, -RADIUS, -RADIUS),
            pos.add(RADIUS, RADIUS, RADIUS)
    );
    List<Chunk> chunks = new ArrayList<>();

    public TileEntitySheepFarm() {
        this.slotSeeds = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == Items.WHEAT;
            }
        };
        this.output = new InvSlotOutput(this, 9);
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public <T extends Entity> List<T> getEntitiesWithinAABB(
            Class<? extends T> clazz,
            AxisAlignedBB aabb,
            @Nullable Predicate<? super T> filter
    ) {
        List<T> list = Lists.newArrayList();
        this.chunks.forEach(chunk -> chunk.getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter));
        return list;
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
    public ContainerSheepFarm getGuiContainer(final EntityPlayer var1) {
        return new ContainerSheepFarm(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSheepFarm(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.sheep_farm;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            final AxisAlignedBB aabb = searchArea.offset(pos);
            searchArea = aabb;
            int j2 = MathHelper.floor((aabb.minX - 2) / 16.0D);
            int k2 = MathHelper.ceil((aabb.maxX + 2) / 16.0D);
            int l2 = MathHelper.floor((aabb.minZ - 2) / 16.0D);
            int i3 = MathHelper.ceil((aabb.maxZ + 2) / 16.0D);
            for (int j3 = j2; j3 < k2; ++j3) {
                for (int k3 = l2; k3 < i3; ++k3) {
                    final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                    if (!chunks.contains(chunk)) {
                        chunks.add(chunk);
                    }
                }
            }
        }
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
        if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<EntitySheep> sheepList = getEntitiesWithinAABB(EntitySheep.class, searchArea, EntitySelectors.NOT_SPECTATING);
            shearSheep(sheepList);


            if (sheepList.size() < MAX_SHEEP) {
                breedSheep(sheepList);
            }


            if (sheepList.size() > MAX_SHEEP) {
                killExcessSheep(sheepList);
            }
        }
    }

    private void killExcessSheep(List<EntitySheep> sheepList) {
        for (int i = sheepList.size() - 1; i >= MAX_SHEEP; i--) {
            EntitySheep sheep = sheepList.get(i);
            sheep.setDead();
            this.output.add(new ItemStack(Items.MUTTON, 1));
        }
    }

    private void breedSheep(List<EntitySheep> sheepList) {
        for (int i = 0; i < sheepList.size(); i++) {
            for (int j = i + 1; j < sheepList.size(); j++) {
                EntitySheep sheep1 = sheepList.get(i);
                EntitySheep sheep2 = sheepList.get(j);

                if (sheep1.getGrowingAge() == 0 && this.slotSeeds
                        .get()
                        .getCount() >= 2 && !sheep1.isInLove() && !sheep2.isInLove() && sheep2.getGrowingAge() == 0 && sheep1.getLoveCause() == null && sheep2.getLoveCause() == null) {
                    sheep1.setInLove(null);
                    sheep2.setInLove(null);
                    slotSeeds.get().shrink(2);
                    break;
                }
            }
        }
    }

    private void shearSheep(List<EntitySheep> sheepList) {
        for (EntitySheep sheep : sheepList) {
            if (!sheep.getSheared() && sheep.isEntityAlive()) {
                sheep.setSheared(true);
                ItemStack wool = new ItemStack(Blocks.WOOL, world.rand.nextInt(2) + 1, sheep.getFleeceColor().getMetadata());
                this.output.add(wool);
            }
        }
    }

}
