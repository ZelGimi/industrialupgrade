package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerFieldCleaner;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiFieldCleaner;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.denfop.utils.ModUtils.getVecFromVec3i;

public class TileEntityFieldCleaner extends TileEntityInventory implements IUpgradableBlock {


    private static final int RADIUS = 8;
    public final Energy energy;
    public final Fluids.InternalFluidTank tank;
    public final InvSlotUpgrade upgradeSlot;
    private final Fluids fluids;
    private final ComponentUpgradeSlots componentUpgrade;

    AABB searchArea = new AABB(
            getVecFromVec3i(pos.offset(-RADIUS, -RADIUS, -RADIUS)),
            getVecFromVec3i(pos.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1))
    );
    List<List<TileEntityCrop>> list = new ArrayList<>();
    List<ChunkPos> chunks;
    private ComponentVisibleArea visible;

    public TileEntityFieldCleaner(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.field_cleaner, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 10000, Fluids.fluidPredicate(FluidName.fluidweed_ex.getInstance().get()));
        this.energy = this.addComponent(Energy.asBasicSink(this, 1024, 4));
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.FluidInput
        );
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.hasFluidHandler(player.getItemInHand(hand))) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(Capabilities.FluidHandler.BLOCK, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.field_cleaner;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.field_cleaner.info"));
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
            final AABB aabb = searchArea;
            int j2 = Mth.floor((aabb.minX - 2) / 16.0D);
            int k2 = Mth.ceil((aabb.maxX + 2) / 16.0D);
            int l2 = Mth.floor((aabb.minZ - 2) / 16.0D);
            int i3 = Mth.ceil((aabb.maxZ + 2) / 16.0D);
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
    public ContainerFieldCleaner getGuiContainer(final Player var1) {
        return new ContainerFieldCleaner(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiFieldCleaner((ContainerFieldCleaner) menu);
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
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(10)) {
            cycle:
            for (List<TileEntityCrop> crops : list) {
                for (TileEntityCrop crop : crops) {
                    if (this.energy.getEnergy() > 10 && tank.getFluidAmount() > 1) {
                        if (crop.getCrop() != null && crop.getTickPest() == 0 && this.contains(crop.getPos()) && crop
                                .getCrop()
                                .getId() != 3) {
                            tank.drain(1, IFluidHandler.FluidAction.EXECUTE);
                            crop.setTickPest();
                            this.energy.useEnergy(10);
                        }
                    } else {
                        break cycle;
                    }
                }
            }
        }


    }


}
