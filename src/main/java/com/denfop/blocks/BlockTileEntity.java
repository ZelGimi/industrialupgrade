package com.denfop.blocks;

import appeng.block.networking.BlockCableBus;
import appeng.util.Platform;
import com.denfop.IUItem;
import com.denfop.api.item.IItemIgnoringNull;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.tile.IWrenchable;
import com.denfop.blocks.state.TileEntityBlockStateContainer;
import com.denfop.items.block.ItemBlockTileEntity;
import com.denfop.network.packet.PacketLandEffect;
import com.denfop.network.packet.PacketRunParticles;
import com.denfop.render.base.ISpecialParticleModel;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ModUtils;
import com.denfop.utils.ParticleBaseBlockDust;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class BlockTileEntity extends BlockBase implements IWrenchable, ITileEntityProvider {

    public static final Map<BlockPos, TileEntityBlock> teBlockDrop = new HashMap<>();
    public static final IProperty<EnumFacing> facingProperty = PropertyDirection.create("facing");
    public static TypeProperty currentTypeProperty;
    public final ItemBlockTileEntity item;
    public final TileBlockCreator.InfoAboutTile<?> teInfo;
    private final CreativeTabs tab;
    public final TypeProperty typeProperty = this.getTypeProperty();
    public BlockTileEntity(String name, final ResourceLocation identifier, final TileBlockCreator.InfoAboutTile<?> value) {
        super(null, value.getDefaultMaterial());
        this.item = new ItemBlockTileEntity(this, identifier);
        this.register(name, identifier, item);
        this.setDefaultState(this.blockState
                .getBaseState()
                .withProperty(this.typeProperty, TypeProperty.invalid)
                .withProperty(facingProperty, EnumFacing.DOWN));
        this.teInfo = value;
        this.tab = value.getTab();
        this.setSoundType(value.getDefaultMaterial() == Material.CLOTH ? SoundType.CLOTH : SoundType.STONE);

    }

    static BlockTileEntity create(String name, ResourceLocation identifier, final TileBlockCreator.InfoAboutTile<?> value) {
        currentTypeProperty = new TypeProperty(identifier, value);
        BlockTileEntity ret = new BlockTileEntity(name, identifier, value);
        currentTypeProperty = null;
        return ret;
    }

    public static EnumFacing getItemFacing(IMultiTileBlock teBlock) {
        Set<EnumFacing> supported = teBlock.getSupportedFacings();
        if (supported.contains(EnumFacing.NORTH)) {
            return EnumFacing.NORTH;
        } else {
            return !supported.isEmpty() ? supported.iterator().next() : EnumFacing.DOWN;
        }
    }

    private static TileEntityBlock getTe(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityBlock ? (TileEntityBlock) te : null;
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        StateMapperIU stateInvalid = new StateMapperIU(ModUtils.getName(IUItem.invalid));
        final ModelResourceLocation invalidLocation = stateInvalid.getModelResourceLocation(
                this.blockState
                        .getBaseState()
                        .withProperty(this.typeProperty, TypeProperty.invalid)
                        .withProperty(facingProperty, EnumFacing.NORTH)
        );

        ModelLoader.setCustomStateMapper(this, block -> block.getBlockState().getValidStates().stream()
                .collect(Collectors.toMap(
                        state -> state,
                        state -> {
                            State stateProperty = state.getValue(this.typeProperty);
                            EnumFacing facing = state.getValue(BlockTileEntity.facingProperty);
                            StateMapperIU stateMapper = new StateMapperIU(stateProperty.teBlock.getIdentifier());
                            if (!stateProperty.teBlock.getSupportedFacings().contains(facing) &&
                                    (facing != EnumFacing.DOWN || !stateProperty.teBlock.getSupportedFacings().isEmpty())) {
                                return invalidLocation;
                            } else {
                                return stateMapper.getModelResourceLocation(state);
                            }
                        }
                )));

        ModelLoader.setCustomMeshDefinition(this.item, stack -> {
            IMultiTileBlock teBlock = teInfo.getIdMap().isEmpty() ? null : teInfo.getIdMap().get(stack.getItemDamage());
            if (teBlock == null && !(teInfo.getListBlock().get(0) instanceof IItemIgnoringNull)) {
                return invalidLocation;
            } else if (teInfo.getListBlock().get(0) instanceof IItemIgnoringNull) {
                teBlock = teInfo.getListBlock().get(0);
                StateMapperIU stateMapper = new StateMapperIU(teBlock.getIdentifier());

                IBlockState state = BlockTileEntity.this.getDefaultState().withProperty(
                        BlockTileEntity.this.typeProperty,
                        BlockTileEntity.this.typeProperty.getState(teBlock)
                );
                return stateMapper.getModelResourceLocation(state);

            } else if (teBlock instanceof IMultiBlockItem && ((IMultiBlockItem) teBlock).hasUniqueRender(stack)) {
                ModelResourceLocation location = ((IMultiBlockItem) teBlock).getModelLocation(stack);
                return location == null ? invalidLocation : location;
            } else {
                try {
                    IBlockState state = BlockTileEntity.this.getDefaultState().withProperty(
                            BlockTileEntity.this.typeProperty,
                            BlockTileEntity.this.typeProperty.getState(teBlock)
                    );
                    StateMapperIU stateMapper = new StateMapperIU(teBlock.getIdentifier());

                    if (state != null) {
                        return stateMapper.getModelResourceLocation(state);
                    } else {
                        stateMapper = new StateMapperIU( ModUtils.getName(IUItem.invalid));
                        return stateMapper.getModelResourceLocation(
                                this.blockState
                                        .getBaseState()
                                        .withProperty(this.typeProperty, TypeProperty.invalid)
                        );
                    }
                }catch (Exception e){
                    final StateMapperIU stateMapper = new StateMapperIU(ModUtils.getName(IUItem.invalid));
                    return stateMapper.getModelResourceLocation(
                            this.blockState
                                    .getBaseState()
                                    .withProperty(this.typeProperty, TypeProperty.invalid)
                    );
                }
            }
        });

        boolean checkSpecialModels = teInfo.hasSpecialModels();

        for (TypeProperty.StatesBlocks statesBlocks : this.typeProperty.getAllStates()) {
            if (statesBlocks.hasItem()) {
                ModelResourceLocation model = checkSpecialModels ? this.getSpecialModel(statesBlocks) : null;
                if (model == null) {
                    IBlockState state = BlockTileEntity.this.getDefaultState()
                            .withProperty(this.typeProperty, statesBlocks.statesBlocks.get(0));
                    StateMapperIU stateMapper = new StateMapperIU(statesBlocks.getIdentifier());
                    model = stateMapper.getModelResourceLocation(state);
                }
                ModelBakery.registerItemVariants(this.item, model);
            }
        }


    }

    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return (te == null ? state : te.getExtendedState((TileEntityBlockStateContainer.PropertiesStateInstance)state));
    }
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getSpecialModel(TypeProperty.StatesBlocks blockTextures) {
        assert blockTextures.getBlock() instanceof IMultiBlockItem;

        IMultiBlockItem block = (IMultiBlockItem) blockTextures.getBlock();
        ItemStack stack = new ItemStack(this.item, 1, blockTextures.getBlock().getId());
        return block.hasUniqueRender(stack) ? block.getModelLocation(stack) : null;
    }

    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID;
    }

    public boolean hasTileEntity() {
        return true;
    }

    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    public BlockStateContainer createBlockState() {
        return new TileEntityBlockStateContainer(this, this.getTypeProperty(), facingProperty);
    }

    public TileEntity createTileEntity(World world, IBlockState state) {
        return (this).createNewTileEntity(world, getMetaFromState1(state));

    }

    public TileEntity createNewTileEntity(final World world, final int i) {
        if (i == -1) {
            return null;
        }
        IMultiTileBlock teBlock = this.item.getTeBlock(this.getItemStack(i));
        Class<? extends TileEntityBlock> teClass = teBlock.getTeClass();
        return TileEntityBlock.instantiate(teClass);
    }

    public int getMetaFromState(IBlockState state) {
        final State value = state.getValue(this.getTypeProperty());
        return value.teBlock.getId() == -1 ? 0 : value.teBlock.getId() >= 16 ? 15 : value.teBlock.getId();
    }

    public int getMetaFromState1(IBlockState state) {
        final State value = state.getValue(this.getTypeProperty());
        return value.teBlock.getId();
    }

    public @NotNull IBlockState getStateFromMeta(int meta) {
        final Map<ImmutableMap<IProperty<?>, Comparable<?>>, IBlockState> property =
                ((TileEntityBlockStateContainer.PropertiesStateInstance) this
                        .getDefaultState()).getMapProperties();
        for (IBlockState state : property.values()) {
            final State value = state.getValue(this.getTypeProperty());
            if (value.state.equals("active")) {
                continue;
            }
            if (value.teBlock.getId() == -1 && meta == 0) {
                return state;
            }
            if (value.teBlock.getId() == meta) {
                return state;
            }
        }

        return this.getDefaultState();
    }

    public @NotNull IBlockState getStateFromMeta1(ItemStack stack, final EnumFacing facing) {
        IMultiTileBlock teBlock = this.item.getTeBlock(stack);
        if (facing == null) {
            return this.getState(teBlock);
        } else {
            return this.getState(teBlock, facing);
        }
    }

    public @NotNull IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? state : te.getBlockState();
    }

    public String getUnlocalizedName() {
        return this.item.identifier.getResourceDomain() + '.' + this.item.identifier.getResourcePath();
    }

    public void getSubBlocks(CreativeTabs tabs, NonNullList<ItemStack> list) {
        if (tabs == tab || tabs == CreativeTabs.SEARCH) {

            for (final IMultiTileBlock type : this.teInfo.getListBlock()) {
                if (type.hasItem()) {
                    list.add(this.getItemStack(type));
                }
                if (type.hasOtherVersion()) {
                    list.addAll(type.getOtherVersion(this.getItemStack(type)));
                }
            }
        }

    }

    public Set<IMultiTileBlock> getAllTypes() {
        return Collections.unmodifiableSet(teInfo.getTeBlocks());
    }

    public @NotNull ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? ModUtils.emptyStack : te.getItem(null, null);
    }

    public @NotNull ItemStack getPickBlock(
            IBlockState state,
            RayTraceResult target,
            World world,
            BlockPos pos,
            EntityPlayer player
    ) {
        TileEntityBlock te = getTe(world, pos);

        return te == null ? ModUtils.emptyStack : te.getPickBlock(player, target);
    }

    public IBlockState getState(IMultiTileBlock variant) {

        Set<EnumFacing> supportedFacings = variant.getSupportedFacings();
        EnumFacing facing;
        if (supportedFacings.isEmpty()) {
            facing = EnumFacing.DOWN;
        } else if (supportedFacings.contains(EnumFacing.NORTH)) {
            facing = EnumFacing.NORTH;
        } else {
            facing = supportedFacings.iterator().next();
        }

        return this
                .getDefaultState()
                .withProperty(this.typeProperty, this.typeProperty.getState(variant))
                .withProperty(facingProperty, facing);

    }

    public IBlockState getState(IMultiTileBlock variant, EnumFacing facing) {
        return this
                .getDefaultState()
                .withProperty(this.typeProperty, this.typeProperty.getState(variant))
                .withProperty(facingProperty, facing);
    }

    public ItemStack getItemStack(IMultiTileBlock type) {
        return new ItemStack(this.item, 1, type.getId());
    }

    public ItemStack getItemStack(int type) {
        return new ItemStack(this.item, 1, type);
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack stack) {
        if (ModUtils.isEmpty(stack)) {
            return true;
        } else {
            return stack.getItem() == this.item;
        }
    }

    public boolean addLandingEffects(
            IBlockState state,
            WorldServer world,
            BlockPos pos,
            IBlockState state2,
            EntityLivingBase entity,
            int numberOfParticles
    ) {
        new PacketLandEffect(world, pos, entity.posX, entity.posY, entity.posZ, numberOfParticles);
        return true;
    }

    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        new PacketRunParticles(world, pos, entity);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(final World world, final BlockPos pos, final ParticleManager manager) {

        final TileEntityBlock te = getTe(world, pos);
        if (pos != null && te.hasSpecialModel()) {
            IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(te.getBlockState());
            final IBlockState state = te.getBlockState().getBlock().getExtendedState(te.getBlockState(), world, pos);
            int i = 4;

            for (int j = 0; j < 4; ++j)
            {
                for (int k = 0; k < 4; ++k)
                {
                    for (int l = 0; l < 4; ++l)
                    {
                        double d0 = ((double)j + 0.5D) / 4.0D;
                        double d1 = ((double)k + 0.5D) / 4.0D;
                        double d2 = ((double)l + 0.5D) / 4.0D;
                        ParticleDigging particle = new ParticleBaseBlockDust(world, pos.getX() + d0, (double)pos.getY() + d1,
                                (double)pos.getZ() + d2, 0, 0, 0, state);
                        ((ISpecialParticleModel)model).enhanceParticle(particle, (TileEntityBlockStateContainer.PropertiesStateInstance) state);
                        particle.init();
                        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    }
                }
            }
            return true;
        }else{
            return false;
        }

    }

    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {

        final BlockPos pos = target.getBlockPos();
        final TileEntityBlock te = getTe(world, pos);
        if (te == null) {
            return true;
        }
        double offset = 0.1;
        AxisAlignedBB aabb = te.getVisualBoundingBox();
        double x = (double) pos.getX() + world.rand.nextDouble() * (aabb.maxX - aabb.minX - offset * 2.0) + offset + aabb.minX;
        double y = (double) pos.getY() + world.rand.nextDouble() * (aabb.maxY - aabb.minY - offset * 2.0) + offset + aabb.minY;
        double z = (double) pos.getZ() + world.rand.nextDouble() * (aabb.maxZ - aabb.minZ - offset * 2.0) + offset + aabb.minZ;
        switch (target.sideHit) {
            case DOWN:
                y = (double) pos.getY() + aabb.minY - offset;
                break;
            case UP:
                y = (double) pos.getY() + aabb.maxY + offset;
                break;
            case NORTH:
                z = (double) pos.getZ() + aabb.minZ - offset;
                break;
            case SOUTH:
                z = (double) pos.getZ() + aabb.maxZ + offset;
                break;
            case WEST:
                x = (double) pos.getX() + aabb.minX - offset;
                break;
            case EAST:
                x = (double) pos.getX() + aabb.maxX + offset;
                break;
        }

        ParticleDigging particle = new ParticleBaseBlockDust(world, x, y, z, 0.0, 0.0, 0.0, te.getBlockState());
        particle.setBlockPos(pos);
        particle.multiplyVelocity(0.2F);
        particle.multipleParticleScaleBy(0.6F);
        if (pos != null && te.hasSpecialModel()) {
            IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(te.getBlockState());
            if (model instanceof ISpecialParticleModel) {

                state = te.getBlockState().getBlock().getExtendedState(te.getBlockState(), world, pos);


                ((ISpecialParticleModel)model).enhanceParticle(particle, (TileEntityBlockStateContainer.PropertiesStateInstance) state);
            }
        }
        particle.init();

        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        return true;
    }

    public Material getMaterial(IBlockState state) {
        return blockMaterial;
    }

    public boolean causesSuffocation(IBlockState state) {
        return this.getMaterial(state).blocksMovement() && this.getDefaultState().isFullCube();
    }

    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return !this.getMaterial(world.getBlockState(pos)).blocksMovement();
    }

    public boolean canSpawnInBlock() {
        return super.canSpawnInBlock();
    }

    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return this.getMaterial(state).getMobilityFlag();
    }

    public boolean isTranslucent(IBlockState state) {
        return !this.getMaterial(state).blocksLight();
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return this.getMaterial(state).getMaterialMapColor();
    }

    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            te.onPlaced(stack, placer, EnumFacing.UP);
        }
    }

    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        return super.collisionRayTrace(state, world, pos, start, end);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.getBoundingBox(state, world, pos) : te.getVisualBoundingBox();
    }

    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.getSelectedBoundingBox(state, world, pos) : te.getOutlineBoundingBox().offset(pos);
    }

    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.getCollisionBoundingBox(state, world, pos) : te.getPhysicsBoundingBox();
    }

    public void addCollisionBoxToList(
            IBlockState state,
            World world,
            BlockPos pos,
            AxisAlignedBB mask,
            List<AxisAlignedBB> list,
            Entity collidingEntity,
            boolean isActualState
    ) {
        TileEntityBlock te = getTe(world, pos);
        if (te == null) {
            super.addCollisionBoxToList(state, world, pos, mask, list, collidingEntity, isActualState);
        } else {
            te.addCollisionBoxesToList(mask, list, collidingEntity);
        }

    }

    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            te.onEntityCollision(entity);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.shouldSideBeRendered(state, world, pos, side) : te.shouldSideBeRendered(side, pos.offset(side));
    }

    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.doesSideBlockRendering(face);
    }

    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.isNormalCube();
    }

    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.isSideSolid(side);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? super.getBlockFaceShape(world, state, pos, face) : te.getFaceShape(face);
    }

    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    public boolean onBlockActivated(
            World world,
            BlockPos pos,
            IBlockState state,
            EntityPlayer player,
            EnumHand hand,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        if (player.isSneaking()) {
            TileEntityBlock te = getTe(world, pos);
            return te != null && te.onSneakingActivated(player, hand, side, hitX, hitY, hitZ);
        } else {
            TileEntityBlock te = getTe(world, pos);
            return te != null && te.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            te.onClicked(player);
        }
    }

    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            te.onNeighborChange(neighborBlock, neighborPos);
        }
    }

    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? 0 : te.getWeakPower(side);
    }

    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.canConnectRedstone(side);
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? 0 : te.getComparatorInputOverride();
    }

    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(world, pos, explosion);
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            te.onBlockBreak(false);
        }

        super.breakBlock(world, pos, state);
    }

    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            if (!te.onRemovedByPlayer(player, willHarvest)) {
                return false;
            }
            teBlockDrop.put(pos, te);
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        float ret = super.getPlayerRelativeBlockHardness(state, player, world, pos);
        if (!player.canHarvestBlock(state)) {
            TileEntityBlock te = getTe(world, pos);
            if (te != null && te.teBlock.getHarvestTool() == MultiTileBlock.HarvestTool.None) {
                ret *= 3.3333333F;
            }
        }

        return ret;
    }

    @Override
    public void onBlockHarvested(
            final World p_176208_1_,
            final BlockPos p_176208_2_,
            final IBlockState p_176208_3_,
            final EntityPlayer p_176208_4_
    ) {
        p_176208_1_.playSound(null, p_176208_2_,
                this.getMaterial(p_176208_3_) == MultiTileBlock.MACHINE ? SoundEvents.BLOCK_STONE_BREAK :
                        SoundEvents.BLOCK_CLOTH_BREAK,
                SoundCategory.BLOCKS, 1F, 1
        );

        super.onBlockHarvested(p_176208_1_, p_176208_2_, p_176208_3_, p_176208_4_);
    }

    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        boolean ret = super.canHarvestBlock(world, pos, player);
        if (ret) {
            return ret;
        } else {
            TileEntityBlock te = getTe(world, pos);
            if (te == null) {
                return false;
            } else {
                switch (te.teBlock.getHarvestTool()) {
                    case None:
                        return true;
                    case Wrench:
                        ItemStack stack = player.getHeldItemMainhand();
                        if (!stack.isEmpty()) {
                            String tool = MultiTileBlock.HarvestTool.Pickaxe.toolClass;
                            return stack.getItem().getHarvestLevel(
                                    stack,
                                    tool,
                                    player,
                                    world.getBlockState(pos)
                            ) >= MultiTileBlock.HarvestTool.Pickaxe.level;
                        }
                    default:
                        return false;
                }
            }
        }
    }

    public String getHarvestTool(IBlockState state) {
        return state.getBlock() != this ? null : state.getValue(this.typeProperty).teBlock.getHarvestTool().toolClass;
    }

    public int getHarvestLevel(IBlockState state) {
        return state.getBlock() != this ? 0 : state.getValue(this.typeProperty).teBlock.getHarvestTool().level;
    }

    public void getDrops(NonNullList<ItemStack> list, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        list.addAll(this.getDrops(world, pos, state, fortune));
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityBlock te = getTe(world, pos);
        if (te == null) {
            te = teBlockDrop.get(pos);
            if (te == null) {
                return new ArrayList<>();
            } else {
                List<ItemStack> ret = new ArrayList<>();
                boolean wasWrench = false;
                EntityPlayer player = this.harvesters.get();
                if (player != null) {
                    ItemStack stack = player.getHeldItemMainhand();
                    if (!stack.isEmpty()) {
                        String tool = MultiTileBlock.HarvestTool.Wrench.toolClass;
                        wasWrench = stack.getItem().getHarvestLevel(
                                stack,
                                tool,
                                player,
                                state
                        ) >= MultiTileBlock.HarvestTool.Wrench.level;
                    }
                }

                ret.addAll(te.getSelfDrops(fortune, wasWrench));
                ret.addAll(te.getAuxDrops(fortune));
                teBlockDrop.remove(pos);
                return ret;
            }
        }
        List<ItemStack> ret = new ArrayList();
        boolean wasWrench = false;
        EntityPlayer player = this.harvesters.get();
        if (player != null) {
            ItemStack stack = player.getHeldItemMainhand();
            if (!stack.isEmpty()) {
                String tool = MultiTileBlock.HarvestTool.Wrench.toolClass;
                wasWrench = stack.getItem().getHarvestLevel(
                        stack,
                        tool,
                        player,
                        state
                ) >= MultiTileBlock.HarvestTool.Wrench.level;
            }
        }

        ret.addAll(te.getSelfDrops(fortune, wasWrench));
        ret.addAll(te.getAuxDrops(fortune));
        return ret;
    }

    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? 5.0F : te.getHardness();
    }

    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return 5;
    }

    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        TileEntityBlock te = getTe(world, pos);
        return te == null || te.canEntityDestroy(entity);
    }

    public EnumFacing getFacing(World world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        return te == null ? EnumFacing.DOWN : te.getFacing();
    }

    public boolean canSetFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.canSetFacingWrench(newDirection, player);
    }

    public boolean setFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.setFacingWrench(newDirection, player);
    }

    public boolean wrenchCanRemove(World world, BlockPos pos, EntityPlayer player) {
        TileEntityBlock te = getTe(world, pos);
        return te != null && te.wrenchCanRemove(player);
    }

    public List<ItemStack> getWrenchDrops(
            World world,
            BlockPos pos,
            IBlockState state,
            TileEntity te,
            EntityPlayer player,
            int fortune
    ) {
        final List<ItemStack> list = ((TileEntityBlock) te).getWrenchDrops(
                player,
                fortune
        );
        return list;
    }

    @Override
    public void wrenchBreak(World world, BlockPos pos) {
        TileEntityBlock tileEntityBlock = (TileEntityBlock) world.getTileEntity(pos);
        if (tileEntityBlock != null) {
            tileEntityBlock.wrenchBreak();
        }
    }

    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        TileEntityBlock te = getTe(world, pos);
        return this.getSoundType();
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        TileEntityBlock te = getTe(world, pos);
        if (te != null) {
            EnumFacing target = te.getFacing().rotateAround(axis.getAxis());
            if (te.getSupportedFacings().contains(target) && te.getFacing() != target) {
                te.setFacing(target);
                return true;
            }
        }

        return false;
    }

    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        TileEntityBlock te = getTe(world, pos);
        if (te == null) {
            return null;
        } else {
            Set<EnumFacing> facings = te.getSupportedFacings();
            return !facings.isEmpty() ? facings.toArray(new EnumFacing[0]) : null;
        }
    }

    public ItemBlockTileEntity getItem() {
        return this.item;
    }

    public TypeProperty getTypeProperty() {
        return this.typeProperty != null ? this.typeProperty : currentTypeProperty;
    }




}
