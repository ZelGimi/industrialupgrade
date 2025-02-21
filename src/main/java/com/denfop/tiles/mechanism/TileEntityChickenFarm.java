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
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerChickenFarm;
import com.denfop.gui.GuiChickenFarm;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
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
import java.util.List;
import java.util.Set;

public class TileEntityChickenFarm extends TileEntityInventory implements IUpgradableBlock {

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

    public TileEntityChickenFarm() {
        this.slotSeeds = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == Items.WHEAT_SEEDS;
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
    @Override
    public ContainerChickenFarm getGuiContainer(final EntityPlayer var1) {
        return new ContainerChickenFarm(this,var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiChickenFarm(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.chicken_farm;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.chicken_farm.info"));
    }

    private static final int MAX_CHICKENS = 12;

    public <T extends Entity> List<T> getEntitiesWithinAABB(
            Class<? extends T> clazz,
            AxisAlignedBB aabb,
            @Nullable Predicate<? super T> filter
    ) {
        List<T> list = Lists.newArrayList();
        this.chunks.forEach(chunk -> chunk.getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter));
        return list;
    }

    List<Chunk> chunks = new ArrayList<>();

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
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 20 == 0 && this.energy.canUseEnergy(50)) {
            this.energy.useEnergy(50);
            List<EntityChicken> chickens = getEntitiesWithinAABB(EntityChicken.class, searchArea, EntitySelectors.NOT_SPECTATING);
            collectEggs(searchArea);


            if (chickens.size() < MAX_CHICKENS) {
                breedChickens(chickens);
            }


            if (chickens.size() > MAX_CHICKENS) {
                killExcessChickens(chickens);
            }
        }
    }

    private void killExcessChickens(List<EntityChicken> chickens) {
        for (int i = chickens.size() - 1; i >= MAX_CHICKENS; i--) {
            EntityChicken chicken = chickens.get(i);
            chicken.setDead();
            this.output.add(new ItemStack(Items.CHICKEN, 1));
            if (world.rand.nextBoolean()) {
                this.output.add(new ItemStack(Items.FEATHER, world.rand.nextInt(2) + 1));
            }
        }
    }

    private void breedChickens(List<EntityChicken> chickens) {
        for (int i = 0; i < chickens.size(); i++) {
            for (int j = i + 1; j < chickens.size(); j++) {
                EntityChicken chicken1 = chickens.get(i);
                EntityChicken chicken2 = chickens.get(j);

                if (!this.slotSeeds.isEmpty() && this.slotSeeds
                        .get()
                        .getCount() >= 2 && chicken1.getGrowingAge() == 0 && chicken2.getGrowingAge() == 0 && chicken1.getLoveCause() == null && chicken2.getLoveCause() == null && !chicken1.isInLove() && !chicken2.isInLove()) {
                    chicken1.setInLove(null);
                    chicken2.setInLove(null);
                    this.slotSeeds.get().shrink(2);
                    break;
                }
            }
        }
    }

    private void collectEggs(AxisAlignedBB area) {
        List<EntityItem> items = getEntitiesWithinAABB(EntityItem.class, area, EntitySelectors.NOT_SPECTATING);

        for (EntityItem item : items) {
            ItemStack stack = item.getItem();
            if (stack.getItem() == Items.EGG) {
                item.setDead();
                this.output.add(stack);
            }
        }
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
