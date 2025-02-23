package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.blocks.FluidName;
import com.denfop.network.packet.PacketRadiationUpdateValue;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRadioprotector extends ItemFluidContainer {

    public ItemRadioprotector() {
        super("radioprotector", 1000);
        this.setMaxStackSize(1);
    }


    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.").replace(".name", ""));
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            subItems.add(new ItemStack(this));
            subItems.add(this.getItemStack(FluidName.fluidazurebrilliant));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null && fluid.amount == 1000) {
                return new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" + "radioprotector_full", null);
            }

            return new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" + "radioprotector", null);

        });
        ModelBakery.registerItemVariants(
                this,
                new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" + "radioprotector_full", null)
        );
        ModelBakery.registerItemVariants(this, new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" +
                "radioprotector", null));
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (IUCore.proxy.isSimulating()) {
            ItemStack stack = ModUtils.get(player, hand);
            final NBTTagCompound nbt = player.getEntityData();
            FluidStack fluid = FluidUtil.getFluidContained(stack);
            if (fluid != null && fluid.amount == 1000 && nbt.getDouble("radiation") > 0D) {
                IFluidHandlerItem handler;
                handler = FluidUtil.getFluidHandler(stack);
                handler.drain(1000, true);

                nbt.setDouble("radiation", 0);
                new PacketRadiationUpdateValue(player, 0);
                player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 1200));
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 600));
                player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 600));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300));
            }
        }

        return super.onItemRightClick(world, player, hand);
    }


    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidazurebrilliant.getInstance();
    }


}
