package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.bee.BeeAI;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.api.bee.IBee;
import com.denfop.api.bee.genetics.EnumGenetic;
import com.denfop.api.bee.genetics.GeneticTraits;
import com.denfop.api.bee.genetics.Genome;
import com.denfop.api.item.IDamageItem;
import com.denfop.api.pollution.ChunkLevel;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockApiary;
import com.denfop.container.ContainerApiary;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.gui.GuiApiary;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemFluidCell;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.items.energy.ItemNet;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.tiles.mechanism.TileEntityApothecaryBee;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.denfop.api.bee.genetics.GeneticsManager.enumGeneticListMap;
import static com.denfop.api.bee.genetics.GeneticsManager.geneticTraitsMap;


public class TileEntityApiary extends TileEntityInventory implements IApiaryTile {

    public static BeeAI beeAI = BeeAI.beeAI;
    static final int percentAttackBee = 20;
    static final int percentDoctorBee = 20;
    static final int percentWorkersBee = 45;

    static final int percentBuildersBee = 15;
    public final InvSlotOutput invSlotProduct;
    public final InvSlot frameSlot;
    public final InvSlotOutput invSlotFood;
    public final InvSlotOutput invSlotJelly;
    public final InvSlot foodCellSlot;
    public final InvSlot jellyCellSlot;
    double[] massiveNeeds = new double[5];
    List<Bee> apairyBeeList = new LinkedList<>();

    IBee queen;

    List<Bee> attackBeeList = new LinkedList<>();
    List<Bee> doctorBeeList = new LinkedList<>();
    List<Bee> workersBeeList = new LinkedList<>();
    List<Bee> buildersBeeList = new LinkedList<>();
    public List<Bee> birthBeeList = new LinkedList<>();
    List<Bee> illBeeList = new LinkedList<>();
    Map<EntityPlayer, Double> enemy;
    public double royalJelly = 0;
    public double food = 0;
    private AxisAlignedBB axisAlignedBB;
    private Vec3d center;
    private double maxDistance;
    public long id;
    private byte tickDrainFood = 0;
    private byte tickDrainJelly = 0;
    Map<Long, EnumStatus> statusMap = new HashMap<>();
    Map<EntityPlayer, Double> entityWeightMap = new HashMap<>();
    Set<ChunkPos> chunkPositions = new HashSet<>();
    Map<ChunkPos, List<TileEntityCrop>> chunkPosListMap = new HashMap<>();
    Map<ChunkPos, List<TileEntityApiary>> chunkPosListMap1 = new HashMap<>();
    List<TileEntityCrop> crops = new ArrayList<>();
    List<TileEntityCrop> passedCrops = new ArrayList<>();

    List<TileEntityApiary> apiaries = new ArrayList<>();
    List<TileEntityApiary> passedApiaries = new ArrayList<>();
    public int harvest = 0;
    public ChunkPos chunkPos;
    public ChunkLevel chunkLevel;
    public Biome biome;
    public Chunk chunk;
    public Radiation radLevel;
    public boolean rain;
    public boolean thundering;
    public int task;
    public int workers;
    public int builders;
    public int attacks;
    public int bees;
    public int doctors;
    public int death;
    public byte deathTask;
    public byte illTask;
    public int birth;
    public int ill;
    double slowAging = 1;
    double producing = 1;
    double speedCrop = 1;
    double speedBirthRate = 1;
    int chanceCrossing = 0;
    double chanceHealing = 1;
    int generation = 0;

    private Map<TileEntityApiary, Double> bees_nearby;
    public List<EnumProblem> problemList = new ArrayList<>();
    public boolean work;
    private double coef;
    Genome genome;
    private ItemStack stack;


    private int weatherGenome = 0;
    private double pestGenome = 1;
    private double birthRateGenome = 1;
    private double radiusGenome = 1;
    private double populationGenome = 1;
    private double foodGenome = 1;
    private double jellyGenome = 1;
    private double productGenome = 1;
    private double hardeningGenome = 1;
    private double swarmGenome = 1;
    private double mortalityGenome = 1;
    private boolean sunGenome = false;
    private boolean nightGenome = false;
    public short maxFood = 1000;
    public short maxJelly = 200;
    public short maxDefaultFood = 1000;
    public short maxDefaultJelly = 200;
    private int genomeResistance = 0;
    private int genomeAdaptive = 0;
    private LevelPollution airPollution = LevelPollution.LOW;
    private LevelPollution soilPollution = LevelPollution.LOW;
    private EnumLevelRadiation radiationPollution = EnumLevelRadiation.LOW;

    public TileEntityApiary() {
        this.invSlotProduct = new InvSlotOutput(this, 7);
        this.invSlotFood = new InvSlotOutput(this, 1);
        this.invSlotJelly = new InvSlotOutput(this, 1);
        this.foodCellSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemFluidCell;
            }
        };
        this.jellyCellSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ItemFluidCell;
            }
        };
        this.frameSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 4) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof IFrameItem;
            }

            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                update();
            }


            public void update() {
                reset();
                for (ItemStack stack : contents) {
                    if (!stack.isEmpty()) {
                        IFrameItem frameItem = (IFrameItem) stack.getItem();
                        FrameAttributeLevel attributeLevel = frameItem.getAttribute(stack.getItemDamage());
                        switch (attributeLevel.getAttribute()) {
                            case STORAGE_FOOD:
                                maxFood = (short) (maxDefaultFood * attributeLevel.getValue());
                            case STORAGE_JELLY:
                                maxJelly = (short) (maxDefaultJelly * attributeLevel.getValue());
                            case PRODUCING:
                                producing *= attributeLevel.getValue();
                                break;
                            case SLOW_AGING:
                                slowAging *= attributeLevel.getValue();
                                break;
                            case SPEED_CROP:
                                speedCrop *= attributeLevel.getValue();
                                break;
                            case CHANCE_HEALING:
                                chanceHealing *= attributeLevel.getValue();
                                break;
                            case SPEED_BIRTH_RATE:
                                speedBirthRate *= attributeLevel.getValue();
                                break;
                            case CHANCE_CROSSING:
                                chanceCrossing = Math.max(chanceCrossing, (int) attributeLevel.getValue());
                        }
                    }
                }
                if (chanceHealing == 1) {
                    chanceCrossing = 0;
                }
                if (speedBirthRate == 1) {
                    speedBirthRate = 0;
                }
                if (speedCrop == 1) {
                    speedCrop = 0;
                }
                if (slowAging == 1) {
                    slowAging = 0;
                }
                if (producing == 1) {
                    producing = 0;
                }
            }

            public void reset() {
                slowAging = 1;
                producing = 1;
                maxFood = maxDefaultFood;
                maxJelly = maxDefaultJelly;
                speedCrop = 1;
                speedBirthRate = 1;
                chanceCrossing = 0;
                chanceHealing = 1;
            }
        };
    }

    public boolean isWork() {
        return work;
    }

    public IBee getQueen() {
        return queen;
    }

    public int getChanceCrossing() {
        return queen != null ? queen.getChance() + chanceCrossing : 0;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.apiary;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockApiary.apiary;
    }

    public Map<EntityPlayer, Double> getNearbyEntitiesWithWeight() {
        AxisAlignedBB axisAlignedBB = this.axisAlignedBB;

        List<EntityPlayer> players = getEntitiesWithinAABB(axisAlignedBB);
        entityWeightMap.clear();

        for (EntityPlayer player : players) {
            if (getComponentPrivate().getPlayers().contains(player.getName()) || player.isCreative()) {
                continue;
            }
            double distance = center.distanceTo(player.getPositionVector());
            double weight = calculateWeight(distance, maxDistance);
            entityWeightMap.put(player, weight);
        }


        return entityWeightMap;
    }
    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    public void set() {
        if (genome == null) {
            return;
        }

        if (genome.hasGenome(EnumGenetic.WEATHER)) {
            this.weatherGenome = genome.getLevelGenome(EnumGenetic.WEATHER, Integer.class);
        }
        if (genome.hasGenome(EnumGenetic.PEST)) {
            this.pestGenome = genome.getLevelGenome(EnumGenetic.PEST, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.BIRTH)) {
            this.birthRateGenome = genome.getLevelGenome(EnumGenetic.BIRTH, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.RADIUS)) {
            this.radiusGenome = genome.getLevelGenome(EnumGenetic.RADIUS, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.POPULATION)) {
            this.populationGenome = genome.getLevelGenome(EnumGenetic.POPULATION, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.FOOD)) {
            this.foodGenome = genome.getLevelGenome(EnumGenetic.FOOD, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.JELLY)) {
            this.jellyGenome = genome.getLevelGenome(EnumGenetic.JELLY, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.PRODUCT)) {
            this.productGenome = genome.getLevelGenome(EnumGenetic.PRODUCT, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.HARDENING)) {
            this.hardeningGenome = genome.getLevelGenome(EnumGenetic.HARDENING, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.SWARM)) {
            this.swarmGenome = genome.getLevelGenome(EnumGenetic.SWARM, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.MORTALITY_RATE)) {
            this.mortalityGenome = genome.getLevelGenome(EnumGenetic.MORTALITY_RATE, Double.class);
        }


        if (genome.hasGenome(EnumGenetic.SUN)) {
            this.sunGenome = genome.getLevelGenome(EnumGenetic.SUN, Boolean.class);
        }
        if (genome.hasGenome(EnumGenetic.NIGHT)) {
            this.nightGenome = genome.getLevelGenome(EnumGenetic.NIGHT, Boolean.class);
        }


        if (genome.hasGenome(EnumGenetic.AIR)) {
            this.airPollution = genome.getLevelGenome(EnumGenetic.AIR, LevelPollution.class);
        }
        if (genome.hasGenome(EnumGenetic.SOIL)) {
            this.soilPollution = genome.getLevelGenome(EnumGenetic.SOIL, LevelPollution.class);
        }
        if (genome.hasGenome(EnumGenetic.RADIATION)) {
            this.radiationPollution = genome.getLevelGenome(EnumGenetic.RADIATION, EnumLevelRadiation.class);
        }

        if (genome.hasGenome(EnumGenetic.GENOME_RESISTANCE)) {
            this.genomeResistance = genome.getLevelGenome(EnumGenetic.GENOME_RESISTANCE, Integer.class);
        }
        if (genome.hasGenome(EnumGenetic.GENOME_ADAPTIVE)) {
            this.genomeAdaptive = genome.getLevelGenome(EnumGenetic.GENOME_ADAPTIVE, Integer.class);
        }
    }


    public void reset() {
        this.weatherGenome = 0;
        this.pestGenome = 1;
        this.swarmGenome = 1;
        this.mortalityGenome = 1;
        this.birthRateGenome = 1;
        this.radiusGenome = 1;
        this.populationGenome = 1;
        this.foodGenome = 1;
        this.jellyGenome = 1;
        this.productGenome = 1;
        this.hardeningGenome = 1;
        this.sunGenome = false;
        this.nightGenome = false;
        this.genomeResistance = 0;
        this.genomeAdaptive = 0;
        this.airPollution = LevelPollution.LOW;
        this.soilPollution = LevelPollution.LOW;
        this.radiationPollution = EnumLevelRadiation.LOW;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        long packedData = customPacketBuffer.readLong();

        this.task = (int) (packedData & 0x7F);
        this.workers = (int) ((packedData >> 7) & 0x7F);
        this.builders = (int) ((packedData >> 14) & 0x7F);
        this.attacks = (int) ((packedData >> 21) & 0x7F);
        this.bees = (int) ((packedData >> 28) & 0x7F);
        this.doctors = (int) ((packedData >> 35) & 0x7F);
        this.death = (int) ((packedData >> 42) & 0x7F);
        this.birth = (int) ((packedData >> 49) & 0x7F);
        this.ill = (int) ((packedData >> 56) & 0x7F);
        this.maxFood = customPacketBuffer.readShort();
        this.maxJelly = customPacketBuffer.readShort();
        this.food = customPacketBuffer.readDouble();
        this.royalJelly = customPacketBuffer.readDouble();
        this.deathTask = customPacketBuffer.readByte();
        this.illTask = customPacketBuffer.readByte();
        if (this.queen == null) {
            this.queen = BeeNetwork.instance.getBee(customPacketBuffer.readInt());
        } else {
            customPacketBuffer.readInt();
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer buffer = super.writeContainerPacket();
        long packedData = ((long) this.task & 0x7F) |
                ((long) this.workers & 0x7F) << 7 |
                ((long) this.builders & 0x7F) << 14 |
                ((long) this.attacks & 0x7F) << 21 |
                ((long) this.bees & 0x7F) << 28 |
                ((long) this.doctors & 0x7F) << 35 |
                ((long) this.death & 0x7F) << 42 |
                ((long) this.birth & 0x7F) << 49 |
                ((long) this.ill & 0x7F) << 56;
        buffer.writeLong(packedData);
        buffer.writeShort(maxFood);
        buffer.writeShort(maxJelly);
        buffer.writeDouble(this.food);
        buffer.writeDouble(this.royalJelly);
        buffer.writeByte(this.deathTask);
        buffer.writeByte(this.illTask);
        if (queen != null) {
            buffer.writeInt(queen.getId());
        } else {
            buffer.writeInt(0);
        }
        return buffer;
    }

    @Override
    public void onUnloaded() {
        super.onUnloaded();
        if (!this.getWorld().isRemote) {
            BeeNetwork.instance.removeApiaryFromWorld(this);
        }
     /*   if (this.getWorld().isRemote) {
            GlobalRenderManager.removeRender(pos);
        }*/
    }

    List<Chunk> chunks = new ArrayList<>();

    public List<EntityPlayer> getEntitiesWithinAABB(AxisAlignedBB aabb) {
        List<EntityPlayer> list = Lists.newArrayList();
        WorldServer server = (WorldServer) world;
        for (EntityPlayer player : server.playerEntities) {
            if (aabb.contains(player.getPositionVector())) {
                list.add(player);
            }
        }
        return list;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
   /*     if (this.getWorld().isRemote) {
            this.render = createFunction(this);
             GlobalRenderManager.addRender(pos, render);
        }*/
        if (!this.getWorld().isRemote) {
            BeeNetwork.instance.addNewApiaryToWorld(this);
            this.frameSlot.update();
            if (this.queen != null) {
                this.genome = new Genome(this.stack);
                this.chunk = this.getWorld().getChunkFromBlockCoords(pos);
                this.biome = this.getWorld().getBiome(pos);
                reset();
                set();
                this.coef = this.queen.canWorkInBiome(biome) || genome.hasGenome(EnumGenetic.COEF_BIOME) ? 1 : 0.5;
                this.axisAlignedBB = new AxisAlignedBB(
                        pos.getX() + this.queen.getSizeTerritory().minX * radiusGenome,
                        pos.getY() + this.queen.getSizeTerritory().minY * radiusGenome,
                        pos.getZ() + this.queen.getSizeTerritory().minZ * radiusGenome,
                        pos.getX() + this.queen.getSizeTerritory().maxX * radiusGenome,
                        pos.getY() + this.queen.getSizeTerritory().maxY * radiusGenome,
                        pos.getZ() + this.queen.getSizeTerritory().maxZ * radiusGenome
                );
                this.center = new Vec3d(
                        (axisAlignedBB.minX + axisAlignedBB.maxX) / 2,
                        (axisAlignedBB.minY + axisAlignedBB.maxY) / 2,
                        (axisAlignedBB.minZ + axisAlignedBB.maxZ) / 2
                );
                final AxisAlignedBB aabb = axisAlignedBB;
                int j2 = MathHelper.floor((aabb.minX - 2) / 16.0D);
                int k2 = MathHelper.ceil((aabb.maxX + 2) / 16.0D);
                int l2 = MathHelper.floor((aabb.minZ - 2) / 16.0D);
                int i3 = MathHelper.ceil((aabb.maxZ + 2) / 16.0D);
                for (int j3 = j2; j3 < k2; ++j3) {
                    for (int k3 = l2; k3 < i3; ++k3) {
                        final Chunk chunk = world.getChunkFromChunkCoords(j3, k3);
                        if (!chunks.contains(chunk)) {
                            chunks.add(chunk);
                        }
                    }
                }
                chunkPositions.clear();
                this.maxDistance = center.distanceTo(new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));

                int minX = (int) Math.floor(axisAlignedBB.minX) >> 4;
                int maxX = (int) Math.floor(axisAlignedBB.maxX) >> 4;
                int minZ = (int) Math.floor(axisAlignedBB.minZ) >> 4;
                int maxZ = (int) Math.floor(axisAlignedBB.maxZ) >> 4;


                for (int chunkX = minX; chunkX <= maxX; chunkX++) {
                    for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
                        chunkPositions.add(new ChunkPos(chunkX, chunkZ));
                    }
                }
            }
            this.chunkPos = new ChunkPos(pos);
            Radiation radiation1 = RadiationSystem.rad_system.getMap().get(chunkPos);
            if (radiation1 == null) {
                radiation1 = new Radiation(chunkPos);
                RadiationSystem.rad_system.addRadiation(radiation1);
            }
            this.radLevel = radiation1;
            ChunkLevel chunkLevel = PollutionManager.pollutionManager.getChunkLevelSoil(chunkPos);
            if (chunkLevel == null) {
                chunkLevel = new ChunkLevel(chunkPos, LevelPollution.VERY_LOW, 0);
                PollutionManager.pollutionManager.addChunkLevelSoil(chunkLevel);
            }
            for (ChunkPos chunkPos : chunkPositions) {
                chunkPosListMap.put(chunkPos, CropNetwork.instance.getCropsFromChunk(world, chunkPos));
            }
            for (ChunkPos chunkPos : chunkPositions) {
                chunkPosListMap1.put(chunkPos, BeeNetwork.instance.getApiaryFromChunk(world, chunkPos));
            }
            this.chunkLevel = chunkLevel;

        }
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        if (queen == null){
            tooltip.add(Localization.translate("iu.use_bee"));
        }
    }
    private double calculateWeight(double distance, double maxDistance) {
        return Math.max(0.0, 1.0 - (distance / maxDistance));
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemJarBees && queen == null && !this.getWorld().isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            if (nbt.hasKey("swarm")) {
                this.stack = stack.copy();
                this.stack.setCount(1);
                int id = nbt.getInteger("bee_id");
                this.id = WorldBaseGen.random.nextLong();
                this.queen = BeeNetwork.instance.getBee(id).copy();
                this.genome = new Genome(this.stack);
                this.biome = this.getWorld().getBiome(pos);
                this.coef = this.queen.canWorkInBiome(biome) ? 1 : 0.5;
                reset();
                set();
                this.coef = genome.hasGenome(EnumGenetic.COEF_BIOME) ? 1 : coef;
                int totalBeeCount = nbt.getInteger("swarm");
                if (!nbt.hasKey("info")) {
                    int attackBeeCount = (int) (totalBeeCount * (percentAttackBee / 100.0));
                    int doctorBeeCount = (int) (totalBeeCount * (percentDoctorBee / 100.0));
                    int workersBeeCount = (int) (totalBeeCount * (percentWorkersBee / 100.0));
                    int buildersBeeCount = totalBeeCount - (attackBeeCount + doctorBeeCount + workersBeeCount);
                    for (int i = 0; i < attackBeeCount; i++) {
                        Bee bee = new Bee(EnumTypeBee.ATTACK, queen, EnumTypeLife.BEE, 25,
                                WorldBaseGen.random.nextInt(queen.getTickLifecycles()-queen.getTickBirthRate()) + queen.getTickBirthRate()
                        );
                        attackBeeList.add(bee);
                        apairyBeeList.add(bee);
                    }
                    for (int i = 0; i < doctorBeeCount; i++) {
                        Bee bee = new Bee(EnumTypeBee.DOCTOR, queen, EnumTypeLife.BEE, 25,
                                WorldBaseGen.random.nextInt(queen.getTickLifecycles()-queen.getTickBirthRate()) + queen.getTickBirthRate()
                        );
                        doctorBeeList.add(bee);
                        apairyBeeList.add(bee);
                    }
                    for (int i = 0; i < workersBeeCount; i++) {
                        Bee bee = new Bee(EnumTypeBee.WORKER, queen, EnumTypeLife.BEE, 25,
                                WorldBaseGen.random.nextInt(queen.getTickLifecycles()-queen.getTickBirthRate()) + queen.getTickBirthRate()
                        );
                        workersBeeList.add(bee);
                        apairyBeeList.add(bee);
                    }
                    for (int i = 0; i < buildersBeeCount; i++) {
                        Bee bee = new Bee(EnumTypeBee.BUILDER, queen, EnumTypeLife.BEE, 25,
                                WorldBaseGen.random.nextInt(queen.getTickLifecycles()-queen.getTickBirthRate()) + queen.getTickBirthRate()
                        );
                        buildersBeeList.add(bee);
                        apairyBeeList.add(bee);
                    }
                } else {
                    final NBTTagList tagList = nbt.getTagList("info", 10);
                    for (int i = 0; i < tagList.tagCount(); i++) {
                        NBTTagCompound beeInfo = tagList.getCompoundTagAt(i);
                        Bee bee = new Bee(beeInfo);
                        if (bee.isIll()) {
                            this.illBeeList.add(bee);
                        }
                        if (bee.isChild()) {
                            this.birthBeeList.add(bee);
                        }
                        switch (bee.getTypeBee()) {
                            case BUILDER:
                                this.buildersBeeList.add(bee);
                                break;
                            case DOCTOR:
                                this.doctorBeeList.add(bee);
                                break;
                            case WORKER:
                                this.workersBeeList.add(bee);
                                break;
                            case ATTACK:
                                this.attackBeeList.add(bee);
                                break;
                        }
                        this.apairyBeeList.add(bee);
                    }
                }
                this.axisAlignedBB = new AxisAlignedBB(
                        pos.getX() + this.queen.getSizeTerritory().minX * radiusGenome,
                        pos.getY() + this.queen.getSizeTerritory().minY * radiusGenome,
                        pos.getZ() + this.queen.getSizeTerritory().minZ * radiusGenome,
                        pos.getX() + this.queen.getSizeTerritory().maxX * radiusGenome,
                        pos.getY() + this.queen.getSizeTerritory().maxY * radiusGenome,
                        pos.getZ() + this.queen.getSizeTerritory().maxZ * radiusGenome
                );
                this.center = new Vec3d(
                        (axisAlignedBB.minX + axisAlignedBB.maxX) / 2,
                        (axisAlignedBB.minY + axisAlignedBB.maxY) / 2,
                        (axisAlignedBB.minZ + axisAlignedBB.maxZ) / 2
                );
                this.maxDistance = center.distanceTo(new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));

                int minX = (int) Math.floor(axisAlignedBB.minX) >> 4;
                int maxX = (int) Math.floor(axisAlignedBB.maxX) >> 4;
                int minZ = (int) Math.floor(axisAlignedBB.minZ) >> 4;
                int maxZ = (int) Math.floor(axisAlignedBB.maxZ) >> 4;

                chunkPositions.clear();
                for (int chunkX = minX; chunkX <= maxX; chunkX++) {
                    for (int chunkZ = minZ; chunkZ <= maxZ; chunkZ++) {
                        chunkPositions.add(new ChunkPos(chunkX, chunkZ));
                    }
                }
                chunkPosListMap.clear();
                chunkPosListMap1.clear();
                for (ChunkPos chunkPos : chunkPositions) {
                    chunkPosListMap.put(chunkPos, CropNetwork.instance.getCropsFromChunk(world, chunkPos));
                }
                for (ChunkPos chunkPos : chunkPositions) {
                    chunkPosListMap1.put(chunkPos, BeeNetwork.instance.getApiaryFromChunk(world, chunkPos));
                }


                stack.shrink(1);
            }
        } else if (queen != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemNet&& !this.getWorld().isRemote) {
            ItemStack stack = new ItemStack(IUItem.jarBees, 1);

            final NBTTagCompound nbt = ModUtils.nbt(stack);
            this.genome.writeNBT(nbt);
            nbt.setInteger("bee_id", queen.getId());
            nbt.setInteger("swarm", this.apairyBeeList.size());
            NBTTagList tagList = new NBTTagList();
            for (Bee bee : this.apairyBeeList) {
                tagList.appendTag(bee.writeToNBT());
            }
            nbt.setTag("info", tagList);
            player.addItemStackToInventory(stack);
            this.apairyBeeList.clear();
            this.queen = null;
            this.attackBeeList.clear();
            this.workersBeeList.clear();
            this.doctorBeeList.clear();
            this.buildersBeeList.clear();
            this.illBeeList.clear();
            birth = 0;
            death = 0;
            this.birthBeeList.clear();
        }
        if (this.queen != null) {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public ContainerApiary getGuiContainer(final EntityPlayer var1) {
        return new ContainerApiary(var1, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiApiary(getGuiContainer(var1));
    }

    public void feedBees(List<Bee> beeList) {
        double totalRequiredFood = 0.0;


        for (Bee bee : beeList) {
            totalRequiredFood += 25 - bee.getFood();
        }
        if (totalRequiredFood > 0) {
            if (this.food >= totalRequiredFood) {
                for (Bee bee : beeList) {
                    double add = 25 - bee.getFood();
                    bee.addFood(add);
                    this.food -= add;
                }
            } else {
                double equalFoodShare = this.food / beeList.size();
                for (Bee bee : beeList) {
                    bee.addFood(equalFoodShare);
                }
                this.food = 0;
            }
        }

        double totalRequiredJelly = 0.0;


        for (Bee bee : birthBeeList) {
            totalRequiredJelly += 2 - bee.getJelly();
        }


        if (this.royalJelly >= totalRequiredJelly) {
            for (Bee bee : birthBeeList) {
                double add = 2 - bee.getJelly();
                bee.addJelly(add);
                this.royalJelly -= add;
            }
        } else {
            double equalFoodShare = this.royalJelly / birthBeeList.size();
            for (Bee bee : birthBeeList) {
                bee.addJelly(equalFoodShare);
            }
        }
    }

    public void healBees() {
        if (illBeeList.isEmpty()) {
            return;
        }
        illBeeList.sort(Comparator.comparingInt(bee -> bee.getTypeBee().ordinal()));
        List<Bee> recoveryList = new ArrayList<>();
        for (Bee bee : doctorBeeList) {
            if (bee.isIll() || bee.isDead()) {
                continue;
            }
            for (Bee illBee : illBeeList) {
                if (!illBee.isIll()) {
                    continue;
                }

                if (royalJelly >= 0.1) {
                    royalJelly -= 0.1;
                    if (WorldBaseGen.random.nextDouble() < 0.25 + chanceHealing) {
                        illBee.setIll(false);
                        recoveryList.add(illBee);
                    }
                } else {
                    break;
                }
            }
        }
        illBeeList.removeAll(recoveryList);
    }

    public void healBeesFromApothecary(TileEntityApothecaryBee apothecaryBee) {
        if (illBeeList.isEmpty()) {
            return;
        }
        illBeeList.sort(Comparator.comparingInt(bee -> bee.getTypeBee().ordinal()));
        List<Bee> recoveryList = new ArrayList<>();

        for (Bee illBee : illBeeList) {
            if (!illBee.isIll()) {
                continue;
            }
            if (apothecaryBee.energy.getEnergy() < 50) {
                break;
            }
            apothecaryBee.energy.useEnergy(50);
            illBee.setIll(false);
            recoveryList.add(illBee);


        }

        illBeeList.removeAll(recoveryList);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);
        if (queen != null) {
            NBTTagList tagList = new NBTTagList();
            for (Bee bee : apairyBeeList) {
                tagList.appendTag(bee.writeToNBT());
            }
            nbt.setByte("bee_id", (byte) queen.getId());
            nbt.setTag("bees", tagList);
            nbt.setLong("id_queen", id);
            nbt.setTag("stack", stack.serializeNBT());
        }

        nbt.setShort("royalJelly", (short) (royalJelly*10));
        nbt.setShort("food", (short) (food*10));
        nbt.setByte("harvest", (byte) harvest);
        nbt.setInteger("birth", birth);
        nbt.setInteger("generation", generation);
        nbt.setInteger("death", death);
        NBTTagList tagList = new NBTTagList();
        statusMap.forEach((id, status) -> {
            final NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setLong("id", id);
            nbt1.setByte("status", (byte) status.ordinal());
        });
        nbt.setTag("status", tagList);

        return nbt;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("bees")) {
            int beeId = nbt.getByte("bee_id");
            id = nbt.getLong("id_queen");
            queen = BeeNetwork.instance.getBee(beeId);
            this.stack = new ItemStack(nbt.getCompoundTag("stack"));
            NBTTagList tagList = nbt.getTagList("bees", 10);
            apairyBeeList.clear();
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound beeTag = tagList.getCompoundTagAt(i);
                Bee bee = new Bee(beeTag);
                apairyBeeList.add(bee);
                if (bee.isIll()) {
                    illBeeList.add(bee);
                }
                if (bee.isChild()) {
                    birthBeeList.add(bee);
                }
                switch (bee.getTypeBee()) {
                    case BUILDER:
                        buildersBeeList.add(bee);
                        break;
                    case WORKER:
                        workersBeeList.add(bee);
                        break;
                    case DOCTOR:
                        doctorBeeList.add(bee);
                        break;
                    case ATTACK:
                        attackBeeList.add(bee);
                        break;
                }
            }
        }
        statusMap.clear();
        NBTTagList tagList = nbt.getTagList("status", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbt1 = tagList.getCompoundTagAt(i);

            long id = nbt1.getLong("id");
            byte statusOrdinal = nbt1.getByte("status");
            EnumStatus status = EnumStatus.values()[statusOrdinal];
            statusMap.put(id, status);
        }
        royalJelly = nbt.getShort("royalJelly")/10D;
        food = nbt.getShort("food")/10D;
        harvest = nbt.getByte("harvest");
        birth = nbt.getInteger("birth");
        death = nbt.getInteger("death");
        generation = nbt.getInteger("generation");
    }
    public void death(Bee bee){
        death++;
        deathTask = 1;
        switch (bee.getTypeBee()) {
            case ATTACK:
                attackBeeList.remove(bee);
                break;
            case DOCTOR:
                doctorBeeList.remove(bee);
                break;
            case WORKER:
                workersBeeList.remove(bee);
                break;
            case BUILDER:
                buildersBeeList.remove(bee);
                break;
        }
        illBeeList.remove(bee);
        if (bee.getType() == EnumTypeLife.LARVA) {
            birthBeeList.remove(bee);
        }

        apairyBeeList.remove(bee);
    }

    public void setTickDrainJelly(final byte tickDrainJelly) {
        this.tickDrainJelly = tickDrainJelly;
    }

    public void setTickDrainFood(final byte tickDrainFood) {
        this.tickDrainFood = tickDrainFood;
    }

    public byte getTickDrainFood() {
        return tickDrainFood;
    }

    public byte getTickDrainJelly() {
        return tickDrainJelly;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        final boolean day = getWorld().provider.isDaytime();
        if (!this.apairyBeeList.isEmpty() && this
                .getWorld()
                .getWorldTime() % 20 == 0 && queen != null && (day && (queen.isSun() || sunGenome) || (!day && (queen.isNight() || nightGenome)))) {
            for (int i = 0; i < this.frameSlot.size(); i++) {
                ItemStack stack = this.frameSlot.get(i);
                if (!stack.isEmpty()) {
                    if (((IDamageItem) stack.getItem()).applyCustomDamage(stack, -20, null)) {
                        this.frameSlot.put(i, ItemStack.EMPTY);
                    }
                }
            }
            if (this.getWorld().getWorldTime() % 6000 == 0){
                generation++;
                canAdaptationBee();
            }
            this.rain = world.isRaining();
            this.thundering = world.isThundering();
            feedBees(apairyBeeList);
            if (food > maxFood) {
                food = maxFood;
            }
            if (royalJelly > maxJelly) {
                royalJelly = maxJelly;
            }
            if (royalJelly < 0) {
                royalJelly = 0;
            }
            massiveNeeds[0] = this.food / (maxFood * 1D);
            massiveNeeds[1] = this.apairyBeeList.size() * 1D / (queen.getMaxSwarm() * swarmGenome);
            enemy = getNearbyEntitiesWithWeight();
            problemList.clear();
            double maxValue = enemy.values()
                    .stream()
                    .max(Double::compareTo)
                    .orElse(0.0);
            bees_nearby = getNearbyBees();
            maxValue = Math.max(bees_nearby.values()
                    .stream()
                    .max(Double::compareTo)
                    .orElse(0.0), maxValue);
            massiveNeeds[2] = maxValue;
            massiveNeeds[3] = this.illBeeList.size() * 1D / (this.apairyBeeList.size() - birthBeeList.size());
            massiveNeeds[4] = this.royalJelly / (maxJelly * 1D);
            this.task = beeAI.predict(massiveNeeds);
            this.workers = workersBeeList.size();
            this.builders = buildersBeeList.size();
            this.bees = apairyBeeList.size();
            this.attacks = attackBeeList.size();
            this.doctors = doctorBeeList.size();
            this.ill = illBeeList.size();
            deathTask = 0;
            illTask = 0;
            switch (task) {
                case 0:
                    queen.removeAllPercent(0.5);
                    harvest();
                    if (tickDrainJelly > 20) {
                        tickDrainJelly = 20;
                    }
                    if (tickDrainFood > 20) {
                        tickDrainFood = 20;
                    }
                    break;
                case 1:
                    attackPlayers(enemy);
                    attackBees();
                    break;
                case 2:
                    healBees();
                    break;
                case 3:
                    birthBees();
                    break;
                case 4:
                    canHelpOtherBees();
                    break;
            }
            problemList.clear();
            if (massiveNeeds[0] < 0.3) {
                problemList.add(EnumProblem.FOOD);
            }
            if (massiveNeeds[1] < 0.3) {
                problemList.add(EnumProblem.BEES);
            }
            if (massiveNeeds[3] > 0.15) {
                problemList.add(EnumProblem.ILL);
            }
            if (massiveNeeds[4] < 0.3) {
                problemList.add(EnumProblem.JELLY);
            }
            List<Bee> iterator = new ArrayList<>(apairyBeeList);
            ChunkLevel airPollution = PollutionManager.pollutionManager.getChunkLevelAir(chunkPos);
            int totalBees = (apairyBeeList.size() - birthBeeList.size());
            int targetWorkers = (int) (totalBees * (percentWorkersBee / 100.0));
            int targetDoctors = (int) (totalBees * (percentDoctorBee / 100.0));
            int targetAttackers = (int) (totalBees * (percentAttackBee / 100.0));
            int targetBuilders = (int) (totalBees * (percentBuildersBee / 100.0));
            boolean canChangeWork = targetWorkers > workers || targetDoctors > doctors || targetAttackers > attacks || targetBuilders > builders;
            boolean needChangeWorkers = targetWorkers < workers;
            boolean needChangeDoctors = targetDoctors < doctors;
            boolean needChangeAttacks = targetAttackers < attacks;
            boolean needChangeBuilders = targetBuilders < builders;
            EnumTypeBee typeBee = needChangeWorkers ? EnumTypeBee.WORKER : needChangeDoctors ? EnumTypeBee.DOCTOR :
                    needChangeAttacks ? EnumTypeBee.ATTACK : needChangeBuilders ? EnumTypeBee.BUILDER : EnumTypeBee.NONE;
          for (Bee bee : iterator){
                boolean isChild = bee.isChild();
                bee.addTick((int) (20 + (isChild ? speedBirthRate * 20 : -20 * slowAging)), populationGenome);
                if (bee.isDead()) {
                    death(bee);
                } else {
                    bee.removeFood();
                    if (bee.getType() == EnumTypeLife.LARVA) {
                        bee.removeJelly();
                    }

                    if (bee.isIll() && royalJelly >= 0.5) {
                        if (WorldBaseGen.random.nextInt(100) == 0) {
                            royalJelly -= 0.5;
                            bee.setIll(false);
                            illBeeList.remove(bee);
                        } else {
                            royalJelly -= 0.05;
                            if (WorldBaseGen.random.nextDouble() < 0.25 && WorldBaseGen.random.nextDouble() < queen.getMaxMortalityRate() * this.mortalityGenome ) {
                                bee.setDead(true);
                                death(bee);
                                continue;
                            }
                        }
                    } else {
                        if (WorldBaseGen.random.nextDouble() < 0.1 && WorldBaseGen.random.nextDouble() <= queen.getMaxMortalityRate() * this.mortalityGenome) {
                            bee.setDead(true);
                            death(bee);
                            continue;
                        }
                    }
                    if (!bee.isChild() && isChild && !bee.isDead()) {
                        birthBeeList.remove(bee);
                        findWork(bee);
                    }
                    if (!bee.isChild() && bee.getTypeBee() == typeBee && canChangeWork && WorldBaseGen.random.nextInt(4) == 0) {
                        switch (typeBee) {
                            case ATTACK:
                                attackBeeList.remove(bee);
                                break;
                            case DOCTOR:
                                doctorBeeList.remove(bee);
                                break;
                            case WORKER:
                                workersBeeList.remove(bee);
                                break;
                            case BUILDER:
                                buildersBeeList.remove(bee);
                                break;
                        }
                        if (targetWorkers > workers) {
                            workersBeeList.add(bee);
                            bee.setTypeBee(EnumTypeBee.WORKER);
                        } else if (targetBuilders > builders) {
                            buildersBeeList.add(bee);
                            bee.setTypeBee(EnumTypeBee.BUILDER);
                        } else if (targetDoctors > doctors) {
                            doctorBeeList.add(bee);
                            bee.setTypeBee(EnumTypeBee.DOCTOR);
                        } else if (targetAttackers > attacks) {
                            attackBeeList.add(bee);
                            bee.setTypeBee(EnumTypeBee.ATTACK);
                        }
                        totalBees = (apairyBeeList.size() - birthBeeList.size() + 1);
                        targetWorkers = (int) (totalBees * (percentWorkersBee / 100.0));
                        targetDoctors = (int) (totalBees * (percentDoctorBee / 100.0));
                        targetAttackers = (int) (totalBees * (percentAttackBee / 100.0));
                        targetBuilders = (int) (totalBees * (percentBuildersBee / 100.0));
                        canChangeWork = targetWorkers > workers || targetDoctors > doctors || targetAttackers > attacks || targetBuilders > builders;
                    }

                    if (!bee.isChild() && !bee.isIll()) {
                        int illnessChance =
                                5 - (bee.getTypeBee() == EnumTypeBee.DOCTOR ? 2 : 0) + (chunkLevel
                                        .getLevelPollution()
                                        .ordinal() > this.soilPollution.ordinal()
                                        ? 3 * (chunkLevel.getLevelPollution().ordinal() - this.soilPollution.ordinal())
                                        : 0);
                        if (airPollution != null && airPollution.getLevelPollution().ordinal() > this.airPollution.ordinal()) {
                            illnessChance += (4 * (airPollution.getLevelPollution().ordinal() - this.airPollution.ordinal()));
                        }
                        if (bee.getTypeBee() == EnumTypeBee.DOCTOR){
                            illnessChance = (int) (illnessChance * 0.5);
                        }
                        if (WorldBaseGen.random.nextInt(1500) < illnessChance * hardeningGenome) {
                            bee.setIll(true);
                            illTask = 1;
                            illBeeList.add(bee);
                        }
                        if (this.radLevel.getLevel().ordinal() > this.radiationPollution.ordinal()){
                            if (WorldBaseGen.random.nextInt(100) < (this.radLevel.getLevel().ordinal()-this.radiationPollution.ordinal()) * 25){
                                bee.setDead(true);
                              death(bee);
                            }
                        }
                    }

                }

            }
            if (apairyBeeList.isEmpty()) {
                queen = null;
            }
        } else {
            task = 4;
            deathTask = 0;
            illTask = 0;
            this.workers = workersBeeList.size();
            this.builders = buildersBeeList.size();
            this.bees = apairyBeeList.size();
            this.attacks = attackBeeList.size();
            this.doctors = doctorBeeList.size();
            this.ill = illBeeList.size();
        }

        if (!this.foodCellSlot.isEmpty() && tickDrainFood == 20) {
            if (this.food > 750) {
                tickDrainFood = 0;
                foodCellSlot.get(0).shrink(1);
                this.invSlotFood.add(ModUtils.getCellFromFluid(FluidName.fluidhoney.getInstance()));
                this.food -= 750D;
            }
        }
        if (!this.jellyCellSlot.isEmpty() && tickDrainJelly == 20) {
            if (this.royalJelly > 50) {
                tickDrainJelly = 0;
                jellyCellSlot.get(0).shrink(1);
                this.invSlotJelly.add(ModUtils.getCellFromFluid(FluidName.fluidroyaljelly.getInstance()));
                this.royalJelly -= 50D;
            }
        }
    }

    private void canAdaptationBee() {
        if (WorldBaseGen.random.nextInt(100) >= queen.getChance()) {
            return;
        }
        Genome genome = this.genome;
        int geneticAdaptive = genomeAdaptive != 0 ? genomeAdaptive : 5;
        int geneticResistance = genomeResistance != 0 ? genomeResistance : 5;
        cycle:
        for (EnumGenetic enumGenetic :  EnumGenetic.values()) {
            final List<GeneticTraits> genetic = enumGeneticListMap.get(enumGenetic);
            if (WorldBaseGen.random.nextInt(100) <= geneticAdaptive) {
                if (WorldBaseGen.random.nextInt(100) > geneticResistance) {
                    boolean hasGenome = genome.hasGenome(enumGenetic);
                    if (!hasGenome && WorldBaseGen.random.nextInt(100) == 0) {
                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                        genome.addGenome(geneticTraits, stack);
                    } else {
                        boolean needRemove =
                                WorldBaseGen.random.nextInt(geneticResistance) > WorldBaseGen.random.nextInt(geneticAdaptive);
                        boolean canUpgrade =
                                WorldBaseGen.random.nextInt(geneticAdaptive) > WorldBaseGen.random.nextInt(geneticResistance);
                        if ((needRemove || !canUpgrade) && hasGenome) {
                           genome.removeGenome(enumGenetic, stack);
                        } else if (canUpgrade) {
                            switch (enumGenetic) {
                                case SUN:
                                    if (!hasGenome && !queen.isSun()) {
                                        genome.addGenome(genetic.get(0), stack);
                                    }
                                    break cycle;
                                case NIGHT:
                                    if (!hasGenome && !queen.isNight()) {
                                        genome.addGenome(genetic.get(0), stack);
                                    }
                                    break cycle;
                                case COEF_BIOME:
                                    boolean canWork = queen.canWorkInBiome(biome);
                                    if (!hasGenome) {
                                        if (!canWork && (WorldBaseGen.random.nextInt(4) == 0)) {
                                            genome.addGenome(genetic.get(0), stack);
                                        }
                                    } else {
                                        if (canWork  && (WorldBaseGen.random.nextInt(4) == 0)) {
                                            genome.removeGenome(genetic.get(0), stack);
                                        }
                                    }
                                    break cycle;
                                case AIR:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        genome.addGenome(geneticTraits, stack);
                                    } else {
                                        boolean needDecrease = WorldBaseGen.random.nextBoolean();
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits.getPrev(), stack);
                                             } else {
                                                genome.removeGenome(traits, this.stack);
                                            }

                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits1, stack);
                                            }
                                        }
                                    }
                                    break cycle;
                                case SOIL:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        LevelPollution levelPollution = geneticTraits.getValue(LevelPollution.class);
                                        if (levelPollution.ordinal() >= this.chunkLevel.getLevelPollution().ordinal()) {
                                            genome.addGenome(geneticTraits, stack);
                                        }
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        LevelPollution levelPollution = traits.getValue(LevelPollution.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean() && this.chunkLevel
                                                        .getLevelPollution()
                                                        .ordinal() < levelPollution.ordinal();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits.getPrev(), stack);
                                           } else {
                                                genome.removeGenome(traits, this.stack);
                                             }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits1, stack);
                                            }
                                        }


                                    }
                                    break cycle;
                                case PEST:
                                case MORTALITY_RATE:
                                case FOOD:
                                case JELLY:
                                case PRODUCT:
                                case HARDENING:
                                case POPULATION:
                                case BIRTH:
                                case GENOME_ADAPTIVE:
                                case GENOME_RESISTANCE:
                                case SWARM:
                                case RADIUS:
                                case WEATHER:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        genome.addGenome(geneticTraits, stack);
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits.getPrev(), stack);
                                            } else {
                                                genome.removeGenome(traits, this.stack);
                                             }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits1, stack);
                                            }
                                        }


                                    }
                                    break cycle;
                                case RADIATION:
                                    if (!hasGenome) {
                                        GeneticTraits geneticTraits = genetic.get(WorldBaseGen.random.nextInt(genetic.size()));
                                        EnumLevelRadiation levelPollution = geneticTraits.getValue(EnumLevelRadiation.class);
                                        if (levelPollution.ordinal() >= this.radLevel.getLevel().ordinal()) {
                                            genome.addGenome(geneticTraits, stack);
                                         }
                                    } else {
                                        GeneticTraits traits = genome.getGenome(enumGenetic);
                                        EnumLevelRadiation levelPollution = traits.getValue(EnumLevelRadiation.class);
                                        boolean needDecrease =
                                                WorldBaseGen.random.nextBoolean() && this.radLevel
                                                        .getLevel()
                                                        .ordinal() < levelPollution.ordinal();
                                        if (needDecrease) {
                                            if (traits.getPrev() != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits.getPrev(), stack);
                                                   } else {
                                                genome.removeGenome(traits, this.stack);
                                           }
                                        } else {
                                            GeneticTraits traits1 = geneticTraitsMap.get(traits);
                                            if (traits1 != null) {
                                                genome.removeGenome(traits, this.stack);
                                                genome.addGenome(traits1, stack);
                                            }
                                        }


                                    }
                                    break cycle;
                            }
                        }
                    }
                }
            }
        }
        reset();
        set();
    }

    private void lootApiary(TileEntityApiary apiary) {
        double foodAmount = apiary.food;
        double loot = (foodAmount * (0.3 + WorldBaseGen.random.nextDouble() * 0.2));
        this.food += loot;
        apiary.food -= loot;
        if (WorldBaseGen.random.nextDouble() < 0.5) {
            double royalJellyAmount = apiary.royalJelly;
            double loot1 = (royalJellyAmount * (0.2 + WorldBaseGen.random.nextDouble() * 0.15));
            this.royalJelly += loot1;
            apiary.royalJelly -= loot1;
        }
    }

    private void attackBees() {
        for (TileEntityApiary apiary : bees_nearby.keySet()) {
            EnumStatus status = getStatusMap().get(apiary.id);
            EnumStatus status1 = apiary.statusMap.get(id);
            double value = bees_nearby.get(apiary);
            if (value > 0.5) {
                if (status == EnumStatus.ANGRY || status1 == EnumStatus.ANGRY) {
                    List<Bee> attack = attackBeeList;
                    List<Bee> defenderApiary = apiary.attackBeeList;
                    boolean defenseWon = performBattle(attack, defenderApiary);

                    if (!defenseWon) {
                        lootApiary(apiary);
                    }

                    break;
                }
            }
        }
    }

    private boolean performBattle(List<Bee> attackers, List<Bee> defenders) {
        int attackerPower = attackers.size();
        int defenderPower = defenders.size();


        if (attackerPower > defenderPower) {

            for (Bee defender : defenders) {
                if (WorldBaseGen.random.nextDouble() < 0.5) {
                    defender.setDead(true);


                }
            }
            return false;
        } else {

            for (Bee attacker : attackers) {
                if (WorldBaseGen.random.nextDouble() < 0.3) {
                    attacker.setDead(true);
                }
            }
            return true;
        }
    }

    private void canHelpOtherBees() {
        boolean helped = false;
        cycle:
        for (TileEntityApiary apiary : bees_nearby.keySet()) {
            EnumStatus status = getStatusMap().get(apiary.id);
            EnumStatus status1 = apiary.statusMap.get(id);
            if (status != EnumStatus.ANGRY && status1 != EnumStatus.ANGRY) {
                List<EnumProblem> problems = apiary.problemList;
                if ((status == EnumStatus.NORMAL && WorldBaseGen.random.nextBoolean()) || status == EnumStatus.FRIENDLY) {
                    for (EnumProblem problem : problems) {
                        if (helped) {
                            break cycle;
                        }
                        switch (problem) {
                            case FOOD:
                                double weight = massiveNeeds[0] - (0.3 - apiary.massiveNeeds[0]) - 0.3;
                                if (weight >= 0.01) {
                                    double canFood = 1000 * weight;
                                    this.food -= canFood;
                                    apiary.food += canFood;
                                    helped = true;
                                    if (status1 == EnumStatus.NORMAL) {
                                        if (WorldBaseGen.random.nextDouble() < 0.25) {
                                            apiary.statusMap.replace(id, EnumStatus.FRIENDLY);
                                        }
                                    }
                                }

                                break;
                            case JELLY:
                                weight = massiveNeeds[4] - (0.3 - apiary.massiveNeeds[4]) - 0.3;
                                if (weight >= 0.01) {
                                    double canFood = 200 * weight;
                                    this.royalJelly -= canFood;
                                    apiary.royalJelly += canFood;
                                    helped = true;
                                    if (status1 == EnumStatus.NORMAL) {
                                        if (WorldBaseGen.random.nextDouble() < 0.25) {
                                            apiary.statusMap.replace(id, EnumStatus.FRIENDLY);
                                        }
                                    }
                                }

                                break;
                            case ILL:
                                apiary.illBeeList.sort(Comparator.comparingInt(bee -> bee.getTypeBee().ordinal()));
                                List<Bee> recoveryList = new ArrayList<>();
                                for (Bee bee : doctorBeeList) {
                                    if (bee.isIll() || bee.isDead()) {
                                        continue;
                                    }
                                    for (Bee illBee : apiary.illBeeList) {
                                        if (!illBee.isIll()) {
                                            continue;
                                        }
                                        if (royalJelly >= 0.1) {
                                            royalJelly -= 0.1;
                                            if (WorldBaseGen.random.nextDouble() < 0.25 + chanceHealing) {
                                                illBee.setIll(false);
                                                recoveryList.add(illBee);
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                if (status1 == EnumStatus.NORMAL) {
                                    if (WorldBaseGen.random.nextDouble() < 0.25) {
                                        apiary.statusMap.replace(id, EnumStatus.FRIENDLY);
                                    }
                                }
                                apiary.illBeeList.removeAll(recoveryList);
                                break;
                            case BEES:
                                weight = massiveNeeds[3] - (0.3 - apiary.massiveNeeds[3]) - 0.3;
                                if (weight >= 0.01) {
                                    int col = (int) (weight * apairyBeeList.size());
                                    for (int i = 0; i < col; i++) {
                                        Bee bee = apairyBeeList.get(WorldBaseGen.random.nextInt(apairyBeeList.size()));
                                        while (bee.isChild()) {
                                            bee = apairyBeeList.get(WorldBaseGen.random.nextInt(apairyBeeList.size()));
                                        }
                                        switch (bee.getTypeBee()) {
                                            case BUILDER:
                                                buildersBeeList.remove(bee);
                                                apiary.buildersBeeList.add(bee);
                                                break;
                                            case WORKER:
                                                workersBeeList.remove(bee);
                                                apiary.workersBeeList.add(bee);
                                                break;
                                            case DOCTOR:
                                                doctorBeeList.remove(bee);
                                                apiary.doctorBeeList.add(bee);
                                                break;
                                            case ATTACK:
                                                attackBeeList.remove(bee);
                                                apiary.attackBeeList.add(bee);
                                                break;
                                        }
                                        helped = true;
                                        this.apairyBeeList.remove(bee);
                                        apiary.apairyBeeList.add(bee);
                                        if (status1 == EnumStatus.NORMAL) {
                                            apiary.statusMap.replace(id, EnumStatus.FRIENDLY);
                                        }
                                    }

                                }

                                break;
                        }
                    }
                    if (helped) {
                        break;
                    }
                }
            }
        }
    }

    public Map<Long, EnumStatus> getStatusMap() {
        return statusMap;
    }

    public long getId() {
        return id;
    }

    private Map<TileEntityApiary, Double> getNearbyBees() {
        apiaries.clear();
        passedApiaries.clear();
        for (ChunkPos chunkPos : chunkPositions) {
            apiaries.addAll(chunkPosListMap1.get(chunkPos));
        }
        Map<TileEntityApiary, Double> map = new HashMap<>();
        apiaries = apiaries
                .stream()
                .filter(apiary -> apiary.getQueen() != null && contains(apiary.pos) && apiary.id != this.id)
                .collect(Collectors.toList());
        for (TileEntityApiary apiary : apiaries) {
            Map<Long, EnumStatus> statusMap1 = apiary.getStatusMap();
            EnumStatus status = getStatusMap().get(apiary.id);
            if (status == null) {
                status = EnumStatus.NORMAL;
                if (this.queen.getUnCompatibleBees().contains(apiary.queen)) {
                    status = EnumStatus.ANGRY;
                }
                statusMap.put(apiary.id, status);
            }
            EnumStatus status1 = statusMap1.computeIfAbsent(id, k -> {
                if (apiary.queen.getUnCompatibleBees().contains(this.queen)) {
                    return EnumStatus.ANGRY;
                }
                return EnumStatus.NORMAL;
            });
            if (status1 != EnumStatus.ANGRY && status != EnumStatus.ANGRY) {
                map.put(apiary, 0.0);
            } else {
                double distance = distanceTo(center, apiary.getPos());
                double weight = calculateWeight(distance, maxDistance);
                map.put(apiary, weight);
            }

        }
        return map;
    }

    public double distanceTo(Vec3d vec, BlockPos pos) {
        double d0 = pos.getX() - vec.x;
        double d1 = pos.getY() - vec.y;
        double d2 = pos.getZ() - vec.z;
        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    private void findWork(Bee bee) {
        int totalBees = (apairyBeeList.size() - birthBeeList.size());
        double targetWorkers = Math.ceil(totalBees * (percentWorkersBee / 100.0));
        double targetDoctors =  Math.ceil(totalBees * (percentDoctorBee / 100.0));
        double targetAttackers =  Math.ceil(totalBees * (percentAttackBee / 100.0));
        double targetBuilders =  Math.ceil(totalBees * (percentBuildersBee / 100.0));
        if (targetWorkers > workersBeeList.size()) {
            bee.setTypeBee(EnumTypeBee.WORKER);
            bee.setType(EnumTypeLife.BEE);
            workersBeeList.add(bee);
        } else if (targetDoctors > doctorBeeList.size()) {
            bee.setTypeBee(EnumTypeBee.DOCTOR);
            bee.setType(EnumTypeLife.BEE);
            doctorBeeList.add(bee);
        } else if (targetBuilders > buildersBeeList.size()) {
            bee.setTypeBee(EnumTypeBee.BUILDER);
            bee.setType(EnumTypeLife.BEE);
            buildersBeeList.add(bee);
        } else if (targetAttackers > attackBeeList.size() ){
            bee.setTypeBee(EnumTypeBee.ATTACK);
            bee.setType(EnumTypeLife.BEE);
            attackBeeList.add(bee);
        }else{
            bee.setDead(true);
            death(bee);
        }

    }

    private void birthBees() {
        for (Bee bee : workersBeeList) {
            if (!bee.isIll() && !bee.isDead()) {
                for (int i = 0; i < WorldBaseGen.random.nextInt((int) (queen.getOffspring() * birthRateGenome)); i++) {
                    if (this.apairyBeeList.size() < (this.queen.getMaxSwarm() * swarmGenome) && royalJelly >= 0.2 && food >= 2) {
                        Bee bee1 = new Bee(EnumTypeBee.NONE, queen, EnumTypeLife.LARVA, 2, 0);
                        bee1.addJelly(0.2);
                        royalJelly -= 0.2;
                        this.food -= 2;
                        this.birthBeeList.add(bee1);
                        this.apairyBeeList.add(bee1);
                        birth++;
                    }
                }
            }
        }
    }

    public boolean contains(BlockPos vec) {
        if (vec.getX() > this.axisAlignedBB.minX && vec.getX() < axisAlignedBB.maxX) {
            if (vec.getY() > this.axisAlignedBB.minY && vec.getY() < axisAlignedBB.maxY) {
                return vec.getZ() > axisAlignedBB.minZ && vec.getZ() < axisAlignedBB.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void harvest() {
        passedCrops.clear();
        crops.clear();
        for (ChunkPos chunkPos : chunkPositions) {
            crops.addAll(chunkPosListMap.get(chunkPos));
        }
        crops = crops.stream()
                .filter(crop -> crop.getCrop() != null)
                .sorted(Comparator.comparing(
                        crop -> !(crop.getCrop().getId() == this.queen.getCropFlower().getId())
                ))
                .collect(Collectors.toList());
        boolean addFood = false;
        boolean addJelly = false;
        cycle:
        for (Bee bee : this.workersBeeList) {
            if (!bee.isIll() && !bee.isDead()) {
                if (passedCrops.size() == crops.size()) {
                    break;
                }
                for (TileEntityCrop crop : crops) {
                    final long beeId = crop.getBeeId();
                    if (contains(crop.getPos()) && !passedCrops.contains(crop) && beeId == 0) {
                        if (WorldBaseGen.random.nextDouble() < 0.5) {
                            passedCrops.add(crop);
                            crop.setBeeId(this.id);
                            int stage = crop.getCrop().getStage();
                            if (crop.getCrop().getTick() < crop.getCrop().getMaxTick()) {
                                crop.getCrop().addTick((int) ((int) ((crop
                                        .getCrop()
                                        .getGrowthSpeed() * 10 + 10 * speedCrop)) * pestGenome));
                            }
                            boolean needUpdate = stage != crop.getCrop().getStage();
                            if (needUpdate) {
                                new PacketUpdateFieldTile(crop, "tick", crop.getCrop().getTick());
                            }
                            if (this.queen.getCropFlower().getId() == crop.getCrop().getId()) {
                                double canRoyalJelly = 0.15;
                                if (!this.birthBeeList.isEmpty() || (!doctorBeeList.isEmpty() && doctorBeeList.size() * 1D / apiaries.size() > 0.2)) {
                                    canRoyalJelly = 0.2;
                                }
                                this.royalJelly += canRoyalJelly * (1 + producing) * coef * jellyGenome;
                                addJelly = true;
                                if (this.royalJelly > 200D) {
                                    this.royalJelly = 200D;
                                }
                            } else {
                                queen.addPercentProduct(crop.getCrop(), 0.5);
                            }
                            this.food += 2 * (1 + producing) * coef * foodGenome;
                            addFood = true;
                            if (food > 1000D) {
                                break cycle;
                            }
                        }
                    }
                }
                int weatherResistance = queen.getWeatherResistance();
                if (rain && weatherResistance < 1 && weatherGenome < 0) {
                    if (WorldBaseGen.random.nextInt(100) < 5 * hardeningGenome) {
                        bee.setIll(true);
                        illTask = 1;
                        illBeeList.add(bee);
                    }
                } else if (thundering && weatherResistance < 2 && weatherGenome < 1) {
                    if (WorldBaseGen.random.nextInt(100) < 10 * hardeningGenome) {
                        bee.setIll(true);
                        illTask = 1;
                        illBeeList.add(bee);
                    }
                }
            }
        }


        if (harvest >= 4 / coef) {
            if (addFood){
                this.tickDrainFood++;
            }
            if (addJelly){
                this.tickDrainJelly++;
            }
            if (WorldBaseGen.random.nextBoolean()) {
                queen.getProduct().forEach(product -> {
                    if (WorldBaseGen.random.nextInt(100) < product.getChance() * productGenome) {
                        invSlotProduct.add(product.getCrop().getDrop());
                    }
                });
            }
            harvest = 0;
        } else {
            harvest++;
        }
    }

    private void attackPlayers(Map<EntityPlayer, Double> enemy) {

        enemy.forEach((key, value) -> {

            if (value >= 0.7) {
                for (Bee bee : attackBeeList) {
                    if (!bee.isIll()) {
                        if (WorldBaseGen.random.nextInt(100) < 20) {
                            key.attackEntityFrom(IUDamageSource.bee, 1);
                        }
                    }
                }
            }
        });

    }
    public Genome getGenome(){
        return genome;
    }
}
