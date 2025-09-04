package com.denfop.items.relocator;

import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuRelocator;
import com.denfop.containermenu.ContainerMenuRelocatorAddPoint;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenRelocator;
import com.denfop.screen.ScreenRelocatorAddPoint;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemStackRelocator extends ItemStackInventory {

    public final ItemStack itemStack1;
    private final boolean sneaking;
    public List<Point> points = new ArrayList<>();

    public ItemStackRelocator(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        this.sneaking = player.isShiftKeyDown();
        if (!player.getLevel().isClientSide) {
            points = new ArrayList<>(RelocatorNetwork.instance.getPoints(player));
        }
    }

    public void save() {
        super.save();
    }

    @Override
    public CustomPacketBuffer writeContainer() {
        CustomPacketBuffer customPacketBuffer = super.writeContainer();

        return customPacketBuffer;
    }

    @Override
    public void readContainer(final CustomPacketBuffer buffer) {
        super.readContainer(buffer);

    }

    public ContainerMenuBase<ItemStackRelocator> getGuiContainer(Player player) {
        if (sneaking) {
            return new ContainerMenuRelocatorAddPoint(this);
        }
        return new ContainerMenuRelocator(this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player player, ContainerMenuBase<?> isAdmin) {
        if (sneaking) {
            return new ScreenRelocatorAddPoint(isAdmin);
        }
        return new ScreenRelocator(isAdmin);
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

    }

    @Override
    public int getBaseIndex(final Inventory var1) {
        return 0;
    }


    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }


    public int getStackSizeLimit() {
        return 64;
    }


}
