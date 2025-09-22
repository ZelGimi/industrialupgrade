package com.denfop.items.panel;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.solar.EnumSolarType;
import com.denfop.api.solar.ISolarItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemNightSolarPanelGlass extends ItemSubTypes<ItemNightSolarPanelGlass.Types> implements ISolarItem, IModelRegister {

    protected static final String NAME = "night_solar_panel_glass";

    public ItemNightSolarPanelGlass() {
        super(Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    @Override
    public ResourceLocation getResourceLocation(int meta) {
        return new ResourceLocation(
                Constants.MOD_ID,
                "textures/items/night/" + ItemDayNightSolarPanelGlass.Types.getFromID(meta).getName() + ".png"
        );
    }

    @Override
    public EnumSolarType getType() {
        return EnumSolarType.NIGHT;
    }

    @Override
    public double getGenerationValue(final int damage) {
        return 0.25 * Math.pow(2, damage);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        tooltip.add(Localization.translate("iu.minipanel.generating_night") + this.getGenerationValue(stack.getItemDamage()) + " EF/t");
        tooltip.add(Localization.translate("iu.minipanel.jei"));
        tooltip.add(Localization.translate("iu.minipanel.jei1") + Localization.translate(new ItemStack(IUItem.basemachine2, 1
                , 91).getUnlocalizedName()));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public enum Types implements ISubEnum {
        adv(0),
        hyb(1),
        ult(2),
        qua(3),
        spe(4),
        pro(5),
        sin(6),
        adm(7),
        pho(8),
        neu(9),
        bar(10),
        adr(11),
        gra(12),
        kvr(13),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
