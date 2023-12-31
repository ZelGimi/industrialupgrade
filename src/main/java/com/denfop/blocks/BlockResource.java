package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class BlockResource extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);


    public BlockResource() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("blockresource");
        setCreativeTab(IUCore.IUTab);
        setHardness(3.0F);
        setResistance(10.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.bronze_block));
        setHarvestLevel("pickaxe", 2);
    }

    public ItemStack getItemStack(Type type) {
        return this.getItemStack(this.getState(type));
    }

    public IBlockState getState(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("invalid type: " + type);
        } else {
            return this.getDefaultState().withProperty(VARIANT, type);
        }
    }

    public ItemStack getItemStack(IBlockState state) {
        if (state.getBlock() != this) {
            return null;
        } else {
            Item item = Item.getItemFromBlock(this);
            if (item != Items.AIR) {
                int meta = this.getMetaFromState(state);
                return new ItemStack(item, 1, meta);
            } else {
                throw new RuntimeException("no matching item for " + this);
            }
        }
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
    public float getExplosionResistance(
            final World world,
            final BlockPos pos,
            @Nullable final Entity exploder,
            final Explosion explosion
    ) {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(VARIANT).getExplosionResistance();
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
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(VARIANT).getHardness();
    }

    public boolean preInit() {
        setRegistryName("blockresource");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(Objects.requireNonNull(getRegistryName()));
        ForgeRegistries.ITEMS.register(itemBlock);
        IUCore.proxy.addIModelRegister(this);

        return true;
    }

    @Override
    public SoundType getSoundType(final IBlockState state, final World world, final BlockPos pos, @Nullable final Entity entity) {
        return state.getValue(VARIANT).metal ? SoundType.METAL : SoundType.STONE;
    }

    public boolean initialize() {

        return true;
    }

    public enum Type implements IStringSerializable {
        basalt(20.0F, 45.0F, false),
        bronze_block(5.0F, 10.0F, true),
        copper_block(4.0F, 10.0F, true),
        lead_block(4.0F, 10.0F, true),
        steel_block(8.0F, 10.0F, true),
        tin_block(4.0F, 10.0F, true),
        uranium_block(6.0F, 10.0F, true),
        reinforced_stone(80.0F, 180.0F, false),
        machine(5.0F, 10.0F, true),
        advanced_machine(8.0F, 10.0F, true),

        ;
        private final float hardness;
        private final float explosionResistance;
        private final boolean metal;
        private final int metadata;
        private final String name;

        Type(float hardness, float explosionResistance, boolean metal) {
            this.metadata = this.ordinal();
            this.name = this.name().toLowerCase(Locale.US);
            this.hardness = hardness;
            this.explosionResistance = explosionResistance;
            this.metal = metal;
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public float getExplosionResistance() {
            return explosionResistance;
        }

        public float getHardness() {
            return hardness;
        }

        public boolean isMetal() {
            return metal;
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
