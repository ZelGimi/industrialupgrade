package com.denfop.items;

import com.denfop.Constants;
import com.denfop.blocks.FluidName;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCanister extends ItemFluidContainer {

    public ItemCanister() {
        super("canister", 1000);
        this.setMaxStackSize(1);
    }


    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.").replace(".name", ""));
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            subItems.add(new ItemStack(this));
            subItems.add(this.getItemStack(FluidName.fluidmotoroil));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(
                this,
                stack -> new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" + "canister", null)
        );
        ModelBakery.registerItemVariants(
                this,
                new ModelResourceLocation(Constants.MOD_ID + ":" + "tools" + "/" + "canister", null)
        );

    }


    public boolean canfill(Fluid fluid) {
        return fluid == FluidName.fluidmotoroil.getInstance();
    }


}
