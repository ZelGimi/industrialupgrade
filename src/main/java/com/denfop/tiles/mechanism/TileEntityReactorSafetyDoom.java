package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSafetyDoom;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSafetyDoom;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TileEntityReactorSafetyDoom extends TileElectricMachine {


    public final ComponentBaseEnergy rad;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public List<List<IAdvReactor>> iAdvReactorList = new ArrayList<>();
    public boolean full = false;

    public TileEntityReactorSafetyDoom(BlockPos pos, BlockState state) {
        super(50000, 14, 1,BlockBaseMachine3.reactor_safety_doom,pos,state);
        this.rad = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.RADIATION, this, 5000000000D));

    }
    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.reactor_safety_doom.info1"));
    }
    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        ChunkPos chunkPos = this.getWorld().getChunkAt(this.pos).getPos();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                List<IAdvReactor> list = RadiationSystem.rad_system.getAdvReactorMap().computeIfAbsent(
                        chunkPos1,
                        k -> new ArrayList<>()
                );
                iAdvReactorList.add(list);
                full = full || !list.isEmpty();
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.full);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        full = customPacketBuffer.readBoolean();
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.reactor_safety_doom;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    public void updateEntityServer() {

        super.updateEntityServer();

        if (getWorld().getGameTime() % 100 == 0) {
            boolean work = false;
            this.full = false;
            if (this.rad.getEnergy() < this.rad.getCapacity() && this.energy.canUseEnergy(10)) {
                this.energy.useEnergy(10);
                for (List<IAdvReactor> reactors : this.iAdvReactorList) {
                    for (IAdvReactor reactor : reactors) {
                        this.full = true;
                        double free = this.rad.getFreeEnergy();
                        int amount = (reactor.getLevelReactor() + 1);
                        if (reactor.isWork()) {
                            amount *= (int) (reactor.getOutput() / (amount * 5D));
                        }
                        if (free == 0 || this.energy.getEnergy() < amount) {
                            break;
                        }
                        final double energy1 = Math.min(reactor.getRadiation().getEnergy(), free);
                        reactor.getRadiation().useEnergy(energy1);
                        this.rad.addEnergy(energy1);
                        this.energy.useEnergy(amount);
                        work = true;
                    }

                }
            }

            this.setActive(work);
        }
    }


    @Override
    public ContainerSafetyDoom getGuiContainer(final Player entityPlayer) {
        return new ContainerSafetyDoom(this, entityPlayer);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSafetyDoom((ContainerSafetyDoom) menu);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.radiation.getSoundEvent();
    }

}
