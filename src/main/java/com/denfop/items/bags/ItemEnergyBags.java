package com.denfop.items.bags;

import com.denfop.ElectricItem;
import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.container.ContainerBags;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.IProperties;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemEnergyBags extends Item implements IItemStackInventory, IProperties, IUpdatableItemStackEvent, IItemTab, IUpgradeItem, IEnergyItem {
    private final int slots;
    private final int maxStorage;
    private final int getTransferLimit;
    private String nameItem;

    public ItemEnergyBags(int slots, int maxStorage, int getTransferLimit) {
        super(new Properties().stacksTo(1));
        this.slots = slots;

        this.getTransferLimit = getTransferLimit;
        this.maxStorage = maxStorage;
        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.BAGS.list));
        IUCore.proxy.addProperties(this);
    }


    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable TooltipContext world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.literal(Localization.translate("iu.bags.info")));
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            if (!stack.has(DataComponentsInit.DESCRIPTIONS_CONTAINER)) {
                return;
            }

            List<BagsDescription> list = stack.getOrDefault(DataComponentsInit.DESCRIPTIONS_CONTAINER, Collections.emptyList());


            for (BagsDescription description : list) {
                tooltip.add(Component.literal(description.getCount() + "x ")
                        .append(description.getStack().getHoverName())
                        .withStyle(ChatFormatting.GREEN));
            }
        }
    }

    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.BAGS.list;
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return maxStorage;
    }

    public short getTierItem(ItemStack stack) {
        return (short) 2;
    }

    public double getTransferEnergy(ItemStack stack) {
        return getTransferLimit;
    }

    public IAdvInventory getInventory(Player player, ItemStack stack) {
        return new ItemStackBags(player, stack, this.slots);
    }


    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    public void save(ItemStack stack, Player player) {
        ContainerItem containerItem = ContainerItem.getContainer(stack);
        containerItem = containerItem.updateOpen(stack, true);
        containerItem.updateSlot(stack, player.getInventory().selected);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {
        stack.set(DataComponentsInit.BLACK_LIST, !stack.getOrDefault(DataComponentsInit.BLACK_LIST, false));

    }

    public boolean canInsert(Player player, ItemStack stack, ItemStack stack1) {
        ItemStackBags box = (ItemStackBags) getInventory(player, stack);
        return box.canAdd(stack1);
    }

    public void insert(Player player, ItemStack stack, ItemStack stack1) {
        ItemStackBags box = (ItemStackBags) getInventory(player, stack);
        box.add(stack1);
        box.setChanged();
    }

    public void insertWithoutSave(Player player, ItemStack stack, ItemStack stack1) {
        ItemStackBags box = (ItemStackBags) getInventory(player, stack);
        box.addWithoutSave(stack1);
    }

    @Override
    public void inventoryTick(
            ItemStack stack,
            Level world,
            Entity entity,
            int itemSlot,
            boolean isSelected
    ) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);

        if (!(entity instanceof Player)) {
            return;
        }

        if (!UpgradeSystem.system.hasInMap(stack)) {
            NeoForge.EVENT_BUS.post(new EventItemLoad(world, this, stack));
        }

        Player player = (Player) entity;
        ContainerItem containerItem = ContainerItem.getContainer(stack);

        if (containerItem.open()) {
            int slotId = containerItem.slot_inventory();
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerBags) {
                ItemStackBags toolbox = ((ContainerBags) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    containerItem.updateOpen(stack, false);
                }
            } else if (!(player.containerMenu instanceof ContainerBags)) {
                containerItem.updateOpen(stack, false);
            }
        }


    }


    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerBags) {
            ItemStackBags toolbox = ((ContainerBags) player.containerMenu).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeContainer();
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, player.getItemInHand(hand)) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, player.getItemInHand(hand)).number * 0.25D : 0);

        if (ElectricItem.manager.canUse(player.getItemInHand(hand), 350 * coef)) {
            ElectricItem.manager.use(player.getItemInHand(hand), 350 * coef, player);
            if (!world.isClientSide && !player.isShiftKeyDown()) {
                save(stack, player);

                CustomPacketBuffer growingBuffer = new CustomPacketBuffer(player.registryAccess());

                growingBuffer.writeByte(1);

                growingBuffer.flip();
                player.openMenu(getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


                return InteractionResultHolder.success(player.getItemInHand(hand));

            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }


    @Override
    public String[] properties() {
        return new String[]{"open"};
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        return itemStack.getOrDefault(DataComponentsInit.CONTAINER, ContainerItem.EMPTY).open() ? 1 : 0;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            p_41392_.add(var4);
            p_41392_.add(new ItemStack(this, 1));
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
}
