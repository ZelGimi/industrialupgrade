package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.mechanism.BlockEntitySoilAnalyzer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuRadiationPurifier;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRadiationPurifier;
import com.denfop.sound.EnumSound;
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

public class BlockEntityRadiationPurifier extends BlockEntityElectricMachine {


    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public Radiation radiation;

    public Map<BlockPos, BlockEntitySoilAnalyzer> booleanMap = new HashMap<>();
    private ItemStack stack;

    public BlockEntityRadiationPurifier(BlockPos pos, BlockState state) {
        super(50000, 14, 1, BlockBaseMachine3Entity.radiation_purifier, pos, state);
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
        boolean can = tile instanceof BlockEntitySoilAnalyzer;
        if (!booleanMap.containsKey(neighborPos) && can) {
            booleanMap.put(neighborPos, (BlockEntitySoilAnalyzer) tile);
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
                boolean can = tile instanceof BlockEntitySoilAnalyzer;
                if (!booleanMap.containsKey(pos1) && can) {
                    booleanMap.put(pos1, (BlockEntitySoilAnalyzer) tile);
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.radiation_purifier;
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
                for (Map.Entry<BlockPos, BlockEntitySoilAnalyzer> entry : booleanMap.entrySet()) {
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
    public ContainerMenuRadiationPurifier getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuRadiationPurifier(this, entityPlayer);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenRadiationPurifier((ContainerMenuRadiationPurifier) menu);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.radiation.getSoundEvent();
    }

}
