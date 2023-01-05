package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.BaseElectricItem;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.Localization;
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

public class ItemMagnet extends BaseElectricItem implements IModelRegister, IUpgradeItem {


    private final int radius;

    public ItemMagnet(String name, double maxCharge, double transferLimit, int tier, int radius) {
        super(name, maxCharge, transferLimit, tier);
        this.setMaxDamage(27);
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
            tooltip.add(Localization.translate("iu.changemode_key") + Localization.translate(
                    "iu.changemode_rcm1"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
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

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(p_77663_2_, this, itemStack));
        }
        EntityPlayer player = (EntityPlayer) p_77663_3_;
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

            for (Entity entityinlist : list) {
                if (entityinlist instanceof EntityItem) {
                    EntityItem item = (EntityItem) entityinlist;
                    if (ElectricItem.manager.canUse(itemStack, 200 * energy)) {
                        if (mode == 1) {

                            item.setLocationAndAngles(p_77663_3_.posX, p_77663_3_.posY, p_77663_3_.posZ, 0.0F, 0.0F);
                            if (!player.world.isRemote) {
                                ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityTeleport(item));
                            }
                            item.setPickupDelay(0);
                            ElectricItem.manager.use(itemStack, 200 * energy, null);
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

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if (IC2.platform.isSimulating()) {

            int mode = ModUtils.NBTGetInteger(player.getHeldItem(hand), "mode");
            mode++;
            if (mode > 2 || mode < 0) {
                mode = 0;
            }

            ModUtils.NBTSetInteger(player.getHeldItem(hand), "mode", mode);
            IC2.platform.messagePlayer(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                            + Localization.translate("message.magnet.mode." + mode)
            );


            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else {
            return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        }
    }

}
