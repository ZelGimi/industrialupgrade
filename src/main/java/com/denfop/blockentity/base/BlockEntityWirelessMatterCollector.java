package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWirelessMatterCollector;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWirelessMatterCollector;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockEntityWirelessMatterCollector extends BlockEntityInventory {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    private final Energy energy;
    private final ComponentVisibleArea visible;
    Map<ChunkPos, List<IMatter>> chunkPosListMap = new HashMap<>();

    public BlockEntityWirelessMatterCollector(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.matter_collector, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("tank", 16384000, Fluids.fluidPredicate(FluidName.fluiduu_matter.getInstance().get()));
        this.energy = this.addComponent(Energy.asBasicSink(this, 10000, 14));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
        visible = this.addComponent(new ComponentVisibleArea(this));

    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.matter_collector.info"));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        ChunkPos chunkPos = new ChunkPos(this.pos);
        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                int chunkX = (chunkPos.x + dx) * 16;
                int chunkZ = (chunkPos.z + dz) * 16;

                if (chunkX < minX) minX = chunkX;
                if (chunkZ < minZ) minZ = chunkZ;
                if (chunkX + 15 > maxX) maxX = chunkX + 15;
                if (chunkZ + 15 > maxZ) maxZ = chunkZ + 15;
            }
        }

        visible.aabb = new AABB(minX, level.getMinBuildHeight(), minZ, maxX, level.getMaxBuildHeight(), maxZ);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.matter_collector;
    }

    @Override
    public ContainerMenuWirelessMatterCollector getGuiContainer(final Player var1) {
        return new ContainerMenuWirelessMatterCollector(this, var1);
    }

    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenWirelessMatterCollector((ContainerMenuWirelessMatterCollector) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 200 == 0) {
            ChunkPos chunkPos = new ChunkPos(this.pos);
            Map<ChunkPos, List<IMatter>> map = BlockEntityMultiMatter.worldMatterMap.get(this.level.dimension());
            this.chunkPosListMap.clear();
            if (map != null) {
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                        final List<IMatter> list = map.get(chunkPos1);
                        if (list != null) {
                            chunkPosListMap.put(chunkPos1, list);
                        }
                    }
                }
            }
        }
        boolean active = false;
        if (this.energy.getEnergy() > 30) {
            for (Map.Entry<ChunkPos, List<IMatter>> chunkPosListEntry : chunkPosListMap.entrySet()) {
                if (this.energy.getEnergy() < 30) {
                    break;
                }
                for (IMatter matter : chunkPosListEntry.getValue()) {
                    if (this.energy.getEnergy() < 30) {
                        break;
                    }
                    int amount = matter.getMatterTank().getFluidAmount();
                    amount = Math.min(amount, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
                    if (amount > 0) {
                        this.fluidTank.fill(new FluidStack(FluidName.fluiduu_matter.getInstance().get(), amount), IFluidHandler.FluidAction.EXECUTE);
                        matter.getMatterTank().drain(amount, IFluidHandler.FluidAction.EXECUTE);
                        this.energy.useEnergy(30);
                        active = true;
                    }
                }
            }
        }
        this.setActive(active);
    }

    public int getFilled(int i) {
        return (int) (i * this.getFluidTank().getFluidAmount() / (this.getFluidTank().getCapacity() * 1D));
    }

    public int getIntegerPercentage() {
        return (int) (100 * this.getFluidTank().getFluidAmount() / (this.getFluidTank().getCapacity() * 1D));
    }

    public Energy getEnergy() {
        return energy;
    }

}
