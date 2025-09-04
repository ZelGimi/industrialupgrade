package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCollectorProductBee;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenCollectorProductBee;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityCollectorProductBee extends BlockEntityInventory implements BlockEntityUpgrade {


    private static final int RADIUS = 8;
    public final Energy energy;
    public final InventoryFluidByList fluidSlot;
    public final InventoryFluidByList fluidSlot1;
    public final Fluids.InternalFluidTank tank;
    public final Fluids.InternalFluidTank tank1;
    public final InventoryOutput outputSlot;
    public final InventoryOutput outputSlot1;
    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    private final Fluids fluids;
    private final ComponentVisibleArea visible;
    AABB searchArea = new AABB(
            pos.offset(-RADIUS, -RADIUS, -RADIUS),
            pos.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1)
    );
    List<List<BlockEntityApiary>> list = new ArrayList<>();
    List<ChunkPos> chunks;

    public BlockEntityCollectorProductBee(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.collector_product_bee, pos, state);
        this.energy = this.addComponent(Energy.asBasicSink(this, 10000, 1));
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankExtract("tank", 10000, Fluids.fluidPredicate(FluidName.fluidhoney.getInstance().get()));
        this.tank1 = this.fluids.addTankExtract("tank1", 10000, Fluids.fluidPredicate(FluidName.fluidroyaljelly.getInstance().get()));
        this.fluidSlot = new InventoryFluidByList(this, 1, FluidName.fluidhoney.getInstance().get());
        this.fluidSlot.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot1 = new InventoryFluidByList(this, 1, FluidName.fluidroyaljelly.getInstance().get());
        this.fluidSlot1.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.outputSlot = new InventoryOutput(this, 2);
        this.outputSlot1 = new InventoryOutput(this, 16);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
        visible = this.addComponent(new ComponentVisibleArea(this));

    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Transformer, EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput, EnumBlockEntityUpgrade.FluidExtract
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.collector_product_bee;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.collector_product_bee.info"));
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
                this.list.add(BeeNetwork.instance.getApiaryFromChunk(level, chunk));
            }
        }
    }

    @Override
    public ContainerMenuCollectorProductBee getGuiContainer(final Player var1) {
        return new ContainerMenuCollectorProductBee(this, var1);
    }

    private void updateBee() {
        list.clear();
        for (ChunkPos chunk : chunks) {
            this.list.add(BeeNetwork.instance.getApiaryFromChunk(level, chunk));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenCollectorProductBee((ContainerMenuCollectorProductBee) menu);
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

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 100 == 0) {
            updateBee();
        }
        if (this.getWorld().getGameTime() % 20 == 0 && this.energy.canUseEnergy(20)) {
            cycle:
            for (List<BlockEntityApiary> bees : list) {
                for (BlockEntityApiary bee : bees) {
                    if (this.energy.getEnergy() >= 20) {
                        if (this.contains(bee.getPos())) {
                            if (bee.food > 750 && bee.getTickDrainFood() == 20 && tank.getFluidAmount() + 1000 <= tank.getCapacity() && this.energy.getEnergy() >= 20) {
                                bee.food -= 750;
                                tank.fill(new FluidStack(FluidName.fluidhoney.getInstance().get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                                this.energy.useEnergy(20);
                                bee.setTickDrainFood((byte) 0);
                            }
                            if (bee.royalJelly > 50 && bee.getTickDrainJelly() == 20 && tank1.getFluidAmount() + 1000 <= tank1.getCapacity() && this.energy.getEnergy() >= 20) {
                                bee.royalJelly -= 50;
                                tank1.fill(new FluidStack(FluidName.fluidroyaljelly.getInstance().get(), 1000), IFluidHandler.FluidAction.EXECUTE);
                                this.energy.useEnergy(20);
                                bee.setTickDrainJelly((byte) 0);
                            }
                            if (!bee.invSlotProduct.isEmpty() && this.energy.getEnergy() >= 20) {
                                for (ItemStack stack : bee.invSlotProduct) {
                                    if (this.outputSlot1.add(stack)) {
                                        stack.shrink(stack.getCount());
                                    }
                                }
                                this.energy.useEnergy(20);
                            }
                        }
                    } else {
                        break cycle;
                    }
                }
            }
        }
        MutableObject<ItemStack> output1 = new MutableObject<>();
        if (this.tank.getFluidAmount() - 1000 >= 0 && this.fluidSlot.transferFromTank(
                this.tank,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot.transferFromTank(this.tank, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
        }
        if (this.tank1.getFluidAmount() - 1000 >= 0 && this.fluidSlot1.transferFromTank(
                this.tank1,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferFromTank(this.tank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
        }
        if (this.getWorld().getGameTime() % 20 == 0) {
            this.upgradeSlot.tickNoMark();
        }
    }


}
