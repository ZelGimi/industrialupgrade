package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.KeyboardIU;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemMagnet extends BaseEnergyItem implements IItemStackInventory, IUpdatableItemStackEvent, IUpgradeItem {
    private final int radius;

    public ItemMagnet(double maxCharge, double transferLimit, int tier, int radius) {
        super(new Properties().component(DataComponentsInit.MODE, 0).component(DataComponentsInit.BLACK_LIST, false), maxCharge, transferLimit, tier);

        this.radius = radius;

        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.MAGNET.list));
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

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
            this.nameItem = "iu.energy." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {
        stack.set(DataComponentsInit.BLACK_LIST, !stack.getOrDefault(DataComponentsInit.BLACK_LIST, false));

    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level p_77663_2_, Entity p_77663_3_, int slotIndex, boolean isCurrentItem) {
        if (!(p_77663_3_ instanceof Player)) {
            return;
        }
        if (p_77663_2_.isClientSide) {
            return;
        }

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            NeoForge.EVENT_BUS.post(new EventItemLoad(p_77663_2_, this, itemStack));
        }
        Player player = (Player) p_77663_3_;
        int mode = itemStack.getOrDefault(DataComponentsInit.MODE, 0);
        if (mode != 0) {
            int radius1 = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SIZE, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SIZE, itemStack).number : 0);
            double energy = 1 - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, itemStack).number * 0.25 : 0);

            int radius = this.radius + radius1;
            AABB axisalignedbb = new AABB(player.getX() - radius, player.getY() - radius,
                    player.getZ() - radius, player.getX() + radius, p_77663_3_.getY() + radius, p_77663_3_.getZ() + radius
            );

            LevelEntityGetter<Entity> list1 = ((ServerLevel) p_77663_2_).getEntities();
            List<Entity> list = Lists.newArrayList();
            list1.get(axisalignedbb, (p_151522_) -> {
                if (p_151522_ instanceof ItemEntity) {
                    list.add(p_151522_);
                }
            });
            final ItemStackMagnet inventory = (ItemStackMagnet) this.getInventory(player, itemStack);

            for (Entity entityinlist : list) {
                if (entityinlist instanceof ItemEntity) {
                    ItemEntity item = (ItemEntity) entityinlist;
                    if (ElectricItem.manager.canUse(itemStack, 200 * energy) && canInsert(itemStack,
                            ((ItemEntity) entityinlist).getItem(), inventory
                    )) {
                        if (mode == 1) {

                            item.absMoveTo(p_77663_3_.position().x, p_77663_3_.position().y, p_77663_3_.position().z, 0.0F, 0.0F);
                            if (!player.level().isClientSide) {
                                ((ServerPlayer) player).connection.send(new ClientboundTeleportEntityPacket(item));

                            }
                            item.setPickUpDelay(0);
                            ElectricItem.manager.use(itemStack, 200 * energy, player);
                        } else if (mode == 2) {
                            boolean xcoord = item.position().x + 2 >= p_77663_3_.position().x && item.position().x - 2 <= p_77663_3_.position().x;
                            boolean zcoord = item.position().z + 2 >= p_77663_3_.position().z && item.position().z - 2 <= p_77663_3_.position().z;

                            if (!xcoord && !zcoord) {
                                item.absMoveTo(p_77663_3_.position().x, p_77663_3_.position().y - 1, p_77663_3_.position().z);
                                item.setPickUpDelay(10);
                            }

                        }
                    }

                }
            }
        }
        ItemStack stack = itemStack;
        ContainerItem containerItem = ContainerItem.getContainer(stack);

        if (containerItem.open()) {
            int slotId = containerItem.slot_inventory();
            if (slotId != slotIndex && !p_77663_2_.isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMagnet) {
                ItemStackMagnet toolbox = ((ContainerMagnet) player.containerMenu).base;
                if (toolbox.isThisContainer(stack)) {
                    toolbox.saveAsThrown(stack);
                    player.closeContainer();
                    containerItem.updateOpen(stack, false);
                }
            }
        }
    }

    private boolean canInsert(ItemStack itemstack, ItemStack itemStack1, ItemStackMagnet inventory) {
        boolean white = itemstack.getOrDefault(DataComponentsInit.BLACK_LIST, false);
        boolean can = false;
        if (white) {
            for (ItemStack stack1 : inventory.list) {
                if (!stack1.isEmpty() && stack1.is(itemStack1.getItem())) {
                    can = true;
                    break;
                }
            }
        } else {
            for (ItemStack stack1 : inventory.list) {
                if (!stack1.isEmpty() && stack1.is(itemStack1.getItem())) {
                    can = false;
                    break;
                } else {
                    can = true;
                }
            }
        }
        return can;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand hand) {

        if (IUCore.keyboard.isChangeKeyDown(player)) {

            int mode = player.getItemInHand(hand).getOrDefault(DataComponentsInit.MODE, 0);
            mode++;
            if (mode > 2 || mode < 0) {
                mode = 0;
            }
            player.getItemInHand(hand).set(DataComponentsInit.MODE, mode);
            if (!p_41432_.isClientSide)
                IUCore.proxy.messagePlayer(
                        player,
                        ChatFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + Localization.translate("message.magnet.mode." + mode)
                );


            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        } else {
            if (!p_41432_.isClientSide) {
                save(player.getItemInHand(hand), player);

                CustomPacketBuffer growingBuffer = new CustomPacketBuffer(p_41432_.registryAccess());

                growingBuffer.writeByte(1);

                growingBuffer.flip();
                player.openMenu(getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        }
    }

    public void save(ItemStack stack, Player player) {
        ContainerItem containerItem = ContainerItem.getContainer(stack);
        containerItem = containerItem.updateOpen(stack, true);
        containerItem.updateSlot(stack, player.getInventory().selected);
    }

    public IAdvInventory getInventory(Player player, ItemStack stack) {
        return new ItemStackMagnet(player, stack, 0);
    }

    @Override
    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, @Nonnull Player player) {
        if (!player.level().isClientSide && !stack.isEmpty() && player.containerMenu instanceof ContainerMagnet) {
            ItemStackMagnet toolbox = ((ContainerMagnet) player.containerMenu).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAndThrow(stack);
                player.closeContainer();
            }
        }
        return true;
    }


    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        int mode = p_41421_.getOrDefault(DataComponentsInit.MODE, 0);
        if (mode > 2 || mode < 0) {
            mode = 0;
        }

        p_41423_.add(Component.literal(
                ChatFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                        + Localization.translate("message.magnet.mode." + mode))
        );
        int radius1 = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SIZE, p_41421_) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SIZE, p_41421_).number : 0);

        p_41423_.add(Component.literal(Localization.translate("iu.magnet.info") + (radius + radius1) + "x" + (radius + radius1)));

        if (!KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            p_41423_.add(Component.literal(Localization.translate("press.lshift")));
        }
        if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            p_41423_.add(Component.literal(Localization.translate("iu.changemode_key") + Localization.translate(
                    "iu.changemode_rcm1")));
        }
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }


    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.MAGNET.list;
    }
}
