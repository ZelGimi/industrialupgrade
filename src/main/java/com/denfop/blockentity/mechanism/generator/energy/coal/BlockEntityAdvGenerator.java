package com.denfop.blockentity.mechanism.generator.energy.coal;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.mechanism.generator.energy.BlockEntityBaseGenerator;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenGenerator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class BlockEntityAdvGenerator extends BlockEntityBaseGenerator implements IType {

    public final Inventory fuelSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
        @Override
        public boolean canPlaceItem(final int index, final ItemStack stack) {
            return ModUtils.getFuelValue(stack, false) > 0;
        }
    };

    private final double coef;


    public int itemFuelTime = 0;

    public BlockEntityAdvGenerator(double coef, int maxstorage, int tier, MultiBlockEntity multiTileBlock, BlockPos pos, BlockState state) {
        super(
                coef * (double) Math.round(10.0F * 1),
                tier,
                maxstorage, multiTileBlock, pos, state
        );
        this.coef = coef;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            ModUtils.showFlames(this.getWorld(), this.getBlockPos(), this.getFacing());
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

    public int gaugeFuelScaled(int i) {
        if (this.fuel <= 0) {
            return 0;
        } else {
            if (this.itemFuelTime <= 0) {
                this.itemFuelTime = this.fuel;
            }

            return Math.min(this.fuel * i / this.itemFuelTime, i);
        }
    }

    public int consumeFuel() {
        ItemStack fuel = this.consume(1);
        return fuel == null ? 0 : ModUtils.getFuelValue(fuel, false);
    }

    public boolean gainFuel() {

        int fuelValue = this.consumeFuel() / 4;
        if (fuelValue == 0) {
            return false;
        } else {
            this.fuel += fuelValue;
            this.itemFuelTime = fuelValue;
            return true;
        }
    }


    public ItemStack consume(int amount) {
        ItemStack ret = ItemStack.EMPTY;


        ItemStack stack = this.fuelSlot.get(0);
        if (!stack.isEmpty() && (ModUtils.getSize(stack) == 1 || !stack.getItem().hasCraftingRemainingItem(stack))) {
            int currentAmount = Math.min(amount, ModUtils.getSize(stack));
            amount -= currentAmount;
            if (ModUtils.getSize(stack) == currentAmount) {
                if (stack.getItem().hasCraftingRemainingItem(stack)) {
                    ItemStack container = stack.getItem().getCraftingRemainingItem(stack);
                    if (container.isEmpty() && container.isDamageableItem() && DamageHandler.getDamage(container) > DamageHandler.getMaxDamage(
                            container)) {
                        container = ItemStack.EMPTY;
                    }

                    this.fuelSlot.set(0, container);
                } else {
                    this.fuelSlot.set(0, ItemStack.EMPTY);
                }
            } else {
                this.fuelSlot.set(0, ModUtils.decSize(stack, currentAmount));
            }

            ret = ModUtils.setSize(stack, currentAmount);
        }


        return ret;
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }


    public ContainerMenuGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenGenerator((ContainerMenuGenerator) menu);
    }

    public boolean isConverting() {
        return this.fuel > 0;
    }

    public String getOperationSoundFile() {
        return "Generators/GeneratorLoop.ogg";
    }


    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.itemFuelTime = nbt.getInt("itemFuelTime");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putInt("itemFuelTime", this.itemFuelTime);
        return nbt;
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.DEFAULT;
    }

}
