package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRadiationPurifier;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRadiationPurifier;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TileRadiationPurifier extends TileElectricMachine {


    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public Radiation radiation;

    public Map<BlockPos, TileEntitySoilAnalyzer> booleanMap = new HashMap<>();
    private ItemStack stack;

    public TileRadiationPurifier(BlockPos pos,BlockState state) {
        super(50000, 14, 1,BlockBaseMachine3.radiation_purifier,pos,state);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    @Override
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.getWorld().isClientSide) {
            return;
        }

        final BlockEntity tile = this.getWorld().getBlockEntity(neighborPos);
        boolean can = tile instanceof TileEntitySoilAnalyzer;
        if (!booleanMap.containsKey(neighborPos) && can) {
            booleanMap.put(neighborPos, (TileEntitySoilAnalyzer) tile);
        } else if (!can) {
            booleanMap.remove(neighborPos);
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            for (Direction facing1 : Direction.values()) {
                final BlockPos pos1 = this.pos.offset(facing1.getNormal());
                final BlockEntity tile = this.getWorld().getBlockEntity(pos1);
                boolean can = tile instanceof TileEntitySoilAnalyzer;
                if (!booleanMap.containsKey(pos1) && can) {
                    booleanMap.put(pos1, (TileEntitySoilAnalyzer) tile);
                } else if (!can) {
                    booleanMap.remove(pos1);
                }
            }
        }
    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        this.radiation = RadiationSystem.rad_system.getMap().get(this.getWorld().getChunkAt(this.pos).getPos());
        stack = new ItemStack(IUItem.crafting_elements.getStack(443), 1);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.radiation_purifier;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    public void updateEntityServer() {

        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 20 == 0) {
            if (this.radiation != null && this.energy.canUseEnergy(100) && !booleanMap.isEmpty()) {
                boolean canWork = false;
                for (Map.Entry<BlockPos, TileEntitySoilAnalyzer> entry : booleanMap.entrySet()) {
                    canWork = entry.getValue().analyzed;
                    if (canWork) {
                        break;
                    }
                }
                if (canWork && this.outputSlot.canAdd(stack)) {
                    if (this.radiation.removeRadiationWithType(1000)) {
                        this.energy.useEnergy(100);
                        this.outputSlot.add(stack);
                        this.setActive(true);
                    } else {
                        this.setActive(false);
                    }
                }
            } else {
                this.setActive(false);
            }
        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        boolean can = customPacketBuffer.readBoolean();
        if (can) {
            try {
                this.radiation = (Radiation) DecoderHandler.decode(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.radiation != null);
        if (this.radiation != null) {
            try {
                EncoderHandler.encode(customPacketBuffer, this.radiation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return customPacketBuffer;
    }

    @Override
    public ContainerRadiationPurifier getGuiContainer(final Player entityPlayer) {
        return new ContainerRadiationPurifier(this, entityPlayer);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiRadiationPurifier((ContainerRadiationPurifier) menu);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.radiation.getSoundEvent();
    }

}
