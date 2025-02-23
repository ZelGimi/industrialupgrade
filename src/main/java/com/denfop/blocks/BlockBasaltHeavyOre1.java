package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class BlockBasaltHeavyOre1 extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);


    public BlockBasaltHeavyOre1() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("basaltheavyore1");
        setCreativeTab(IUCore.OreTab);
        setHardness(1.0F);
        setResistance(3.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.arsenopyrite));
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public boolean canSilkHarvest(
            final World p_canSilkHarvest_1_,
            final BlockPos p_canSilkHarvest_2_,
            final IBlockState p_canSilkHarvest_3_,
            final EntityPlayer p_canSilkHarvest_4_
    ) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(
            final IBlockState state,
            final RayTraceResult target,
            final World world,
            final BlockPos pos,
            final EntityPlayer player
    ) {
        return new ItemStack(IUItem.mineral, 1, getMetaFromState(state));
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(IUItem.mineral, 1, this.getMetaFromState(state)));
        drops.add(new ItemStack(IUItem.basalts, 1, 0));
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

    @Nonnull
    public IBlockState getStateMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Type.values()[meta % 13]);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState().withProperty(VARIANT, Type.values()[meta]);
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
        setRegistryName("basaltheavyore1");
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
        arsenopyrite(0),
        braggite(1),
        wolframite(2),
        germanite(3),
        coltan(4),
        crocoite(5),
        xenotime(6),
        iridosmine(7),
        theophrastite(8),
        tetrahedrite(9),
        fergusonite(10),
        celestine(11),
        zircon(12),
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
