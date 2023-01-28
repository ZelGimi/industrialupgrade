package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.ModUtils;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemVeinSensor extends ItemMulti<ItemVeinSensor.Types> implements IModelRegister {

    protected static final String NAME = "sensor";

    public ItemVeinSensor() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc;
        if (!extraName.isEmpty()) {
            loc = Constants.MOD_ID +
                    ':' +
                    "sensor" + "/" + name + "_" + extraName;

        } else {
            loc = Constants.MOD_ID +
                    ':' +
                    "sensor" + "/" + name;

        }
        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void registerModels() {
        registerModels("sensor");
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        tooltip.add(Localization.translate("iu.sensor.info"));

        if (!nbt.getString("type").equals("")) {
            String s = nbt.getString("type");
            if (!s.equals("oil")) {
                tooltip.add(Localization.translate("iu.vein_info") + Localization.translate("iu." + s + ".name"));
            } else {
                tooltip.add(Localization.translate("iu.vein_info") + Localization.translate("iu.oil_vein"));
            }
            int x = nbt.getInteger("x");
            int z = nbt.getInteger("z");
            tooltip.add(Localization.translate("iu.modulewirelles1") + "x: " + x + " z: " + z);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            if (!nbt.getString("type").equals("")) {
                return getModelLocation1(name, nbt.getString("type"));
            }

            return getModelLocation1(name, "");

        });
        String[] mode = {"", "magnetite", "calaverite", "galena", "nickelite", "pyrite", "quartzite", "uranite", "azurite",
                "rhodonite", "alfildit", "euxenite", "smithsonite", "ilmenite", "todorokite", "ferroaugite", "sheelite",
                "oil"};
        for (final String s : mode) {
            if (s.equals("")) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
            }
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));

        }

    }


    public enum Types implements IIdProvider {
        sensor(0);

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
