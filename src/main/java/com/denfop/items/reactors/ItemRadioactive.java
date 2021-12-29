package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.core.IC2Potion;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemIC2;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRadioactive extends ItemIC2 implements IModelRegister {

    private final String name;
    private final String path;
    protected final int radiationLength;

    protected final int amplifier;

    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (this.radiationLength != 0) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLiving = (EntityLivingBase) entity;
                if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving)) {
                    IC2Potion.radiation.applyTo(entityLiving, this.radiationLength, this.amplifier);
                }
            }
        }
    }

    public ItemRadioactive(String name, int radiationLength1, int amplifier1) {
        super(null);
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);
        setUnlocalizedName(name);
        this.name = name;
        this.radiationLength = radiationLength1;
        this.amplifier = amplifier1;
        this.path = "reactors";
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + "/" + this.name, null)
        );
    }

}
