package com.denfop.items.panel;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.solar.IBatteryItem;
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

public class ItemBatterySolarPanel extends ItemSubTypes<ItemBatterySolarPanel.Types> implements IBatteryItem, IModelRegister {

    protected static final String NAME = "solar_panel_battery";

    public ItemBatterySolarPanel() {
        super(Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        tooltip.add(Localization.translate("iu.minipanel.storage") + (int) this.getCapacity(stack.getItemDamage()) + " EF");
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


    @Override
    public double getCapacity(final int damage) {
        return 1000 * Math.pow(2, damage);
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
