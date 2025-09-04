package com.denfop.blockentity.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoilerEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDefaultMultiElement;
import com.denfop.containermenu.SlotInvSlot;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemCraftingElements;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamBoilerExchanger;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class BlockEntitySteamExchangerBoiler extends BlockEntityMultiBlockElement implements IExchanger {

    public final Inventory slot;
    public final Inventory slot1;

    public BlockEntitySteamExchangerBoiler(BlockPos pos, BlockState state) {
        super(BlockSteamBoilerEntity.steam_boiler_heat_exchanger, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemCraftingElements && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 599;
            }

            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
        this.slot1 = new Inventory(this, Inventory.TypeItemSlot.INPUT, 10) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof ItemCraftingElements && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 294;
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
    public ContainerMenuDefaultMultiElement getGuiContainer(final Player var1) {
        return new ContainerMenuDefaultMultiElement(this, var1) {
            @Override
            public void addSlots(final BlockEntityMultiBlockElement multiBlockElement, final Player var1) {
                super.addSlots(multiBlockElement, var1);

                this.addSlotToContainer(new SlotInvSlot(slot, 0, 68, 37));
                this.addSlotToContainer(new SlotInvSlot(slot1, 0, 37, 19));
                this.addSlotToContainer(new SlotInvSlot(slot1, 1, 37, 19 + 18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 2, 37, 19 + 18 + 18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 3, 99, 19));
                this.addSlotToContainer(new SlotInvSlot(slot1, 4, 99, 19 + 18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 5, 99, 19 + 18 + 18));
                this.addSlotToContainer(new SlotInvSlot(slot1, 6, 59, 15));
                this.addSlotToContainer(new SlotInvSlot(slot1, 7, 59 + 18, 15));
                this.addSlotToContainer(new SlotInvSlot(slot1, 8, 59, 59));
                this.addSlotToContainer(new SlotInvSlot(slot1, 9, 59 + 18, 59));
            }
        };
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamBoilerExchanger((ContainerMenuDefaultMultiElement) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamBoilerEntity.steam_boiler_heat_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler.getBlock(getTeBlock());
    }

    @Override
    public boolean isWork() {
        for (ItemStack stack : slot1) {
            if (stack.isEmpty()) {
                return false;
            }
        }
        return !slot.isEmpty();
    }

}
