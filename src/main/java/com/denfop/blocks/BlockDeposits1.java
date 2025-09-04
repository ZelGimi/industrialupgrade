package com.denfop.blocks;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IBaseRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.datagen.blocktags.BlockTagsProvider;
import com.denfop.datagen.blocktags.IBlockTag;
import com.denfop.dataregistry.DataBlock;
import com.denfop.items.energy.ItemHammer;
import com.denfop.utils.Localization;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN;
import static net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL;

public class BlockDeposits1<T extends Enum<T> & SubEnum> extends BlockCore<T> implements IBlockTag, SimpleWaterloggedBlock, com.denfop.blocks.Deposits {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape Deposits = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
    Map<Integer, List<String>> mapInf = new HashMap<>();

    public BlockDeposits1(T[] elements, T element, DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock) {
        super(Properties.of().mapColor(MapColor.STONE).destroyTime(3f).noOcclusion().explosionResistance(5F).sound(SoundType.STONE).requiresCorrectToolForDrops(), elements, element, dataBlock);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
        BlockTagsProvider.list.add(this);
    }

    public static boolean isFree(BlockState p_53242_) {
        return p_53242_.isAir() || p_53242_.is(BlockTags.FIRE) || p_53242_.liquid() || p_53242_.canBeReplaced();
    }

    @Override
    public List<String> getInformationFromMeta() {
        int meta = this.getElement().getId();
        List<String> inf = mapInf.get(meta);
        if (inf == null) {
            final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
            List<String> stringList = new ArrayList<>();
            final String s = Localization.translate("deposists.jei1") + (vein.getHeavyOre() != null ?
                    new ItemStack(vein.getHeavyOre().getBlock(), 1).getDisplayName().getString() :
                    new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1
                    ).getDisplayName().getString());
            stringList.add(s);
            if (vein.getHeavyOre() != null) {
                final String s1 = new ItemStack(vein.getHeavyOre().getBlock(), 1).getDisplayName().getString() + " 50%";
                stringList.add(s1);
                for (int i = 0; i < vein.getOres().size(); i++) {
                    final ChanceOre chanceOre = vein.getOres().get(i);
                    String s2 =
                            new ItemStack(
                                    chanceOre.getBlock().getBlock(),
                                    1
                            ).getDisplayName().getString() + " " + chanceOre.getChance() + "%";
                    stringList.add(s2);
                }
            } else {
                for (int i = 0; i < vein.getOres().size(); i++) {
                    final ChanceOre chanceOre = vein.getOres().get(i);
                    String s2 =
                            new ItemStack(
                                    chanceOre.getBlock().getBlock(),
                                    1
                            ).getDisplayName().getString() + " " + chanceOre.getChance() + "%";
                    stringList.add(s2);
                }
            }
            mapInf.put(meta, stringList);
            return stringList;
        } else {
            return inf;
        }
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return getElement().getId();
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        int levelEnchant = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem());
        return levelEnchant == 0;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootParams.Builder p_60538_) {
        ItemStack tool = p_60538_.getParameter(TOOL);
        BlockPos pos = new BlockPos((int) p_60538_.getParameter(ORIGIN).x, (int) p_60538_.getParameter(ORIGIN).y, (int) p_60538_.getParameter(ORIGIN).z);
        List<ItemStack> drops = NonNullList.create();
        drops = getDrops(p_60538_.getLevel(), pos, p_60537_, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool), tool);
        return drops;
    }

    public List<ItemStack> getDrops(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, int fortune, ItemStack tool) {
        List<ItemStack> drops = new ArrayList<>();
        if (tool.getItem() instanceof ItemHammer) {
            final int meta = getMetaFromState(state);
            final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
            if (vein.getHeavyOre() == null) {
                final ChanceOre block = vein.getOres().get(0);
                drops.add(new ItemStack(block.getBlock().getBlock(), 1));
            } else {
                final IBaseRecipe recipe = Recipes.recipes.getRecipe("handlerho");
                final List<BaseMachineRecipe> recipe_list = Recipes.recipes.getRecipeList("handlerho");
                final MachineRecipe output = Recipes.recipes.getRecipeMachineRecipeOutput(
                        recipe,
                        recipe_list,
                        false,
                        Collections.singletonList(new ItemStack(vein.getHeavyOre().getBlock(), 1))
                );
                if (output != null) {
                    final int[] col = new int[output.getRecipe().output.items.size()];
                    for (int i = 0; i < col.length; i++) {
                        col[i] = output.getRecipe().output.metadata.getInt(("input" + i));
                        col[i] = Math.min(col[i], 95);
                    }
                    List<ItemStack> stacks = new ArrayList<>();
                    for (int i = 0; i < col.length; i++) {
                        final RandomSource rand = world.random;
                        if ((rand.nextInt(100) < col[i])) {
                            stacks.add(output.getRecipe().output.items.get(i));
                        }
                    }
                    for (ItemStack stack : stacks) {
                        final BaseMachineRecipe rec1 = Recipes.recipes.getRecipeOutput("macerator", false, stack);
                        if (rec1 != null) {
                            ItemStack stack1 = rec1.output.items.get(0).copy();
                            stack1.setCount(1);
                            drops.add(stack1);
                        } else {
                            drops.add(stack.copy());
                        }
                    }
                }
            }
        } else {
            final int meta = getMetaFromState(state);
            final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
            if (vein.getHeavyOre() == null) {
                final ChanceOre block = vein.getOres().get(0);
                drops.add(new ItemStack(block.getBlock().getBlock(), 1));
            } else {
                drops.add(new ItemStack(vein.getHeavyOre().getBlock(), 1));
            }
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        final int meta = getMetaFromState(pState);
        final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
        if (vein.getHeavyOre() == null) {
            final ChanceOre block = vein.getOres().get(0);
            return new ItemStack(block.getBlock().getBlock(), 1);
        } else {
            return new ItemStack(vein.getHeavyOre().getBlock(), 1);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        final int meta = getMetaFromState(state);
        final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
        if (vein.getHeavyOre() == null) {
            final ChanceOre block = vein.getOres().get(0);
            return new ItemStack(block.getBlock().getBlock(), 1);
        } else {
            return new ItemStack(vein.getHeavyOre().getBlock(), 1);
        }
    }

    public BlockState updateShape(BlockState p_154530_, Direction p_154531_, BlockState p_154532_, LevelAccessor p_154533_, BlockPos p_154534_, BlockPos p_154535_) {
        BlockState blockstate = super.updateShape(p_154530_, p_154531_, p_154532_, p_154533_, p_154534_, p_154535_);
        p_154533_.scheduleTick(p_154534_, this, this.getDelayAfterPlace());
        FluidState fluidState = p_154533_.getFluidState(p_154534_);
        if (!blockstate.isAir() && fluidState.getType() == Fluids.WATER) {
            p_154533_.scheduleTick(p_154534_, Fluids.WATER, Fluids.WATER.getTickDelay(p_154533_));
        }

        return blockstate;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public FluidState getFluidState(BlockState state) {
        if (Boolean.TRUE.equals(state.getValue(WATERLOGGED)))
            return Fluids.WATER.getSource(false);
        return Fluids.EMPTY.defaultFluidState();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);

    }

    public boolean canPlaceLiquid(BlockGetter p_154505_, BlockPos p_154506_, BlockState p_154507_, Fluid p_154508_) {
        return false;
    }

    public boolean placeLiquid(LevelAccessor p_154520_, BlockPos p_154521_, BlockState p_154522_, FluidState p_154523_) {
        return false;
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState p_181242_, BlockGetter p_181243_, BlockPos p_181244_) {
        return false;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return true;
    }

    public boolean canBeReplaced(BlockState p_53035_, Fluid p_53036_) {
        return false;
    }

    @Override
    public <T extends Enum<T> & SubEnum> BlockState getStateForPlacement(T element, BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public void onPlace(BlockState p_53233_, Level p_53234_, BlockPos p_53235_, BlockState p_53236_, boolean p_53237_) {
        p_53234_.scheduleTick(p_53235_, this, this.getDelayAfterPlace());
    }

    public void tick(BlockState p_221124_, ServerLevel p_221125_, BlockPos p_221126_, RandomSource p_221127_) {
        if (isFree(p_221125_.getBlockState(p_221126_.below())) && p_221126_.getY() >= p_221125_.getMinBuildHeight()) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(p_221125_, p_221126_, p_221124_);
            this.falling(fallingblockentity);
        }
    }

    public void animateTick(BlockState p_221129_, Level p_221130_, BlockPos p_221131_, RandomSource p_221132_) {
        if (p_221132_.nextInt(16) == 0) {
            BlockPos blockpos = p_221131_.below();
            if (isFree(p_221130_.getBlockState(blockpos))) {
                double d0 = (double) p_221131_.getX() + p_221132_.nextDouble();
                double d1 = (double) p_221131_.getY() - 0.05D;
                double d2 = (double) p_221131_.getZ() + p_221132_.nextDouble();
                p_221130_.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, p_221129_), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void falling(FallingBlockEntity p_53206_) {
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.scheduleTick(pos, this, 3);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return Deposits;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return Deposits;
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_60479_, BlockGetter p_60480_, BlockPos p_60481_, CollisionContext p_60482_) {
        return Deposits;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState p_60547_, BlockGetter p_60548_, BlockPos p_60549_) {
        return super.getInteractionShape(p_60547_, p_60548_, p_60549_);
    }


    @Override
    public <T extends Enum<T> & SubEnum> void fillItemCategory(CreativeModeTab p40569, NonNullList<ItemStack> p40570, T element) {
        p40570.add(new ItemStack(this.stateDefinition.any().getBlock()));
    }


    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public Pair<String, Integer> getHarvestLevel() {
        return new Pair<>("pickaxe", 0);
    }

    public enum Type implements SubEnum {
        deposits_iridium(0),
        deposits_germanium(1),
        deposits_vanadium(2),

        deposits_osmium(3),

        deposits_tantalum(4),
        deposits_cadmium(5),
        deposits_copper(6),
        deposits_tin(7),

        deposits_arsenopyrite(8),
        deposits_braggite(9),
        deposits_wolframite(10),

        deposits_xenotime(11),

        deposits_tetrahedrite(12),
        deposits_crocoite(13),
        deposits_zircon(14),
        deposits_celestine(15),

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

        @Override
        public int getId() {
            return this.metadata;
        }

        @Override
        public String getOtherPart() {
            return "type=";
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "deposits1";
        }

        @Override
        public boolean canAddToTab() {
            return false;
        }

        public int getLight() {
            return 0;
        }


    }
}
