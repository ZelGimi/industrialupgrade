package com.denfop.items.bags;

import com.denfop.IUCore;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.item.upgrade.EnumUpgrades;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.containermenu.ContainerMenuBags;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.IProperties;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemEnergyBags extends Item implements IItemStackInventory, IProperties, IUpdatableItemStackEvent, UpgradeItem, EnergyItem {
    private final int slots;
    private final int maxStorage;
    private final int getTransferLimit;
    private String nameItem;

    public ItemEnergyBags(int slots, int maxStorage, int getTransferLimit) {
        super(new Properties().tab(IUCore.EnergyTab).stacksTo(1));
        this.slots = slots;

        this.getTransferLimit = getTransferLimit;
        this.maxStorage = maxStorage;
        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.BAGS.list));
        IUCore.proxy.addProperties(this);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level world,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.literal(Localization.translate("iu.bags.info")));
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            CompoundTag nbt = stack.getTag();
            if (nbt == null || !nbt.contains("bag")) {
                return;
            }

            List<BagsDescription> list = new ArrayList<>();
            CompoundTag nbt1 = nbt.getCompound("bag");
            int size = nbt1.getInt("size");

            for (int i = 0; i < size; i++) {
                list.add(new BagsDescription(nbt1.getCompound(String.valueOf(i))));
            }

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
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
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

    public CustomWorldContainer getInventory(Player player, ItemStack stack) {
        return new ItemStackBags(player, stack, this.slots);
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
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
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putBoolean("open", true);
        nbt.putInt("slot_inventory", player.getInventory().selected);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putBoolean("white", !nbt.getBoolean("white"));
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
        CompoundTag nbt = ModUtils.nbt(stack);

        if (!UpgradeSystem.system.hasInMap(stack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, stack));
        }

        Player player = (Player) entity;

        if (nbt.getBoolean("open")) {
            int slotId = nbt.getInt("slot_inventory");
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuBags) {
                ItemStackBags toolbox = ((ContainerMenuBags) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    nbt.putBoolean("open", false);
                }
            } else if (!(player.containerMenu instanceof ContainerMenuBags)) {
                nbt.putBoolean("open", false);
            }
        }


    }


    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuBags) {
            ItemStackBags toolbox = ((ContainerMenuBags) player.containerMenu).base;
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

                CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

                growingBuffer.writeByte(1);

                growingBuffer.flip();
                NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


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
        return ModUtils.nbt(itemStack).getBoolean("open") ? 1 : 0;
    }
}
