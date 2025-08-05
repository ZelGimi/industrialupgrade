package com.denfop.items.relocator;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRelocator;
import com.denfop.container.ContainerRelocatorAddPoint;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRelocator;
import com.denfop.gui.GuiRelocatorAddPoint;
import com.denfop.items.ItemStackInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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
        if (!player.level().isClientSide) {
            points = new ArrayList<>(RelocatorNetwork.instance.getPoints(player));
        }
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

    public ContainerBase<ItemStackRelocator> getGuiContainer(Player player) {
        if (sneaking) {
            return new ContainerRelocatorAddPoint(this);
        }
        return new ContainerRelocator(this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        if (sneaking) {
            return new GuiRelocatorAddPoint(isAdmin);
        }
        return new GuiRelocator(isAdmin);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


}
