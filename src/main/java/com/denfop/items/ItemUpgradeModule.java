package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.upgrades.UpgradeItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.containermenu.ContainerMenuUpgrade;
import com.denfop.items.bags.BagsDescription;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemUpgradeModule<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements UpgradeItem, IItemStackInventory, IUpdatableItemStackEvent {
    public ItemUpgradeModule(T element) {
        super(new Item.Properties().tab(IUCore.UpgradeTab), element);
    }

    public static Type getType(int meta) {
        if (meta < 0 || meta >= Type.Values.length) {
            return null;
        }
        return Type.Values[meta];
    }

    private static Direction getDirection(final ItemStack stack) {
        final int rawDir = ModUtils.nbt(stack).getByte("dir");
        if (rawDir < 1 || rawDir > 6) {
            return null;
        }
        return Direction.values()[rawDir - 1];
    }

    private static String getSideName(final ItemStack stack) {
        final Direction dir = getDirection(stack);
        if (dir == null) {
            return "iu.tooltip.upgrade.ejector.anyside";
        }
        switch (dir) {
            case WEST: {
                return "iu.dir.west";
            }
            case EAST: {
                return "iu.dir.east";
            }
            case DOWN: {
                return "iu.dir.bottom";
            }
            case UP: {
                return "iu.dir.top";
            }
            case NORTH: {
                return "iu.dir.north";
            }
            case SOUTH: {
                return "iu.dir.south";
            }
            default: {
                throw new RuntimeException("invalid dir: " + dir);
            }
        }
    }

    @Override
    public CustomWorldContainer getInventory(final Player var1, final ItemStack var2) {
        if (this.getElement().getId() < 11) {
            return null;
        } else {
            return new ItemStackUpgradeModules(var1, var2);


        }
    }

    public void save(ItemStack stack, Player player) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putBoolean("open", true);
        nbt.putInt("slot_inventory", player.getInventory().selected);
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, ItemStack stack) {

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
        Player player = (Player) entity;
        CompoundTag nbt = stack.getOrCreateTag();

        if (nbt.getBoolean("open")) {
            int slotId = nbt.getInt("slot_inventory");
            if (slotId != itemSlot && !world.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuUpgrade) {
                ItemStackUpgradeModules toolbox = ((ContainerMenuUpgrade) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    nbt.putBoolean("open", false);
                }
            }
        }


    }

    @Override
    public void updateEvent(int event, ItemStack stack) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        byte event1 = (byte) event;
        nbt.putByte("dir", event1);
    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMenuUpgrade) {
            ItemStackUpgradeModules toolbox = ((ContainerMenuUpgrade) player.containerMenu).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAsThrown(stack);
                player.closeContainer();
            }
        }
        return true;
    }

    @Override
    public boolean isSuitableFor(final ItemStack stack, final Set<EnumBlockEntityUpgrade> types) {
        Type type = getType(this.getElement().getId());
        if (type == null) {
            return false;
        }
        switch (type) {
            case overclocker:
            case Overclocker1:
            case Overclocker2:
                return (types.contains(EnumBlockEntityUpgrade.Processing));
            case transformer:
            case transformer1:
            case transformer_simple:
                return types.contains(EnumBlockEntityUpgrade.Transformer);
            case storage:
            case adv_storage:
            case imp_storage:
            case per_storage:
            case energy_storage:
                return types.contains(EnumBlockEntityUpgrade.EnergyStorage);
            case ejector:
                return types.contains(EnumBlockEntityUpgrade.ItemExtract);
            case pulling:
                return types.contains(EnumBlockEntityUpgrade.ItemInput);
            case fluid_ejector:
                return types.contains(EnumBlockEntityUpgrade.FluidExtract);
            case fluid_pulling:
                return types.contains(EnumBlockEntityUpgrade.FluidInput);

        }
        return false;
    }

    @Override
    public int getExtraTier(final ItemStack itemStack) {
        ItemUpgradeModule.Type type = getType(this.getElement().getId());
        if (type == null) {
            return 0;
        } else {
            switch (type) {
                case transformer_simple:
                    return 1;
                case transformer:
                    return 2;
                case transformer1:
                    return 4;
                default:
                    return 0;
            }
        }
    }

    @Override
    public double getProcessTimeMultiplier(final ItemStack itemStack) {
        Type type = getType(this.getElement().getId());
        if (type == null) {
            return 1.0D;
        }
        switch (type) {
            case overclocker:
                return 0.8D;
            case Overclocker1:
                return 0.6D;
            case Overclocker2:
                return 0.4D;
        }
        return 1.0D;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
       /* if (world.isClientSide) {
            try {
                Optional<MenuScreens.ScreenConstructor<ContainerBase<?>, ?>> factory = MenuScreens.getScreenFactory(Register.inventory_container.get(), Minecraft.getInstance(), 0, Component.translatable("iu"));
                factory.get().create(getInventory(player,stack).getGuiContainer(player),player.getInventory(), Component.translatable(""));
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
        if (!player.getLevel().isClientSide) {
            save(stack, player);

            CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        Type type = getType(this.getElement().getId());
        if (type == null) {
            return;
        }
        super.appendHoverText(stack, world, tooltip, flag);

        switch (type) {
            case overclocker:
            case Overclocker1:
            case Overclocker2:
                tooltip.add(Component.translatable(
                        "iu.tooltip.upgrade.overclocker.time",
                        String.format("%.2f", 100.0D * Math.pow(getProcessTimeMultiplier(stack), stack.getCount()))
                ));
                tooltip.add(Component.translatable(
                        "iu.tooltip.upgrade.overclocker.power",
                        String.format("%.2f", 100.0D * Math.pow(getEnergyDemandMultiplier(stack), stack.getCount()))
                ));
                break;

            case ejector:
            case fluid_ejector: {
                String side = getSideName(stack);
                tooltip.add(Component.translatable("iu.tooltip.upgrade.ejector", Component.translatable(side)));
                if (type == Type.ejector) {
                    CompoundTag nbt = stack.getOrCreateTag();
                    List<BagsDescription> bags = new ArrayList<>();
                    ListTag contentList = nbt.getList("Items", Tag.TAG_COMPOUND);

                    for (int i = 0; i < contentList.size(); ++i) {
                        CompoundTag slotNbt = contentList.getCompound(i);
                        int slot = slotNbt.getByte("Slot");
                        if (slot >= 0 && slot < 6) {
                            ItemStack stack1 = ItemStack.of(slotNbt);
                            if (!stack1.isEmpty()) {
                                bags.add(new BagsDescription(stack1));
                            }
                        }
                    }

                    for (BagsDescription description : bags) {
                        tooltip.add(Component.literal(description.getStack().getHoverName().getString()).withStyle(ChatFormatting.GREEN));
                    }
                }
                break;
            }

            case pulling:
            case fluid_pulling: {
                String side = getSideName(stack);
                tooltip.add(Component.translatable("iu.tooltip.upgrade.pulling", Component.translatable(side)));
                if (type == Type.pulling) {
                    CompoundTag nbt = stack.getOrCreateTag();
                    List<BagsDescription> bags = new ArrayList<>();
                    ListTag contentList = nbt.getList("Items", Tag.TAG_COMPOUND);

                    for (int i = 0; i < contentList.size(); ++i) {
                        CompoundTag slotNbt = contentList.getCompound(i);
                        int slot = slotNbt.getByte("Slot");
                        if (slot >= 0 && slot < 6) {
                            ItemStack stack1 = ItemStack.of(slotNbt);
                            if (!stack1.isEmpty()) {
                                bags.add(new BagsDescription(stack1));
                            }
                        }
                    }

                    for (BagsDescription description : bags) {
                        tooltip.add(Component.literal(description.getStack().getHoverName().getString()).withStyle(ChatFormatting.GREEN));
                    }
                }
                break;
            }

            case transformer_simple:
            case transformer:
            case transformer1:
                tooltip.add(Component.translatable("iu.tooltip.upgrade.transformer", this.getExtraTier(stack) * stack.getCount()));
                break;

            case storage:
            case adv_storage:
            case imp_storage:
            case per_storage:
            case energy_storage:
                tooltip.add(Component.translatable("iu.tooltip.upgrade.storage", this.getExtraEnergyStorage(stack) * ModUtils.getSize(stack)));
                break;
        }
    }

    public double getExtraEnergyStorage(ItemStack stack) {
        Types type = (Types) this.getElement();
        if (type == null) {
            return 0;
        } else {
            switch (type) {
                case energy_storage:
                    return 10000;
                case storageUpgrade:
                    return 100000;
                case adv_storageUpgrade:
                    return 1000000;
                case imp_storageUpgrade:
                    return 10000000;
                case per_storageUpgrade:
                    return 100000000;
                default:
                    return 0;
            }
        }
    }

    @Override
    public double getEnergyDemandMultiplier(final ItemStack itemStack) {
        Type type = getType(this.getElement().getId());
        if (type == null) {
            return 1.0D;
        }
        switch (type) {
            case overclocker:
                return 1.11D;
            case Overclocker1:
                return 1.3D;
            case Overclocker2:
                return 1.5D;
        }
        return 1.0D;
    }

    public enum Type {
        Overclocker1,
        Overclocker2,
        transformer,
        transformer1,

        storage,

        adv_storage,

        imp_storage,

        per_storage,
        overclocker,
        transformer_simple,
        energy_storage,
        ejector,
        pulling,
        fluid_ejector,
        fluid_pulling;

        public static final Type[] Values = values();

    }

    public enum Types implements ISubEnum {
        overclockerUpgrade1(0),
        overclockerUpgrade2(1),
        transformerUpgrade1(2),
        transformerUpgrade2(3),

        storageUpgrade(4),
        adv_storageUpgrade(5),
        imp_storageUpgrade(6),
        per_storageUpgrade(7),
        overclocker(8),
        transformer(9),
        energy_storage(10),
        ejector(11),
        pulling(12),
        fluid_ejector(13),
        fluid_pulling(14);;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase();
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "upgrades";
        }

        public int getId() {
            return this.ID;
        }
    }
}
