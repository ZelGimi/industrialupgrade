package com.denfop.items.bee;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.bee.IBee;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemJarBees extends ItemSubTypes<ItemJarBees.Types> implements IModelRegister {

    protected static final String NAME = "jar_bee";

    public ItemJarBees() {
        super(Types.class);
        this.setCreativeTab(IUCore.BeesTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public static IBee getBee(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        IBee bee = BeeNetwork.instance.getBee(nbt.getInteger("bee_id"));
        if (bee == null) {
            return null;
        }
        return bee.copy();
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

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("iu.use_bee_analyzer") + Localization.translate(IUItem.bee_analyzer.getUnlocalizedName()));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(Localization.translate("iu.bee_negative"));
        IBee bee = getBee(p_77624_1_);
        if (bee != null) {
            List<IBee> unCompatibleBees = bee.getUnCompatibleBees();
            for (IBee bee1 : unCompatibleBees) {
                p_77624_3_.add(Localization.translate("bee_" + bee1.getName()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, final int meta, final String extraName) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            int mode = nbt.getInteger("bee_id");
            switch (mode) {
                case 1:
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + "winter_bee",
                            null
                    );
                case 2:
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + "forest_bee",
                            null
                    );
                case 3:
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + "tropical_bee",
                            null
                    );
                case 4:
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + "plains_bee",
                            null
                    );
                case 5:
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + "swamp_bee",
                            null
                    );
            }
            return new ModelResourceLocation(
                    Constants.MOD_ID + ":" + NAME + "/" + "forest_bee",
                    null
            );
        });
        String[] mode = {"winter_bee", "forest_bee", "tropical_bee", "plains_bee", "swamp_bee"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, new ModelResourceLocation(
                    Constants.MOD_ID + ":" + NAME + "/" + s,
                    null
            ));
        }

    }

    public ItemStack getStackFromId(int id) {
        ItemStack stack = new ItemStack(this);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("bee_id", id);
        return stack;
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final IBee crop = BeeNetwork.instance.getBee(nbt.getInteger("bee_id"));
        if (nbt.hasKey("bee_id")) {
            return super.getItemStackDisplayName(stack) + ": " + Localization.translate("bee_" + crop.getName());
        } else {
            return super.getItemStackDisplayName(stack);
        }
    }


    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            BeeNetwork.instance.getBeeMap().forEach((id, crop) -> {
                ItemStack stack = new ItemStack(this);
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                nbt.setInteger("bee_id", id);
                subItems.add(stack);
            });
        }
    }

    public enum Types implements ISubEnum {
        bees,
        ;

        private final String name;
        private final int ID;

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
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
