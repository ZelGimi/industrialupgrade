package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerRadiationPurifier;
import com.denfop.gui.GuiRadiationPurifier;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.TileEntitySoilAnalyzer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TileRadiationPurifier extends TileElectricMachine {


    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public Radiation radiation;

    public Map<BlockPos, TileEntitySoilAnalyzer> booleanMap = new HashMap<>();
    private ItemStack stack;

    public TileRadiationPurifier() {
        super(50000, 14, 1);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    @Override
    public void onNeighborChange(final Block neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.getWorld().isRemote) {
            return;
        }

        final TileEntity tile = this.getWorld().getTileEntity(neighborPos);
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
        if (!this.getWorld().isRemote) {
            for (EnumFacing facing1 : EnumFacing.VALUES) {
                final BlockPos pos1 = this.pos.offset(facing1);
                final TileEntity tile = this.getWorld().getTileEntity(pos1);
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
        this.radiation = RadiationSystem.rad_system.getMap().get(this.getWorld().getChunkFromBlockCoords(this.pos).getPos());
        stack = new ItemStack(IUItem.crafting_elements, 1, 443);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.radiation_purifier;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    public void updateEntityServer() {

        super.updateEntityServer();
        if (this.getWorld().getWorldTime() % 20 == 0) {
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
    public ContainerRadiationPurifier getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerRadiationPurifier(this, entityPlayer);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiRadiationPurifier(getGuiContainer(entityPlayer));
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.radiation.getSoundEvent();
    }

}
