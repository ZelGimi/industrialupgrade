package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IVolcanoArmor;
import com.denfop.blocks.state.BoolProperty;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockBasalts extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);
    public static final BoolProperty BOOL_PROPERTY = new BoolProperty("hasdamage");


    public BlockBasalts() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("blockbasalts");
        setCreativeTab(IUCore.IUTab);
        setHardness(1.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.basalt));
        setHarvestLevel("pickaxe", 1);
        setTickRandomly(true);
    }

    public static boolean canFallThrough(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
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

    @Override
    public void getDrops(
            @Nonnull final NonNullList<ItemStack> drops,
            @Nonnull final IBlockAccess world,
            @Nonnull final BlockPos pos,
            @Nonnull final IBlockState state,
            final int fortune
    ) {
        Random rand = world instanceof World ? ((World) world).rand : RANDOM;

        final int meta = this.getMetaFromState(state);
        if (meta == 6) {
            drops.add(new ItemStack(IUItem.iudust, rand.nextInt(2) + 1 + rand.nextInt(fortune + 1), 31));
            return;
        }
        if (meta == 7) {
            drops.add(new ItemStack(IUItem.crafting_elements, rand.nextInt(2) + 1 + rand.nextInt(fortune + 1), 477));
            return;
        }
        if (meta == 5) {
            drops.add(new ItemStack(IUItem.basalts, 1, 2));
            return;
        }
        if (meta == 1) {
            drops.add(new ItemStack(IUItem.basalts, 1, 8));
            return;
        }
        super.getDrops(drops, world, pos, state, fortune);
    }

    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) {
            return;
        }

        boolean damage = state.getValue(BOOL_PROPERTY);
        if (!damage) {
            return;
        }

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2,
                pos.getY() + 2, pos.getZ() + 2
        );
        final List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB);
        for (EntityPlayer player : list) {
            boolean can = !IVolcanoArmor.hasCompleteHazmat(player);
            if (can) {
                player.addPotionEffect(new PotionEffect(IUPotion.poison_gas, 200, 0));

            }


        }

    }

    @Override
    public void onBlockPlacedBy(
            final World p_180633_1_,
            final BlockPos p_180633_2_,
            IBlockState p_180633_3_,
            final EntityLivingBase p_180633_4_,
            final ItemStack p_180633_5_
    ) {
        super.onBlockPlacedBy(p_180633_1_, p_180633_2_, p_180633_3_, p_180633_4_, p_180633_5_);
        p_180633_3_ = p_180633_3_.withProperty(BOOL_PROPERTY, false);
        p_180633_1_.setBlockState(p_180633_2_, p_180633_3_);
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
        return new BlockStateContainer(this, VARIANT, BOOL_PROPERTY);
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
        final int meta = this.getMetaFromState(state);
        if (meta == 4) {
            return 4;
        }
        return this.lightValue;
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
        ModelLoader.setCustomStateMapper(this, block -> block.getBlockState().getValidStates().stream()
                .collect(Collectors.toMap(
                        state -> state,
                        state -> {
                            StateMapperIU stateMapper = new StateMapperIU(getRegistryName());
                            return stateMapper.getModelResourceLocation(state);
                        }
                )));
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(VARIANT).getHardness();
    }

    public boolean preInit() {
        setRegistryName("blockbasalts");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(Objects.requireNonNull(getRegistryName()));
        ForgeRegistries.ITEMS.register(itemBlock);
        IUCore.proxy.addIModelRegister(this);

        return true;
    }

    public void onEntityCollidedWithBlock(
            @Nonnull World worldIn,
            @Nonnull BlockPos pos,
            @Nonnull IBlockState state,
            @Nonnull Entity entityIn
    ) {
        final int meta = this.getMetaFromState(state);
        if (meta == 4) {
            if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment(
                    (EntityLivingBase) entityIn)) {
                entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
            }
        }

    }

    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess p_185484_2_, BlockPos p_185484_3_) {
        final int meta = this.getMetaFromState(state);
        if (meta == 4) {
            return 15728880;
        }
        return super.getPackedLightmapCoords(state, p_185484_2_, p_185484_3_);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

    }

    protected void onStartFalling(EntityFallingBlock fallingEntity) {
    }

    private void checkFallable(World worldIn, BlockPos pos) {
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {


            if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
                if (!worldIn.isRemote) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(
                            worldIn,
                            (double) pos.getX() + 0.5D,
                            (double) pos.getY(),
                            (double) pos.getZ() + 0.5D,
                            worldIn.getBlockState(pos)
                    );
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntity(entityfallingblock);
                }
            } else {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down()) {
                    ;
                }

                if (blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos.up(), state); //Forge: Fix loss of state information during world gen.
                }
            }
        }
    }

    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
    }

    public void onBroken(World worldIn, BlockPos pos) {
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

    }

    public void updateTick(World p_180650_1_, BlockPos p_180650_2_, IBlockState p_180650_3_, Random p_180650_4_) {
        final int meta = this.getMetaFromState(p_180650_3_);
        if (!p_180650_1_.isRemote && meta == 5) {

        }


    }

    public boolean canEntitySpawn(IBlockState p_189872_1_, Entity p_189872_2_) {
        return p_189872_2_.isImmuneToFire();
    }

    @Deprecated
    public int getLightValue(IBlockState state) {
        final int meta = this.getMetaFromState(state);
        if (meta == 4) {
            return 3;
        }
        return this.lightValue;
    }


    @Override
    public SoundType getSoundType(final IBlockState state, final World world, final BlockPos pos, @Nullable final Entity entity) {
        return state.getValue(VARIANT).metal ? SoundType.METAL : SoundType.STONE;
    }

    public boolean initialize() {

        return true;
    }

    public enum Type implements IStringSerializable {
        basalt(5, 45.0F, false),
        basalt_cobblestone(5, 45.0F, false),
        basalt_melted(10, 45.0F, false),
        basalt_blocked(5, 45.0F, false),
        basalt_magma(2.5F, 45.0F, false),
        basalt_pylon(5, 45.0F, false),
        basalt_sulfur_ore(5, 45.0F, false),
        basalt_boron_ore(10, 45.0F, false),
        basalt_spongy(5, 45.0F, false),
        basalt_smooth(5, 45.0F, false),
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
