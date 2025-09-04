package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.interfaces.EnergySink;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuMachineCharger;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMachineCharger;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockEntityMachineCharge extends BlockEntityInventory {

    private final Energy energy;
    private final ComponentVisibleArea visible;
    Map<ChunkPos, List<EnergySink>> chunkPosListMap = new HashMap<>();

    public BlockEntityMachineCharge(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.machine_charger, pos, state);
        this.energy = this.addComponent(Energy.asBasicSink(this, 1000000000, 14));

        this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.addComponent(new AirPollutionComponent(this, 0.1));
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.machine_charger.info"));
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
        return BlockBaseMachine3Entity.machine_charger;
    }

    @Override
    public ContainerMenuMachineCharger getGuiContainer(final Player var1) {
        return new ContainerMenuMachineCharger(this, var1);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenMachineCharger((ContainerMenuMachineCharger) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 200 == 0) {
            ChunkPos chunkPos = new ChunkPos(this.pos);
            Map<ChunkPos, List<EnergySink>> map = EnergyNetGlobal.instance.getChunkPosListMap(this.level);
            this.chunkPosListMap.clear();
            if (map != null) {
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                        final List<EnergySink> list = map.get(chunkPos1);
                        if (list != null) {
                            chunkPosListMap.put(chunkPos1, list);
                        }
                    }
                }
            }
            chunkPosListMap.get(chunkPos).remove((EnergySink) this.energy.getDelegate());
        }
        if (this.energy.getEnergy() > 0) {
            for (Map.Entry<ChunkPos, List<EnergySink>> chunkPosListEntry : chunkPosListMap.entrySet()) {
                if (this.energy.getEnergy() == 0) {
                    break;
                }
                for (EnergySink energySink : chunkPosListEntry.getValue()) {
                    double demanded = energySink.getDemandedEnergy();
                    demanded = Math.min(demanded, this.energy.getEnergy());
                    energySink.receiveEnergy(demanded);
                    this.energy.useEnergy(demanded);
                    if (this.energy.getEnergy() == 0) {
                        break;
                    }
                }
            }
        }
    }


    public Energy getEnergy() {
        return energy;
    }

}
