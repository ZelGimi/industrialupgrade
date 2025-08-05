package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergySink;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerMachineCharger;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiMachineCharger;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityMachineCharge extends TileEntityInventory {

    private final Energy energy;
    private final ComponentVisibleArea visible;
    Map<ChunkPos, List<IEnergySink>> chunkPosListMap = new HashMap<>();

    public TileEntityMachineCharge(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.machine_charger,pos,state);
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
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.machine_charger;
    }

    @Override
    public ContainerMachineCharger getGuiContainer(final Player var1) {
        return new ContainerMachineCharger(this, var1);
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

        visible.aabb = new AABB(minX,level.getMinBuildHeight(),minZ,maxX,level.getMaxBuildHeight(),maxZ);
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiMachineCharger((ContainerMachineCharger) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 200 == 0) {
            ChunkPos chunkPos = new ChunkPos(this.pos);
            Map<ChunkPos, List<IEnergySink>> map = EnergyNetGlobal.instance.getChunkPosListMap(this.level);
            this.chunkPosListMap.clear();
            if (map != null) {
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                        final List<IEnergySink> list = map.get(chunkPos1);
                        if (list != null) {
                            chunkPosListMap.put(chunkPos1, list);
                        }
                    }
                }
            }
            chunkPosListMap.get(chunkPos).remove((IEnergySink) this.energy.getDelegate());
        }
        if (this.energy.getEnergy() > 0) {
            for (Map.Entry<ChunkPos, List<IEnergySink>> chunkPosListEntry : chunkPosListMap.entrySet()) {
                if (this.energy.getEnergy() == 0) {
                    break;
                }
                for (IEnergySink energySink : chunkPosListEntry.getValue()) {
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
