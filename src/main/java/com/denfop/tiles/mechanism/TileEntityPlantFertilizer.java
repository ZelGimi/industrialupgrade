package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.inv.IAdvInventory;
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
import com.denfop.container.ContainerPlantFertilizer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiPlantFertilizer;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.crop.TileEntityCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityPlantFertilizer extends TileEntityInventory implements IUpgradableBlock {

    private static final int RADIUS = 8;
    public final InvSlot slot;
    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    private final AirPollutionComponent pollutionAir;
    private final SoilPollutionComponent pollutionSoil;
    private final ComponentUpgradeSlots componentUpgrade;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS+1, RADIUS+1, RADIUS+1)
    );
    private final ComponentVisibleArea visible;
    List<List<TileEntityCrop>> list = new ArrayList<>();
    List<ChunkPos> chunks;
    public TileEntityPlantFertilizer(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.plant_fertilizer,pos,state);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.fertilizer.getItem();
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemInput
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.plant_fertilizer;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.plant_fertilizer.info"));
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
        if (!this.getWorld().isClientSide) {
            int j2 = Mth.floor((searchArea.minX - 2) / 16.0D);
            int k2 = Mth.ceil((searchArea.maxX + 2) / 16.0D);
            int l2 = Mth.floor((searchArea.minZ - 2) / 16.0D);
            int i3 = Mth.ceil((searchArea.maxZ + 2) / 16.0D);
            chunks = new ArrayList<>();
            for (int j3 = j2; j3 < k2; ++j3) {
                for (int k3 = l2; k3 < i3; ++k3) {
                    final LevelChunk chunk = level.getChunk(j3, k3);
                    if (!chunks.contains(chunk.getPos())) {
                        chunks.add(chunk.getPos());
                    }
                }
            }
            for (ChunkPos chunk : chunks) {
                this.list.add(CropNetwork.instance.getCropsFromChunk(level, chunk));
            }
        }
    }

    @Override
    public ContainerPlantFertilizer getGuiContainer(final Player var1) {
        return new ContainerPlantFertilizer(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiPlantFertilizer((ContainerPlantFertilizer) menu);
    }

    public boolean contains(BlockPos vec) {
        if (vec.getX() > this.searchArea.minX && vec.getX() < searchArea.maxX) {
            if (vec.getY() > this.searchArea.minY && vec.getY() < searchArea.maxY) {
                return vec.getZ() > searchArea.minZ && vec.getZ() < searchArea.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void updateCrop() {
        list.clear();
        for (ChunkPos chunk : chunks) {
            this.list.add(CropNetwork.instance.getCropsFromChunk(level, chunk));
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 100 == 0) {
            updateCrop();
        }
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(5)) {
            cycle:
            for (List<TileEntityCrop> crops : list) {
                for (TileEntityCrop crop : crops) {
                    if (this.energy.getEnergy() > 5 && !this.slot.get(0).isEmpty()) {
                        if (crop.getPestUse() < 40 && this.contains(crop.getPos()) && crop.getCrop() != null && crop
                                .getCrop()
                                .getId() != 3 && crop.getCrop().getStage() != crop.getCrop().getMaxStage()) {
                            crop.fertilizeCrop(this.slot.get(0));
                            crop.addPestUse();
                            this.energy.useEnergy(5);
                        }
                    } else {
                        break cycle;
                    }
                }
            }
        }
    }


}
