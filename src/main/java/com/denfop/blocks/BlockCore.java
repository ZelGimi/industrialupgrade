package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.DataBlock;
import com.denfop.DataMultiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import java.util.List;

import static net.minecraft.world.level.storage.loot.parameters.LootContextParams.*;

public abstract class BlockCore<T extends Enum<T> & ISubEnum> extends Block {
    final String modName;
    private final T[] elements;
    private final DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> data;
    private final DataMultiBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> multiData;
    private final T element;

    public BlockCore(Material material, T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of(material));
        this.elements = elements;
        this.data = dataBlock;
        this.element = element;
        this.multiData = null;
        this.modName = Constants.MOD_ID;
    }

    public BlockCore(Properties properties, T[] elements, DataMultiBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(properties);
        this.elements = elements;
        this.multiData = dataBlock;
        this.data = null;
        this.element = elements[0];
        this.modName = Constants.MOD_ID;
    }

    public BlockCore(Properties properties, T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(properties);
        this.elements = elements;
        this.data = dataBlock;
        this.element = element;
        this.modName = Constants.MOD_ID;
        this.multiData = null;
    }

    public DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> getData() {
        return data;
    }

    public T getElement() {
        return this.element;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        if (data != null)
            return new ItemStack(this.data.getItem(getMetaFromState(state)));
        if (multiData != null)
            return new ItemStack(this.multiData.getItem(getMetaFromState(state)));
        return ItemStack.EMPTY;
    }

    public DataMultiBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> getMultiData() {
        return multiData;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        ItemStack tool = p_60538_.getParameter(TOOL);
        BlockPos pos = new BlockPos(p_60538_.getParameter(ORIGIN));
        List<ItemStack> drops = NonNullList.create();
        if (tool.isEmpty() && (p_60538_.getOptionalParameter(THIS_ENTITY) instanceof Player player))
            tool = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
            drops = getDropsSilk(p_60538_.getLevel(), pos, p_60537_, 0);
        else
            drops = getDrops(p_60538_.getLevel(), pos, p_60537_, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool));

        return drops;
    }
    public List<ItemStack> getDropsSilk(
            @Nonnull final Level world,
            @Nonnull final BlockPos pos,
            @Nonnull final BlockState state,
            final int fortune
    ) {
        if (data != null) {
            return List.of(new ItemStack(this.getData().getItem(getMetaFromState(state))));

        }
        if (multiData != null) {
            return List.of(new ItemStack(this.getMultiData().getItem(getMetaFromState(state))));

        }
        return List.of(ItemStack.EMPTY);
    }

    public List<ItemStack> getDrops(
            @Nonnull final Level world,
            @Nonnull final BlockPos pos,
            @Nonnull final BlockState state,
            final int fortune
    ) {
        if (data != null) {
            return List.of(new ItemStack(this.getData().getItem(getMetaFromState(state))));

        }
        if (multiData != null) {
            return List.of(new ItemStack(this.getMultiData().getItem(getMetaFromState(state))));

        }
        return List.of(ItemStack.EMPTY);
    }

      int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    public T[] getElements() {
        return elements;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_49812_, NonNullList<ItemStack> p_49813_) {
        super.fillItemCategory(p_49812_, p_49813_);
    }


    public  abstract <T extends Enum<T> & ISubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context);

    public abstract <T extends Enum<T> & ISubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element);
}
