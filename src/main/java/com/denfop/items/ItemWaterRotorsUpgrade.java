package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemWaterRotorsUpgrade extends ItemMulti<ItemWaterRotorsUpgrade.Types> implements IModelRegister {

    protected static final String NAME = "water_rotors_upgrade";

    public ItemWaterRotorsUpgrade() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ModuleTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public EnumInfoRotorUpgradeModules getType(int meta) {
        return EnumInfoRotorUpgradeModules.getFromID(meta);

    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            @Nonnull final ITooltipFlag flagIn
    ) {
        final RotorUpgradeItemInform upgrade = new RotorUpgradeItemInform(getType(stack.getItemDamage()), 1);
        tooltip.add(upgrade.getName());
        tooltip.add(Localization.translate("iu.upgrade_item.info") + upgrade.upgrade.max);
        switch (stack.getItemDamage()) {
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 17:
            case 18:
            case 19:
                tooltip.add(TextFormatting.RED + Localization.translate("wind.limit_upgrades.info"));

        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
        water_rotorupgrade(0),
        water_rotorupgrade1(1),
        water_rotorupgrade2(2),
        water_rotorupgrade3(3),
        water_rotorupgrade4(4),
        water_rotorupgrade5(5),
        water_rotorupgrade6(6),
        water_rotorupgrade7(7),
        water_rotorupgrade8(8),
        water_rotorupgrade9(9),
        water_rotorupgrade10(10),
        water_rotorupgrade11(11),
        water_rotorupgrade12(12),
        water_rotorupgrade13(13),
        water_rotorupgrade14(14),
        water_rotorupgrade15(15),
        water_rotorupgrade16(16),
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
