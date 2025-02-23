package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
import java.util.Random;

public class BlockSpace extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);


    public BlockSpace() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("blockspace");
        setCreativeTab(IUCore.OreTab);
        setHardness(1.0F);
        setResistance(5.0F);
        setSoundType(SoundType.METAL);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.ariel_draconium));
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
        return "iu." + Type.values()[meta].getName()  + ".name";
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
    @Override
    public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
        NonNullList<ItemStack> ret = NonNullList.create();
        Type type = Type.values()[getMetaFromState(state)];
        if (type.ordinal() != 3 && type.ordinal() != 4 && type.ordinal() != 5 && type.ordinal() != 0 && type.ordinal() != 10) {
            ItemStack stack = type.getStack();
            if (stack == null) {
                stack = OreDictionary.getOres(type.getRaw()).get(0);
                type.setStack(stack);
            }
            stack = stack.copy();
            stack.setCount(1 + getDrop(fortune));
            ret.add(stack);
        }else if (type.ordinal() == 5) {
            final int i = quantityRedstoneDroppedWithBonus(fortune, WorldBaseGen.random);
            ret.add(new ItemStack(Items.REDSTONE,i));
        }else if (type.ordinal() == 3){
            final int i = WorldBaseGen.random.nextInt(fortune + 2) - 1;
            ret.add(new ItemStack(Items.DYE,i,4));
        }else {
            ret.add(new ItemStack(this,1,type.ordinal()));
        }
        return ret;
    }
    public int quantityRedstoneDroppedWithBonus(int fortune, Random random)
    {
        return this.quantityRedstoneDropped(random) + random.nextInt(fortune + 1);
    }

    public int quantityRedstoneDropped(Random random)
    {
        return 4 + random.nextInt(2);
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
    public boolean preInit() {
        setRegistryName("blockspace");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(Objects.requireNonNull(getRegistryName()));
        ForgeRegistries.ITEMS.register(itemBlock);
        IUCore.proxy.addIModelRegister(this);

        return true;
    }


    public enum Type implements IStringSerializable {
        ariel_draconium(0),
        asteroid_barium_ore(1),
        asteroid_cadmium_ore(2),
        callisto_lapis_ore(3),
        callisto_lithium_ore(4),
        callisto_redstone_ore(5),
        ceres_copper_ore(6),
        ceres_iron_ore(7),
        charon_arsenic(8),
        charon_zinc(9),
        deimos_orichalcum_ore(10),
        dione_lead_ore(11),
        dione_platinum_ore(12),
        enceladus_magnesium_ore(13),
        eris_magnesium(14),
        eris_manganese(15),
        ;

        private final int metadata;
        private final String name;


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
        private String raw;
        public void setRaw(String name){
            this.raw = "raw"+name;
        }

        public String getRaw() {
            return raw;
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

        ItemStack stack;
        public ItemStack getStack() {
          return stack;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }
    }

}
