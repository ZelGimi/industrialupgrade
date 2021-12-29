package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.items.BaseElectricItem;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMagnet extends BaseElectricItem implements IModelRegister {


    private final int radius;

    public ItemMagnet(String name, double maxCharge, double transferLimit, int tier, int radius) {
        super(name, maxCharge, transferLimit, tier);
        this.setMaxDamage(27);
        setMaxStackSize(1);

        this.radius = radius;
        IUCore.proxy.addIModelRegister(this);
        this.name = name;
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
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name, extraName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("energy").append("/").append(name);

        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name, String extraName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public void onUpdate(ItemStack itemStack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        if (!(p_77663_3_ instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) p_77663_3_;
        int mode = ModUtils.NBTGetInteger(itemStack, "mode");
        if (mode != 0) {
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(p_77663_3_.posX - radius, p_77663_3_.posY - radius,
                    p_77663_3_.posZ - radius, p_77663_3_.posX + radius, p_77663_3_.posY + radius, p_77663_3_.posZ + radius
            );
            List<Entity> list = p_77663_2_.getEntitiesWithinAABBExcludingEntity(p_77663_3_, axisalignedbb);

            for (Entity entityinlist : list) {
                if (entityinlist instanceof EntityItem) {
                    EntityItem item = (EntityItem) entityinlist;
                    if (ElectricItem.manager.canUse(itemStack, 500)) {
                        ItemStack stack = item.getItem();
                        if (!(stack.getItem() instanceof ItemMagnet)) {
                            if (mode == 1) {
                                if (player.inventory.addItemStackToInventory(stack)) {

                                    ElectricItem.manager.use(itemStack, 500, (EntityLivingBase) p_77663_3_);
                                    player.inventoryContainer.detectAndSendChanges();
                                } else {
                                    boolean xcoord = item.posX + 2 >= p_77663_3_.posX && item.posX - 2 <= p_77663_3_.posX;
                                    boolean zcoord = item.posZ + 2 >= p_77663_3_.posZ && item.posZ - 2 <= p_77663_3_.posZ;

                                    if (!xcoord && !zcoord) {
                                        item.setPosition(p_77663_3_.posX, p_77663_3_.posY - 1, p_77663_3_.posZ);
                                        item.setPickupDelay(10);
                                    }
                                }
                            } else if (mode == 2) {
                                boolean xcoord = item.posX + 2 >= p_77663_3_.posX && item.posX - 2 <= p_77663_3_.posX;
                                boolean zcoord = item.posZ + 2 >= p_77663_3_.posZ && item.posZ - 2 <= p_77663_3_.posZ;

                                if (!xcoord && !zcoord) {
                                    item.setPosition(p_77663_3_.posX, p_77663_3_.posY - 1, p_77663_3_.posZ);
                                    item.setPickupDelay(10);
                                }
                            }
                        }
                        player.inventoryContainer.detectAndSendChanges();
                    }

                }
            }
        }


    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    public ActionResult onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
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


            return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else {
            return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
        }
    }

}
