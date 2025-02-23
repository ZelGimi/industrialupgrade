package com.denfop.items;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerVeinSensor;
import com.denfop.gui.GuiVeinSensor;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Vector2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemStackVeinSensor extends ItemStackInventory {


    public final ItemStack itemStack1;
    private final Map<Integer, Map<Vector2, DataOres>> map;
    private final Vector2 vector;


    public ItemStackVeinSensor(
            EntityPlayer player, ItemStack stack, final Map<Integer, Map<Vector2, DataOres>> map,
            final Vector2 vector2
    ) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        this.map = map;
        this.vector = vector2;
    }

    public Vector2 getVector() {
        return vector;
    }

    public Map<Integer, Map<Vector2, DataOres>> getMap() {
        return map;
    }

    public void save() {
        super.save();
    }

    public void saveAndThrow(ItemStack stack) {
        NBTTagList contentList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i])) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                contentList.appendTag(nbt);
            }
        }

        ModUtils.nbt(stack).setTag("Items", contentList);
        this.clear();
    }

    public ContainerBase<ItemStackVeinSensor> getGuiContainer(EntityPlayer player) {
        return new ContainerVeinSensor(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiVeinSensor(new ContainerVeinSensor(player, this), itemStack1);
    }

    @Override
    public TileEntityInventory getParent() {
        return null;
    }


    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
        return 0;
    }


    public ItemStack get(int index) {
        return this.inventory[index];
    }

    protected void restore(ItemStack[] backup) {
        if (backup.length != this.inventory.length) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.inventory, 0, this.inventory.length);

        }
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }


    protected ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.inventory.length];

        for (int i = 0; i < this.inventory.length; ++i) {
            ItemStack content = this.inventory[i];
            ret[i] = ModUtils.isEmpty(content) ? ModUtils.emptyStack : content.copy();
        }

        return ret;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory[index] = content;
        this.save();
    }

    public int getStackSizeLimit() {
        return 64;
    }


}
