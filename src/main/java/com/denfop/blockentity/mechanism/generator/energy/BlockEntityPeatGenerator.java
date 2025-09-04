package com.denfop.blockentity.mechanism.generator.energy;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuPeatGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenPeatGenerator;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class BlockEntityPeatGenerator extends BlockEntityElectricMachine implements IType {


    public final Inventory slot;


    public int fuel = 0;

    public BlockEntityPeatGenerator(BlockPos pos, BlockState state) {
        super(0, 1, 0, BlockBaseMachine3Entity.peat_generator, pos, state);
        energy = this.addComponent(Energy.asBasicSource(this, 150000, 1));
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.cultivated_peat_balls.getItem();
            }
        };
        this.addComponent(new SoilPollutionComponent(this, 0.2));
        this.addComponent(new AirPollutionComponent(this, 0.2));

    }


    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            ModUtils.showFlames(this.getWorld(), this.pos, this.getFacing());
        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            fuel = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.peat_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, fuel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
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
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.slot.isEmpty()) {
            if (fuel == 0) {
                fuel = 400;
                this.slot.get(0).shrink(1);
                if (!this.getActive()) {
                    this.setActive(true);
                }
            }
        }
        if (fuel == 0) {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        if (this.getActive()) {
            this.energy.addEnergy(25);
            fuel = Math.max(0, this.fuel - 1);
        }


    }

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {


            return (int) Math.min(this.fuel * i / 400D, i);
        }
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.fuel = nbttagcompound.getInt("fuel");
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("fuel", this.fuel);
        return nbt;
    }

    public ContainerMenuPeatGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuPeatGenerator(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenPeatGenerator((ContainerMenuPeatGenerator) menu);
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
