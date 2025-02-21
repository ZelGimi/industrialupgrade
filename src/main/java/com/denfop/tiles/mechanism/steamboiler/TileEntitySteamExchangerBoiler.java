package com.denfop.tiles.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoiler;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiChemicalExchanger;
import com.denfop.gui.GuiSteamBoilerExchanger;
import com.denfop.invslot.InvSlot;
import com.denfop.items.resource.ItemCraftingElements;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntitySteamExchangerBoiler extends TileEntityMultiBlockElement implements IExchanger{

    public final InvSlot slot;
    public final InvSlot slot1;

    public TileEntitySteamExchangerBoiler(){
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT,1){
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemCraftingElements && stack.getItemDamage() == 599;
            }

            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
        this.slot1 = new InvSlot(this, InvSlot.TypeItemSlot.INPUT,10){
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemCraftingElements && stack.getItemDamage() == 294;
            }
            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("steam_boiler_exchanger.info"));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }
    @Override
    public ContainerDefaultMultiElement getGuiContainer(final EntityPlayer var1) {
        return new ContainerDefaultMultiElement(this, var1){
            @Override
            public void addSlots(final TileEntityMultiBlockElement multiBlockElement, final EntityPlayer var1) {
                super.addSlots(multiBlockElement, var1);

                this.addSlotToContainer(new SlotInvSlot(slot, 0, 68, 37));
                this.addSlotToContainer(new SlotInvSlot(slot1, 0, 37, 19));
                this.addSlotToContainer(new SlotInvSlot(slot1, 1, 37, 19+18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 2, 37, 19+18+18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 3, 99, 19));
                this.addSlotToContainer(new SlotInvSlot(slot1, 4, 99, 19+18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 5, 99, 19+18+18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 6, 59, 15));
                this.addSlotToContainer(new SlotInvSlot(slot1, 7, 59+18, 15));
                this.addSlotToContainer(new SlotInvSlot(slot1, 8, 59, 59));
                this.addSlotToContainer(new SlotInvSlot(slot1, 9, 59+18, 59));
            }
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSteamBoilerExchanger(this.getGuiContainer(var1));
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamBoiler.steam_boiler_heat_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler;
    }

    @Override
    public boolean isWork() {
        for (ItemStack stack : slot1.getContents())
            if (stack.isEmpty())
                return false;
        return !slot.isEmpty();
    }

}
