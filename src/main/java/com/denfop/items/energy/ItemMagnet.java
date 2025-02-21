package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.container.ContainerBags;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.bags.ItemStackBags;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemMagnet extends BaseEnergyItem implements IItemStackInventory, IUpdatableItemStackEvent, IModelRegister,
        IUpgradeItem {


    private final int radius;

    public ItemMagnet(String name, double maxCharge, double transferLimit, int tier, int radius) {
        super(name, maxCharge, transferLimit, tier);
        setMaxStackSize(1);

        this.radius = radius;
        IUCore.proxy.addIModelRegister(this);
        this.name = name;
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.MAGNET.list);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.MAGNET.list;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name);
    }

    @Override
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        int mode = ModUtils.NBTGetInteger(stack, "mode");
        if (mode > 2 || mode < 0) {
            mode = 0;
        }

        tooltip.add(
                TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                        + Localization.translate("message.magnet.mode." + mode)
        );
        int radius1 = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SIZE, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SIZE, stack).number : 0);

        tooltip.add(Localization.translate("iu.magnet.info") + (radius + radius1) + "x" + (radius + radius1));

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.changemode.getKeyCode())) + Localization.translate(
                    "iu.changemode_rcm"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public boolean canInsert(ItemStack itemStack1, ItemStack itemstack, ItemStackMagnet inventory) {
        final NBTTagCompound nbt = ModUtils.nbt(itemStack1);
        boolean white = nbt.getBoolean("white");
        boolean can = false;
        if (white) {
            for (ItemStack stack1 : inventory.list) {
                if (!stack1.isEmpty() && stack1.isItemEqual(itemstack)) {
                    can = true;
                    break;
                }
            }
        } else {
            for (ItemStack stack1 : inventory.list) {
                if (!stack1.isEmpty() && stack1.isItemEqual(itemstack)) {
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
    public void onUpdate(
            @Nonnull ItemStack itemStack,
            @Nonnull World p_77663_2_,
            @Nonnull Entity p_77663_3_,
            int p_77663_4_,
            boolean p_77663_5_
    ) {
        if (!(p_77663_3_ instanceof EntityPlayer)) {
            return;
        }
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        EntityPlayer player = (EntityPlayer) p_77663_3_;
        if (nbt.getBoolean("open")) {
            int slot_id = nbt.getInteger("slot_inventory");
            if (slot_id != p_77663_4_ && !player.getEntityWorld().isRemote && !ModUtils.isEmpty(itemStack) && player.openContainer instanceof ContainerBags) {
                ItemStackBags toolbox = ((ContainerBags) player.openContainer).base;
                if (toolbox.isThisContainer(itemStack)) {
                    toolbox.saveAsThrown(itemStack);
                    player.closeScreen();
                    nbt.setBoolean("open", false);
                }
            } else if (!(player.openContainer instanceof ContainerBags)) {
                nbt.setBoolean("open", false);
            }
        }


        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(p_77663_2_, this, itemStack));
        }

        int mode = ModUtils.NBTGetInteger(itemStack, "mode");
        if (mode != 0) {
            int radius1 = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SIZE, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SIZE, itemStack).number : 0);
            double energy = 1 - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, itemStack).number * 0.25 : 0);

            int radius = this.radius + radius1;
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(p_77663_3_.posX - radius, p_77663_3_.posY - radius,
                    p_77663_3_.posZ - radius, p_77663_3_.posX + radius, p_77663_3_.posY + radius, p_77663_3_.posZ + radius
            );
            List<Entity> list = p_77663_2_.getEntitiesWithinAABBExcludingEntity(p_77663_3_, axisalignedbb);

            if (!list.isEmpty() && !player.getEntityWorld().isRemote) {
                final ItemStackMagnet inventory = this.getInventory(player, itemStack);

                for (Entity entityinlist : list) {
                    if (entityinlist instanceof EntityItem) {
                        EntityItem item = (EntityItem) entityinlist;
                        if (ElectricItem.manager.canUse(itemStack, 200 * energy) && canInsert(itemStack,
                                ((EntityItem) entityinlist).getItem(), inventory
                        )) {
                            if (mode == 1) {

                                item.setLocationAndAngles(p_77663_3_.posX, p_77663_3_.posY, p_77663_3_.posZ, 0.0F, 0.0F);
                                if (!player.world.isRemote) {
                                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityTeleport(item));
                                }
                                item.setPickupDelay(0);
                                ElectricItem.manager.use(itemStack, 200 * energy, player);
                            } else if (mode == 2) {
                                boolean xcoord = item.posX + 2 >= p_77663_3_.posX && item.posX - 2 <= p_77663_3_.posX;
                                boolean zcoord = item.posZ + 2 >= p_77663_3_.posZ && item.posZ - 2 <= p_77663_3_.posZ;

                                if (!xcoord && !zcoord) {
                                    item.setPosition(p_77663_3_.posX, p_77663_3_.posY - 1, p_77663_3_.posZ);
                                    item.setPickupDelay(10);
                                }

                            }
                        }

                    }
                }
            }
        }


    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {

        if (IUCore.keyboard.isChangeKeyDown(player) && IUCore.proxy.isSimulating()) {

            int mode = ModUtils.NBTGetInteger(player.getHeldItem(hand), "mode");
            mode++;
            if (mode > 2 || mode < 0) {
                mode = 0;
            }

            ModUtils.NBTSetInteger(player.getHeldItem(hand), "mode", mode);
            IUCore.proxy.messagePlayer(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                            + Localization.translate("message.magnet.mode." + mode)
            );


            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else {
            if (IUCore.proxy.isSimulating()) {
                save(player.getHeldItem(hand), player);
                player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

            }
            return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        }
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerBags) {
            ItemStackBags toolbox = ((ContainerBags) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAsThrown(stack);
                player.closeScreen();
            }
        }

        return true;
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("white", !nbt.getBoolean("white"));
    }

    public boolean canInsert(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        ItemStackMagnet box = (ItemStackMagnet) getInventory(player, stack);
        return box.canAdd(stack1);
    }

    public void insert(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        ItemStackMagnet box = (ItemStackMagnet) getInventory(player, stack);
        box.add(stack1);
        box.markDirty();
    }

    public void insertWithoutSave(EntityPlayer player, ItemStack stack, ItemStack stack1) {
        ItemStackMagnet box = (ItemStackMagnet) getInventory(player, stack);
        box.addWithoutSave(stack1);
    }

    public ItemStackMagnet getInventory(EntityPlayer player, ItemStack stack) {
        return new ItemStackMagnet(player, stack, 0);
    }

}
