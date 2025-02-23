package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class BlockOres2 extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);


    public BlockOres2() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("baseore1");
        setCreativeTab(IUCore.OreTab);
        setHardness(1.0F);
        setSoundType(SoundType.GROUND);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.lithium));
        setHarvestLevel("shovel", 1);
        setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(VARIANT, Type.cadmium));
        setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(VARIANT, Type.tantalum));
        setHarvestLevel("pickaxe", 1, this.blockState.getBaseState().withProperty(VARIANT, Type.osmium));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World player,
            final List<String> tooltip,
            final ITooltipFlag advanced
    ) {
        if (stack.getItemDamage() >= 6) {
            tooltip.add(Localization.translate("iu.ore_spawn.info1"));
        }
    }

    @Override
    public SoundType getSoundType(final IBlockState state, final World world, final BlockPos pos, @Nullable final Entity entity) {
        final int meta = this.getMetaFromState(state);
        if (meta == 0 || meta > 5) {
            return SoundType.SAND;
        } else if (meta > 2) {
            return SoundType.STONE;
        }
        return super.getSoundType(state, world, pos, entity);

    }

    @Override
    public IBlockState getStateFromMeta(final int meta) {
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
    @Override
    public void getDrops(
            @Nonnull final NonNullList<ItemStack> drops,
            @Nonnull final IBlockAccess world,
            @Nonnull final BlockPos pos,
            @Nonnull final IBlockState state,
            final int fortune
    ) {
        Random rand = WorldBaseGen.random;

        final int meta = this.getMetaFromState(state);
        if (meta >= 3&& meta <= 5) {

            drops.add(new ItemStack(IUItem.rawMetals, 1 + getDrop(fortune), getMetaFromState(state)-3+22));
        }
        return;
    }


    private int getDrop(int fortune) {
        switch (fortune) {
            case 0:
                return 0;
            case 1:
                return WorldBaseGen.random.nextInt(100) < 25 ? 1 : 0;
            case 2:
                return WorldBaseGen.random.nextInt(100) < 50 ? 1 : 0;
            default:
                return WorldBaseGen.random.nextInt(100) < 75 ? 1 : 0;
        }
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
        setRegistryName("baseore1");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(Objects.requireNonNull(getRegistryName()));
        ForgeRegistries.ITEMS.register(itemBlock);
        IUCore.proxy.addIModelRegister(this);

        return true;
    }

    public boolean initialize() {

        return true;
    }

    public enum Type implements IStringSerializable {
        lithium(0),
        beryllium(1),
        bor(2),

        osmium(3),
        tantalum(4),
        cadmium(5),
        saltpeter(6),
        calcium(7);

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
