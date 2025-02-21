package com.denfop.blocks;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.items.ItemBlockLeaves;
import com.denfop.register.Register;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IULeaves extends BlockLeaves implements IModelRegister {

    public static final PropertyEnum<LeavesType> typeProperty = PropertyEnum.create("type", LeavesType.class);

    public IULeaves() {
        this.setUnlocalizedName("leaves");
        this.setCreativeTab(IUCore.IUTab);
        ResourceLocation name = IUCore.getIdentifier("leaves");
        Register.registerBlock(this, name);
        Register.registerItem(new ItemBlockLeaves(this), name);
        this.setDefaultState(this.blockState
                .getBaseState()
                .withProperty(CHECK_DECAY, true)
                .withProperty(DECAYABLE, true)
                .withProperty(typeProperty, IULeaves.LeavesType.rubber));
        IUCore.proxy.addIModelRegister(this);
    }

    private static IBlockState getDropState(IBlockState state) {
        return state.withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        IStateMapper mapper = (new StateMap.Builder()).ignore(CHECK_DECAY, DECAYABLE).build();
        ModelLoader.setCustomStateMapper(this, mapper);
        List<IBlockState> states = new ArrayList(typeProperty.getAllowedValues().size());
        LeavesType[] var4 = IULeaves.LeavesType.values;

        for (LeavesType type : var4) {
            states.add(getDropState(this.getDefaultState().withProperty(typeProperty, type)));
        }

        BlockBase.registerItemModels(this, states, mapper);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE, typeProperty);
    }

    public IBlockState getStateFromMeta(int meta) {
        boolean checkDecay = (meta & 8) != 0;
        boolean decayable = (meta & 4) != 0;
        meta &= 3;
        IBlockState ret = this.getDefaultState().withProperty(CHECK_DECAY, checkDecay).withProperty(DECAYABLE, decayable);
        if (meta < IULeaves.LeavesType.values.length) {
            ret = ret.withProperty(typeProperty, IULeaves.LeavesType.values[meta]);
        }

        return ret;
    }

    public int getMetaFromState(IBlockState state) {
        int ret = 0;
        if (state.getValue(CHECK_DECAY)) {
            ret |= 8;
        }

        if (state.getValue(DECAYABLE)) {
            ret |= 4;
        }

        ret |= state.getValue(typeProperty).ordinal();
        return ret;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return super.isOpaqueCube(state);
    }

    @Override
    public boolean isNormalCube(
            final IBlockState p_isNormalCube_1_,
            final IBlockAccess p_isNormalCube_2_,
            final BlockPos p_isNormalCube_3_
    ) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {

        return true;
    }

    @Override
    public boolean doesSideBlockRendering(
            final IBlockState p_doesSideBlockRendering_1_,
            final IBlockAccess p_doesSideBlockRendering_2_,
            final BlockPos p_doesSideBlockRendering_3_,
            final EnumFacing p_doesSideBlockRendering_4_
    ) {
        return false;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(typeProperty).getSapling().getItem();
    }

    public int damageDropped(IBlockState state) {
        return state.getValue(typeProperty).getSapling().getMetadata();
    }

    public int getSaplingDropChance(IBlockState state) {
        return state.getValue(typeProperty).saplingDropChance;
    }

    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        IBlockState state = getDropState(world.getBlockState(pos));
        return Collections.singletonList(new ItemStack(this, 1, this.getMetaFromState(state)));
    }

    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        IBlockState state = getDropState(this.getDefaultState());
        LeavesType[] var4 = IULeaves.LeavesType.values;
        for (LeavesType type : var4) {
            list.add(new ItemStack(this, 1, this.getMetaFromState(state.withProperty(typeProperty, type))));
        }

    }

    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 30;
    }

    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 20;
    }

    public enum LeavesType implements IStringSerializable {
        rubber(35);

        private static final LeavesType[] values = values();
        public final int saplingDropChance;

        LeavesType(int saplingDropChance) {
            this.saplingDropChance = saplingDropChance;
        }

        public String getName() {
            return this.name();
        }

        public ItemStack getSapling() {
            return new ItemStack(IUItem.rubberSapling);
        }
    }

}
