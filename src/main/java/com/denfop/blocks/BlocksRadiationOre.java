package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.blocks.state.BoolProperty;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class BlocksRadiationOre extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);
    public static final BoolProperty BOOL_PROPERTY = new BoolProperty("hasdamage");


    public BlocksRadiationOre() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("blockradiationore");
        setCreativeTab(IUCore.OreTab);
        setHardness(1.0F);
        setTickRandomly(true);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.americium_ore));
        setHarvestLevel("pickaxe", 1);
    }

    @Nonnull
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


    @Override
    public void getDrops(
            final NonNullList<ItemStack> drops,
            @Nonnull final IBlockAccess world,
            @Nonnull final BlockPos pos,
            final IBlockState state,
            final int fortune
    ) {
        drops.add(new ItemStack(this, 1, state.getBlock().getMetaFromState(state)));
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
        ModelLoader.setCustomStateMapper(this, block -> block.getBlockState().getValidStates().stream()
                .collect(Collectors.toMap(
                        state -> state,
                        state -> {
                            StateMapperIU stateMapper = new StateMapperIU(getRegistryName());
                            return stateMapper.getModelResourceLocation(state);
                        }
                )));
    }

    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) {
            return;
        }

        boolean damage = state.getValue(BOOL_PROPERTY);
        if (!damage) {
            return;
        }
        ChunkPos chunkPos = new ChunkPos(pos);
        new PacketUpdateRadiationValue(chunkPos, 1);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2,
                pos.getY() + 2, pos.getZ() + 2
        );
        final List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisAlignedBB);
        for (EntityPlayer player : list) {
            boolean can = !IHazmatLike.hasCompleteHazmat(player);
            if (can) {
                player.addPotionEffect(new PotionEffect(IUPotion.radiation, 400, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 0));

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


    public boolean preInit() {
        setRegistryName("blockradiationore");
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
        americium_ore(0),
        neptunium_ore(1),
        curium_ore(2),


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
