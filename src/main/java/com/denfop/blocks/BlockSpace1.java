package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BlockSpace1 extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);


    public BlockSpace1() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("blockspace2");
        setCreativeTab(IUCore.OreTab);
        setHardness(1.0F);
        setResistance(5.0F);
        setSoundType(SoundType.METAL);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.europa_mikhail_ore));
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState().withProperty(VARIANT, Type.values()[meta]);
    }

    @Nonnull
    public IBlockState getStateMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Type.values()[meta]);
    }

    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (int i = 0; i < (Type.values()).length; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= (Type.values()).length) {
            meta = 0;
        }
        return "iu." + Type.values()[meta].getName() + ".name";
    }

    public EnumRarity getRarity(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= (Type.values()).length) {
            return EnumRarity.COMMON;
        }
        return Type.values()[meta].getRarity();
    }


    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    public int getLightValue(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return state.getValue(VARIANT).getLight();
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        for (int i = 0; i < (Type.values()).length; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(this),
                    i,
                    new ModelResourceLocation(this.modName + ":" + this.name, "type=" + Type.values()[i].getName())
            );
        }
    }

    public boolean preInit() {
        setRegistryName("blockspace2");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(Objects.requireNonNull(getRegistryName()));
        ForgeRegistries.ITEMS.register(itemBlock);
        IUCore.proxy.addIModelRegister(this);

        return true;
    }

    @Override
    public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
        NonNullList<ItemStack> ret = NonNullList.create();
        BlockSpace1.Type type = BlockSpace1.Type.values()[getMetaFromState(state)];
        if (type.ordinal() == 0 || type.ordinal() == 1 || type.ordinal() == 4 || type.ordinal() == 7 || type.ordinal() == 12 || type.ordinal() == 13 || type.ordinal() == 14) {
            ItemStack stack = type.getStack();
            if (stack == null) {
                stack = OreDictionary.getOres(type.getRaw()).get(0);
                type.setStack(stack);
            }
            stack = stack.copy();
            stack.setCount(1 + getDrop(fortune));
            ret.add(stack);
        } else if (type.ordinal() == 6) {
            final int i = WorldBaseGen.random.nextInt(fortune + 1) + 1;
            ret.add(new ItemStack(Items.DIAMOND, i));
        } else if (type.ordinal() == 10) {
            final int i = WorldBaseGen.random.nextInt(fortune + 1) + 1;
            ret.add(new ItemStack(Items.EMERALD, i));
        } else if (type.ordinal() == 11) {
            ret.add(ModUtils.setSize(IUItem.smallSulfurDust, 4 + WorldBaseGen.random.nextInt(fortune + 1)));
        } else if (type.ordinal() == 9) {
            final int i = WorldBaseGen.random.nextInt(fortune + 2) + 1;
            ret.add(new ItemStack(Items.QUARTZ, i));
        } else if (type.ordinal() == 5) {
            if (WorldBaseGen.random.nextDouble() < 0.5) {
                ret.add(new ItemStack(IUItem.apatite, 1, 1));
            } else {
                ret.add(new ItemStack(IUItem.apatite, 1, 0));
            }

        } else {
            ret.add(new ItemStack(this, 1, type.ordinal()));
        }
        return ret;
    }

    private int getDrop(int fortune) {
        switch (fortune) {
            case 0:
                return 0;
            case 1:
                return WorldBaseGen.random.nextDouble() < 0.25 ? 1 : 0;
            case 2:
                return WorldBaseGen.random.nextDouble() < 0.5 ? 1 : 0;
            default:
                return WorldBaseGen.random.nextDouble() < 0.75 ? 1 : 0;
        }
    }

    public enum Type implements IStringSerializable {
        europa_mikhail_ore(0),
        ganymede_aluminium_ore(1),
        ganymede_beryllium(2),
        ganymede_calcium(3),
        ganymede_tungsten_ore(4),
        haumea_apatite(5),
        haumea_diamond(6),
        haumea_iridium(7),
        haumea_lithium(8),
        haumea_quartz(9),
        io_emerald_ore(10),
        io_sulfur(11),
        makemake_bismuth(12),
        makemake_gold(13),
        makemake_silver(14),
        mars_adamantium_ore(15),
        ;

        private final int metadata;
        private final String name;
        ItemStack stack;
        private String raw;

        Type(int metadata) {
            this.metadata = metadata;
            this.name = this.name().toLowerCase(Locale.US);
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String name) {
            this.raw = "raw" + name;
        }

        public ItemStack getStack() {
            return stack;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        public int getLight() {
            return 0;
        }

        public EnumRarity getRarity() {
            return EnumRarity.COMMON;
        }


    }

}
