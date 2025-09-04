package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSafetyDoom;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSafetyDoom;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
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

public class BlockEntityReactorSafetyDoom extends BlockEntityElectricMachine {


    public final ComponentBaseEnergy rad;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    public List<List<IAdvReactor>> iAdvReactorList = new ArrayList<>();
    public boolean full = false;

    public BlockEntityReactorSafetyDoom(BlockPos pos, BlockState state) {
        super(50000, 14, 1, BlockBaseMachine3Entity.reactor_safety_doom, pos, state);
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.reactor_safety_doom;
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
    public ContainerMenuSafetyDoom getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuSafetyDoom(this, entityPlayer);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSafetyDoom((ContainerMenuSafetyDoom) menu);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.radiation.getSoundEvent();
    }

}
