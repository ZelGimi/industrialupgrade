package com.denfop.blockentity.mechanism.energy;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.event.load.EventLoadController;
import com.denfop.api.energy.event.unload.EventUnloadController;
import com.denfop.api.energy.interfaces.EnergyController;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.energy.networking.Path;
import com.denfop.api.otherenergies.common.InfoTile;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuController;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenEnergyController;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;

public class BlockEntityEnergyController extends BlockEntityInventory implements
        IUpdatableTileEvent, EnergyController {

    public List<Path> energyPathList = new ArrayList<>();
    public boolean work = false;
    public int size;
    Map<Direction, EnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<EnergyTile>> validReceivers = new LinkedList<>();
    int hashCodeSource;
    private ChunkPos chunkPos;
    private long id;

    public BlockEntityEnergyController(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.energy_controller, pos, state);
        this.addComponent(Energy.asBasicSink(this, 0, 14));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.energy_controller;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.controller.info"));
        tooltip.add(Localization.translate("iu.controller.info1"));
    }

    public List<InfoTile<EnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void RemoveTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<EnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<EnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public int getHashCodeSource() {
        return hashCodeSource;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {
        hashCodeSource = hashCode;
    }

    public void AddTile(EnergyTile tile, final Direction facing1) {
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    public Map<Direction, EnergyTile> getTiles() {
        return energyConductorMap;
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            size = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EventLoadController(this, level));

        }

    }

    @Override
    public void onUnloaded() {
        if (!this.getWorld().isClientSide) {
            MinecraftForge.EVENT_BUS.post(new EventUnloadController(this, level));
        }
        super.onUnloaded();
    }


    @Override
    public ContainerMenuController getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuController(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenEnergyController((ContainerMenuController) menu);
    }


    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        nbt.putBoolean("work", work);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.work = nbtTagCompound.getBoolean("work");

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
        if (this.level.isClientSide) {
            return;
        }
        if (i == 0) {
            energyPathList.clear();
            for (Direction facing : Direction.values()) {
                final List<Path> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing.getNormal())
                );

                for (Path path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            this.size = energyPathList.size();
        } else if (i == 1) {
            this.work = true;
            energyPathList.forEach(energyPath -> energyPath.setHasController(true));
        }
    }

    @Override
    public boolean getWork() {
        return this.work;
    }

    @Override
    public void work() {
        if (this.getWork()) {
            energyPathList.clear();
            for (Direction facing : Direction.values()) {
                final List<Path> energyPathList1 = EnergyNetGlobal.instance.getEnergyPaths(
                        this.getWorld(),
                        this.getBlockPos().offset(facing.getNormal())
                );

                for (Path path : energyPathList1) {
                    if (!energyPathList.contains(path)) {
                        energyPathList.add(path);
                    }
                }
            }
            this.size = energyPathList.size();
            energyPathList.forEach(energyPath -> energyPath.setHasController(true));
        }
    }

    @Override
    public void unload() {
        energyPathList.forEach(energyPath -> energyPath.setHasController(false));

    }


}
