package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IBaseRecipe;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.items.energy.ItemHammer;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class BlockDeposits1 extends BlockCore implements IModelRegister, IDeposits {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);
    public static final AxisAlignedBB Deposits = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.2D, 1.0D);
    Map<Integer, List<String>> mapInf = new HashMap<>();

    public BlockDeposits1() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("deposits1");
        setCreativeTab(IUCore.OreTab);
        setHardness(0.2F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.deposits_iridium));
        setHarvestLevel("pickaxe", 0);
    }

    public static boolean canFallThrough(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return Deposits;
    }

    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {

    }

    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return false;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, 2);
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, 2);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.checkFallable(worldIn, pos);
        }
    }
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    @Override
    public List<String> getInformationFromMeta(final int meta) {
        List<String> inf = mapInf.get(meta);
        if (inf == null) {
            final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
            List<String> stringList = new ArrayList<>();
            final String s = Localization.translate("deposists.jei1") + (vein.getHeavyOre() != null ?
                    new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()).getDisplayName() :
                    new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1,
                            vein.getOres().get(0).getMeta()
                    ).getDisplayName());
            stringList.add(s);
            if (vein.getHeavyOre() != null) {
                final String s1 = new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()).getDisplayName() + " 50%";
                stringList.add(s1);
                for (int i = 0; i < vein.getOres().size(); i++) {
                    final ChanceOre chanceOre = vein.getOres().get(i);
                    String s2 =
                            new ItemStack(chanceOre.getBlock().getBlock(),
                                    1,
                                    chanceOre.getMeta()).getDisplayName() + " " + chanceOre.getChance() + "%";
                    stringList.add(s2);
                }
            } else {
                for (int i = 0; i < vein.getOres().size(); i++) {
                    final ChanceOre chanceOre = vein.getOres().get(i);
                    String s2 =
                            new ItemStack(chanceOre.getBlock().getBlock(),
                                    1,
                                    chanceOre.getMeta()).getDisplayName() + " " + chanceOre.getChance() + "%";
                    stringList.add(s2);
                }
            }
            mapInf.put(meta, stringList);
            return stringList;
        } else {
            return inf;
        }
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

    protected void onStartFalling(EntityFallingBlock fallingEntity) {
    }

    public int tickRate(World worldIn) {
        return 10;
    }

    public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
    }

    public void onBroken(World worldIn, BlockPos pos) {
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
        return getDefaultState().withProperty(VARIANT, Type.values()[meta % Type.values().length]);
    }

    public void harvestBlock(
            World worldIn,
            EntityPlayer player,
            BlockPos pos,
            IBlockState state,
            @Nullable TileEntity te,
            ItemStack stack
    ) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(
                Enchantments.SILK_TOUCH,
                stack
        ) > 0) {
            java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
            ItemStack itemstack = this.getSilkTouchDrop(state);

            if (!itemstack.isEmpty()) {
                items.add(itemstack);
            }

            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack item : items) {
                spawnAsEntity(worldIn, pos, item);
            }
        } else {
            harvesters.set(player);
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            this.dropBlockAsItemWithChance(worldIn, pos, state, 1, i, player);
            harvesters.set(null);
        }
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, EntityPlayer player) {
        NonNullList<ItemStack> ret = NonNullList.create();
        getDrops(ret, world, pos, state, fortune, player);
        return ret;
    }


    public void dropBlockAsItemWithChance(
            World worldIn, BlockPos pos, IBlockState state, float chance, int fortune,
            EntityPlayer player
    ) {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            List<ItemStack> drops = getDrops(
                    worldIn,
                    pos,
                    state,
                    fortune,
                    player
            );
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(
                    drops,
                    worldIn,
                    pos,
                    state,
                    fortune,
                    chance,
                    false,
                    harvesters.get()
            );

            for (ItemStack drop : drops) {
                if (worldIn.rand.nextFloat() <= chance) {
                    spawnAsEntity(worldIn, pos, drop);
                }
            }
        }
    }

    @Override
    public ItemStack getPickBlock(
            final IBlockState state,
            final RayTraceResult target,
            final World world,
            final BlockPos pos,
            final EntityPlayer player
    ) {
        final int meta = getMetaFromState(state);
        final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
        if (vein.getHeavyOre() == null) {
            final ChanceOre block = vein.getOres().get(0);
            return new ItemStack(block.getBlock().getBlock(), 1, block.getMeta());
        } else {
            return new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta());
        }
    }

    public void getDrops(
            NonNullList<ItemStack> drops,
            IBlockAccess world,
            BlockPos pos,
            IBlockState state,
            int fortune,
            EntityPlayer player
    ) {
        if (player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemHammer) {
            final int meta = getMetaFromState(state);
            final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
            if (vein.getHeavyOre() == null) {
                final ChanceOre block = vein.getOres().get(0);
                drops.add(new ItemStack(block.getBlock().getBlock(), 1, block.getMeta()));
            } else {
                final IBaseRecipe recipe = Recipes.recipes.getRecipe("handlerho");
                final List<BaseMachineRecipe> recipe_list = Recipes.recipes.getRecipeList("handlerho");
                final MachineRecipe output = Recipes.recipes.getRecipeMachineRecipeOutput(
                        recipe,
                        recipe_list,
                        false,
                        Collections.singletonList(new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()))
                );
                if (output != null) {
                    final int[] col = new int[output.getRecipe().output.items.size()];
                    for (int i = 0; i < col.length; i++) {
                        col[i] = output.getRecipe().output.metadata.getInteger(("input" + i));
                        col[i] = Math.min(col[i], 95);
                    }
                    List<ItemStack> stacks = new ArrayList<>();
                    for (int i = 0; i < col.length; i++) {
                        final Random rand = player.getEntityWorld().rand;
                        if ((rand.nextInt(100) < col[i])){
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
                drops.add(new ItemStack(block.getBlock().getBlock(), 1, block.getMeta()));
            } else {
                drops.add(new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()));
            }
        }
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        final int meta = getMetaFromState(state);
        final VeinType vein = WorldBaseGen.veinTypes.get(16 + meta);
        if (vein.getHeavyOre() == null) {
            final ChanceOre block = vein.getOres().get(0);
            drops.add(new ItemStack(block.getBlock().getBlock(), 1, block.getMeta()));
        } else {
            drops.add(new ItemStack(vein.getHeavyOre().getBlock(), 1, vein.getMeta()));
        }
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
        return 0;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
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
        setRegistryName("deposits1");
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
