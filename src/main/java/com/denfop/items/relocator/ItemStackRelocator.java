package com.denfop.items.relocator;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRelocator;
import com.denfop.container.ContainerRelocatorAddPoint;
import com.denfop.gui.GuiRelocator;
import com.denfop.gui.GuiRelocatorAddPoint;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemStackRelocator extends ItemStackInventory {

    public final ItemStack itemStack1;
    private final boolean sneaking;
    public List<Point> points = new ArrayList<>();

    public ItemStackRelocator(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        this.sneaking = player.isSneaking();
        if (!player.getEntityWorld().isRemote) {
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

    public ContainerBase<ItemStackRelocator> getGuiContainer(EntityPlayer player) {
        if (sneaking) {
            return new ContainerRelocatorAddPoint(this);
        }
        return new ContainerRelocator(this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        if (sneaking) {
            return new GuiRelocatorAddPoint(getGuiContainer(player));
        }
        return new GuiRelocator(getGuiContainer(player));
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

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
