package com.denfop.tiles.mechanism.generator.energy.redstone;

import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.LightComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRedstoneGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRedstoneGenerator;
import com.denfop.invslot.InvSlotRedstoneGenerator;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class TileBaseRedstoneGenerator extends TileElectricMachine implements IType {


    public final double coef;
    public final InvSlotRedstoneGenerator slot;


    public int fuel = 0;
    public int max_fuel = 400;
    public int redstone_coef = 1;

    public TileBaseRedstoneGenerator(double coef, int tier, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(0, tier, 0, block, pos, state);
        energy = this.addComponent(Energy.asBasicSource(this, 150000 * coef, tier));


        this.coef = coef;
        this.slot = new InvSlotRedstoneGenerator(this);
    }

    @OnlyIn(Dist.CLIENT)
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
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.info_upgrade_energy") + this.coef);
        }

        super.addInformation(stack, tooltip);

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        final ItemStack content = this.slot.get(0);
        if (content.isEmpty()) {
            this.redstone_coef = 0;
        }
        if (content.getItem() == Items.REDSTONE) {
            this.redstone_coef = 1;
        } else {
            this.redstone_coef = 9;
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!this.slot.isEmpty()) {
            if (fuel == 0) {
                fuel = max_fuel;
                if (this.slot.get(0).getItem() == Items.REDSTONE) {
                    this.redstone_coef = 1;
                } else {
                    this.redstone_coef = 9;
                }
                this.slot.get(0).shrink(1);
                if (!this.getActive()) {
                    this.setActive(true);
                }
            }
        } else {
            if (fuel == 0) {
                this.redstone_coef = 0;
            }
        }
        if (fuel == 0) {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        if (this.getActive()) {
            this.energy.addEnergy(25 * this.coef * redstone_coef);
            fuel = Math.max(0, this.fuel - 1);
        }


    }

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {


            return Math.min(this.fuel * i / this.max_fuel, i);
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

    public ContainerRedstoneGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerRedstoneGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiRedstoneGenerator((ContainerRedstoneGenerator) isAdmin);
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
