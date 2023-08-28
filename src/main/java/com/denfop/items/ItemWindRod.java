package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemWindRod extends ItemSubTypes<ItemWindRod.Types> implements IModelRegister {

    protected static final String NAME = "windrod";

    public ItemWindRod() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public boolean getLevel(int level, int damage) {
        if (level == 9 && damage == 10) {
            return true;
        } else if (level == 10 && damage == 9) {
            return true;
        } else {
            return level - 1 == damage;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        tooltip.add(Localization.translate("wind.need_level3"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
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

    public enum Types implements ISubEnum {
        oak_rotor_model(0),
        bronze_rotor_model(1),
        iron_rotor_model(2),
        steel_rotor_model(3),
        carbon_rotor_model(4),
        carbon_rotor_model1(5),
        carbon_rotor_model2(6),
        carbon_rotor_model3(7),
        carbon_rotor_model4(8),
        carbon_rotor_model5(9),
        carbon_rotor_model6(10),
        carbon_rotor_model7(11),
        carbon_rotor_model8(12),
        carbon_rotor_model9(13),
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
