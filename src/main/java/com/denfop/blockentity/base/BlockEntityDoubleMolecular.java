package com.denfop.blockentity.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.*;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomerEntity;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBaseDoubleMolecular;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenDoubleMolecularTransformer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;


public class BlockEntityDoubleMolecular extends BlockEntityElectricMachine implements
        IUpdatableTileEvent, IUpdateTick, IHasRecipe, IIsMolecular {

    public boolean need;
    public MachineRecipe output;
    public List<Double> time;
    public boolean queue;
    public byte redstoneMode;
    public int operationLength;
    public boolean need_put_check = false;
    public int operationsPerTick;
    public InventoryRecipes inputSlot;
    public double perenergy;
    public double differenceenergy;
    protected double progress;
    protected double guiProgress;
    protected int size_recipe = 0;
    protected ItemStack output_stack;
    @OnlyIn(Dist.CLIENT)
    private BakedModel bakedModel;
    @OnlyIn(Dist.CLIENT)
    private BakedModel transformedModel;

    public BlockEntityDoubleMolecular(BlockPos pos, BlockState state) {
        super(0, 14, 1, BlockDoubleMolecularTransfomerEntity.double_transformer, pos, state);
        this.progress = 0;
        this.time = new ArrayList<>();
        this.queue = false;
        this.redstoneMode = 0;
        this.inputSlot = new InventoryRecipes(this, "doublemolecular", this) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (((BlockEntityDoubleMolecular) this.tile).getOutput() == null) {
                    if (!content.isEmpty()) {
                        final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                this.getRecipe(),
                                this.getRecipe_list(),
                                false,
                                Arrays.asList(
                                        content,
                                        content
                                )
                        );
                        ((BlockEntityDoubleMolecular) this.tile).need_put_check = recipe1 != null;
                    } else {
                        if (!this.get(0).isEmpty() || !this.get(1).isEmpty()) {
                            if (!this.get(0).isEmpty()) {
                                final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                        this.getRecipe(),
                                        this.getRecipe_list(),
                                        false,
                                        Arrays.asList(
                                                this.get(0),
                                                this.get(0)
                                        )
                                );
                                ((BlockEntityDoubleMolecular) this.tile).need_put_check = recipe1 != null;
                            } else {
                                final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                        this.getRecipe(),
                                        this.getRecipe_list(),
                                        false,
                                        Arrays.asList(
                                                this.get(1),
                                                this.get(1)
                                        )
                                );
                                ((BlockEntityDoubleMolecular) this.tile).need_put_check = recipe1 != null;
                            }
                        } else {
                            ((BlockEntityDoubleMolecular) this.tile).need_put_check = false;
                        }
                    }
                } else {
                    ((BlockEntityDoubleMolecular) this.tile).need_put_check = false;
                }
                return content;
            }
        };
        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));
        this.output = null;
        this.need = false;
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack stack, ItemStack stack2, ItemStack stack1, double energy) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("doublemolecular", new BaseMachineRecipe(
                new Input(input.getInput(stack), input.getInput(stack2)),
                new RecipeOutput(nbt, stack1)
        ));
    }

    public static void addrecipe(ItemStack stack, String stack2, ItemStack stack1, double energy) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("doublemolecular", new BaseMachineRecipe(
                new Input(input.getInput(stack), input.getInput(stack2)),
                new RecipeOutput(nbt, stack1)
        ));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockDoubleMolecularTransfomerEntity.double_transformer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockdoublemolecular.getBlock();
    }

    @Override
    public ItemStack getItemStack() {
        return this.output_stack;
    }

    @Override
    public BlockEntityBase getEntityBlock() {
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BakedModel getBakedModel() {
        return bakedModel;
    }

    public void init() {


        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.LONG_REGENERATION),
                new ItemStack(IUItem.upgrademodule.getStack(17)
                        , 1),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.LONG_INVISIBILITY),
                new ItemStack(IUItem.upgrademodule.getStack(22), 1),
                4000000
        );
        addrecipe(new ItemStack(IUItem.module_schedule.getItem(), 1), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE),
                new ItemStack(IUItem.upgrademodule.getStack(18)), 4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.LONG_POISON),
                new ItemStack(IUItem.upgrademodule.getStack(19)),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(Items.NETHER_STAR, 1),
                new ItemStack(
                        IUItem.upgrademodule.getStack(20), 1
                ),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.spawnermodules.getStack(0), 1),
                new ItemStack(IUItem.upgrademodule.getStack(23)),
                4000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(Items.BLAZE_ROD, 4),
                new ItemStack(
                        IUItem.upgrademodule.getStack(24), 1
                ),
                4000000
        );
        addrecipe(new ItemStack(IUItem.module_schedule.getItem(), 1), new ItemStack(Blocks.COBWEB, 1), new ItemStack(
                IUItem.upgrademodule.getStack(21), 1
        ), 4000000);

        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.perBatChargeCrystal.getItem(), 1),
                new ItemStack(IUItem.upgrademodule.getStack(25)),
                4000000
        );

        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                IUItem.module1,
                new ItemStack(IUItem.upgrademodule.getStack(0)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                IUItem.module2,
                new ItemStack(IUItem.upgrademodule.getStack(1)),
                2500000
        );

        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.alloysdoubleplate.getStack(8)),
                new ItemStack(IUItem.upgrademodule.getStack(2)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.alloysdoubleplate.getStack(0)),
                new ItemStack(IUItem.upgrademodule.getStack(3)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.alloysdoubleplate.getStack(4)),
                new ItemStack(IUItem.upgrademodule.getStack(4)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.radiationresources.getStack(1), 4),
                new ItemStack(IUItem.upgrademodule.getStack(5)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.radiationresources.getStack(2), 4),
                new ItemStack(IUItem.upgrademodule.getStack(6)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),

                PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE),
                new ItemStack(IUItem.upgrademodule.getStack(7)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.LONG_WATER_BREATHING),
                new ItemStack(IUItem.upgrademodule.getStack(8)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.LONG_SWIFTNESS),
                new ItemStack(IUItem.upgrademodule.getStack(9)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.alloysdoubleplate.getStack(6)),
                new ItemStack(IUItem.upgrademodule.getStack(10)),
                2500000
        );

        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.LONG_STRENGTH),
                new ItemStack(IUItem.upgrademodule.getStack(11)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.compressIridiumplate.getItem()),
                new ItemStack(IUItem.upgrademodule.getStack(12)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.radiationresources.getStack(3), 2),
                new ItemStack(IUItem.upgrademodule.getStack(13)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.SWIFTNESS),
                new ItemStack(IUItem.upgrademodule.getStack(14)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                IUItem.module3,
                new ItemStack(IUItem.upgrademodule.getStack(15)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.energy_crystal.getItem(), 1),
                new ItemStack(IUItem.upgrademodule.getStack(16)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.core.getStack(4)),
                new ItemStack(IUItem.upgrademodule.getStack(40)),
                3500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.core.getStack(7)),
                new ItemStack(IUItem.upgrademodule.getStack(41)),
                15000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.core.getStack(3)),
                new ItemStack(IUItem.upgrademodule.getStack(42)),
                3000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.upgrademodule.getStack(25)),
                new ItemStack(IUItem.upgrademodule.getStack(43)),
                10000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.purifier.getItem(), 1),
                new ItemStack(IUItem.upgrademodule.getStack(44)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.electric_treetap.getItem(), 1),
                new ItemStack(IUItem.upgrademodule.getStack(45)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.electric_wrench.getItem(), 1),
                new ItemStack(IUItem.upgrademodule.getStack(46)),
                15000000
        );
        addrecipe(IUItem.module1, IUItem.module1, IUItem.genmodule, 7500000);
        addrecipe(
                IUItem.genmodule,
                IUItem.genmodule,
                IUItem.genmodule1,
                10000000
        );
        addrecipe(
                IUItem.module2,
                IUItem.module2,
                IUItem.gennightmodule,
                7500000
        );
        addrecipe(
                IUItem.gennightmodule,
                IUItem.gennightmodule,
                IUItem.gennightmodule1,
                10000000
        );
        addrecipe(
                IUItem.module3,
                IUItem.module3,
                IUItem.storagemodule,
                7500000
        );
        addrecipe(
                IUItem.storagemodule,
                IUItem.storagemodule,
                IUItem.storagemodule1,
                10000000
        );
        addrecipe(
                IUItem.module4,
                IUItem.module4,
                IUItem.outputmodule,
                7500000
        );
        addrecipe(
                IUItem.outputmodule,
                IUItem.outputmodule,
                IUItem.outputmodule1,
                10000000
        );
        addrecipe(
                new ItemStack(IUItem.entitymodules.getStack(1), 1),
                new ItemStack(IUItem.entitymodules.getStack(1), 1),
                new ItemStack(IUItem.spawnermodules.getStack(6)),
                20000000
        );
        addrecipe(
                new ItemStack(IUItem.spawnermodules.getStack(6)),
                new ItemStack(IUItem.spawnermodules.getStack(6)),
                new ItemStack(IUItem.spawnermodules.getStack(7)),
                20000000
        );

        addrecipe(
                IUItem.phase_module,
                IUItem.phase_module,
                IUItem.phase_module1,
                7500000
        );
        addrecipe(
                IUItem.phase_module1,
                IUItem.phase_module1,
                IUItem.phase_module2,
                10000000
        );
        addrecipe(
                IUItem.moonlinse_module,
                IUItem.moonlinse_module,
                IUItem.moonlinse_module1,
                7500000
        );
        addrecipe(
                IUItem.moonlinse_module1,
                IUItem.moonlinse_module1,
                IUItem.moonlinse_module2,
                10000000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(Blocks.LAPIS_BLOCK, 1),
                new ItemStack(IUItem.upgrademodule.getStack(26)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(Blocks.REDSTONE_BLOCK, 1),
                new ItemStack(IUItem.upgrademodule.getStack(27)),
                2500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.module9.getStack(0), 1),
                new ItemStack(IUItem.upgrademodule.getStack(28)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.NIGHT_VISION),
                new ItemStack(IUItem.upgrademodule.getStack(29)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                EnchantedBookItem.createForEnchantment(new EnchantmentInstance(IUCore.registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.THORNS), 1)),
                new ItemStack(IUItem.upgrademodule.getStack(30)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.spawnermodules.getStack(5)),
                new ItemStack(IUItem.upgrademodule.getStack(31)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                PotionContents.createItemStack(Items.POTION, Potions.STRONG_HARMING),

                new ItemStack(IUItem.upgrademodule.getStack(32)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                EnchantedBookItem.createForEnchantment(new EnchantmentInstance(IUCore.registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PROJECTILE_PROTECTION), 1)),
                new ItemStack(IUItem.upgrademodule.getStack(33)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                EnchantedBookItem.createForEnchantment(new EnchantmentInstance(IUCore.registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FEATHER_FALLING), 1)),
                new ItemStack(IUItem.upgrademodule.getStack(34)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.machines_base.getItem(2)),
                new ItemStack(IUItem.upgrademodule.getStack(35)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(IUItem.machines_base1.getItem(9)),
                new ItemStack(IUItem.upgrademodule.getStack(36)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                "c:doubleplate/Invar",
                new ItemStack(IUItem.upgrademodule.getStack(37)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                new ItemStack(Items.SALMON),
                new ItemStack(IUItem.upgrademodule.getStack(38)),
                1500000
        );
        addrecipe(
                new ItemStack(IUItem.module_schedule.getItem(), 1),
                getBlockStack(BlockBaseMachine3Entity.generator_iu),
                new ItemStack(IUItem.upgrademodule.getStack(39), 1),
                1500000
        );

    }

    public ItemStack getBlockStack(MultiBlockEntity block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }

    @Override
    public ContainerMenuBaseDoubleMolecular getGuiContainer(Player entityPlayer) {
        return new ContainerMenuBaseDoubleMolecular(entityPlayer, this);
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, redstoneMode);
            EncoderHandler.encode(packet, energy, false);
            EncoderHandler.encode(packet, output_stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            redstoneMode = (byte) DecoderHandler.decode(customPacketBuffer);
            energy.onNetworkUpdate(customPacketBuffer);
            output_stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);

            this.bakedModel = Minecraft.getInstance().getItemRenderer().getModel(
                    output_stack,
                    this.getWorld(),
                    null, 0
            );
            this.transformedModel = ClientHooks.handleCameraTransforms(new PoseStack(),
                    this.bakedModel,
                    GROUND,
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
            queue = (boolean) DecoderHandler.decode(customPacketBuffer);
            redstoneMode = (byte) DecoderHandler.decode(customPacketBuffer);
            perenergy = (double) DecoderHandler.decode(customPacketBuffer);
            differenceenergy = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, queue);
            EncoderHandler.encode(packet, redstoneMode);
            EncoderHandler.encode(packet, perenergy);
            EncoderHandler.encode(packet, differenceenergy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public String getInventoryName() {
        return "Molecular Transformer";
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);


        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
        this.queue = nbttagcompound.getBoolean("queue");
        this.progress = nbttagcompound.getDouble("progress");

    }

    public void onLoaded() {
        super.onLoaded();
        if (!getLevel().isClientSide) {
            inputSlot.load();
            this.setOverclockRates();
            this.onUpdate();
            new PacketUpdateFieldTile(this, "redstoneMode", this.redstoneMode);

        }

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.molecular.getSoundEvent();
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putByte("redstoneMode", this.redstoneMode);
        nbttagcompound.putDouble("progress", this.progress);
        nbttagcompound.putBoolean("queue", this.queue);
        return nbttagcompound;

    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player entityPlayer, ContainerMenuBase<?> isAdmin) {
        return new ScreenDoubleMolecularTransformer((ContainerMenuBaseDoubleMolecular) isAdmin);
    }


    public void updateTileServer(Player player, double event) {
        if (event == 0) {
            this.redstoneMode = (byte) (this.redstoneMode + 1);
            if (this.redstoneMode >= 8) {
                this.redstoneMode = 0;
            }
            new PacketUpdateFieldTile(this, "redstoneMode", this.redstoneMode);
        }
        if (event == -1) {
            this.redstoneMode = (byte) (this.redstoneMode - 1);
            if (this.redstoneMode < 0) {
                this.redstoneMode = 7;
            }
            new PacketUpdateFieldTile(this, "redstoneMode", this.redstoneMode);
        }
        if (event == 1) {
            this.queue = !this.queue;
            if (this.need) {
                this.queue = false;
            }
            this.setOverclockRates();
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("redstoneMode")) {
            try {
                this.redstoneMode = (byte) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("output")) {
            try {
                this.output_stack = (ItemStack) DecoderHandler.decode(is);
                if (!output_stack.isEmpty()) {
                    this.bakedModel = Minecraft.getInstance().getItemRenderer().getModel(
                            output_stack,
                            this.getWorld(),
                            null, 0
                    );
                    this.transformedModel = ClientHooks.handleCameraTransforms(new PoseStack(),
                            this.bakedModel,
                            GROUND,
                            false
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BakedModel getTransformedModel() {
        return transformedModel;
    }

    public void setChanged() {
        super.setChanged();
        if (!level.isClientSide) {
            setOverclockRates();
        }
    }

    public void operate(MachineRecipe output) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult);
        if (!this.inputSlot.continue_process(this.output)) {
            getOutput();
            if (!this.inputSlot.continue_process(this.output)) {
                getOutput();
                if (!this.inputSlot.get(0).isEmpty() || !this.inputSlot.get(1).isEmpty()) {
                    if (!this.inputSlot.get(0).isEmpty()) {
                        final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                this.inputSlot.getRecipe(),
                                this.inputSlot.getRecipe_list(),
                                false,
                                Arrays.asList(
                                        this.inputSlot.get(0),
                                        this.inputSlot.get(0)
                                )
                        );
                        this.need_put_check = recipe1 != null;
                    } else {
                        final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                this.inputSlot.getRecipe(),
                                this.inputSlot.getRecipe_list(),
                                false,
                                Arrays.asList(
                                        this.inputSlot.get(1),
                                        this.inputSlot.get(1)
                                )
                        );
                        this.need_put_check = recipe1 != null;
                    }
                } else {
                    this.need_put_check = false;
                }
            }
        }
    }

    public void operate(MachineRecipe output, int size) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult, size);
        if (!this.inputSlot.continue_process(this.output)) {
            getOutput();
            if (!this.inputSlot.get(0).isEmpty() || !this.inputSlot.get(1).isEmpty()) {
                if (!this.inputSlot.get(0).isEmpty()) {
                    final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                            this.inputSlot.getRecipe(),
                            this.inputSlot.getRecipe_list(),
                            false,
                            Arrays.asList(
                                    this.inputSlot.get(0),
                                    this.inputSlot.get(0)
                            )
                    );
                    this.need_put_check = recipe1 != null;
                } else {
                    final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                            this.inputSlot.getRecipe(),
                            this.inputSlot.getRecipe_list(),
                            false,
                            Arrays.asList(
                                    this.inputSlot.get(1),
                                    this.inputSlot.get(1)
                            )
                    );
                    this.need_put_check = recipe1 != null;
                }
            } else {
                this.need_put_check = false;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {
        if (this.outputSlot.canAdd(processResult)) {
            this.inputSlot.consume();
            this.outputSlot.add(processResult);
        }
    }

    public void operateOnce(List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {
            if (this.outputSlot.canAdd(processResult)) {
                this.inputSlot.consume();
                this.outputSlot.add(processResult);
            }
        }
    }

    public void setOverclockRates() {


        MachineRecipe output = getOutput();

        this.output = output;
        this.onUpdate();
        if (!this.queue) {
            if (inputSlot.isEmpty()) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy"));
            } else {
                this.energy.setCapacity(0);
            }
        } else {

            if (inputSlot.isEmpty()) {
                this.energy.setCapacity(0);
            } else if (output != null) {

                int size;
                int size2;
                ItemStack output1 = this.output.getRecipe().output.items.get(0);
                size = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();
                size2 = this.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                size = (int) Math.floor((float) this.inputSlot.get(0).getCount() / size);
                size2 = (int) Math.floor((float) this.inputSlot.get(1).getCount() / size2);
                size = Math.min(size, size2);
                int size1 = !this.outputSlot.isEmpty()
                        ? (64 - this.outputSlot.get(0).getCount()) / output1.getCount()
                        : 64 / output1.getCount();
                size = Math.min(size1, size);
                size = Math.min(output1.getMaxStackSize(), size);
                this.size_recipe = size;
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy") * size);
            } else {
                this.energy.setCapacity(0);
            }
        }


    }

    public void updateEntityServer() {
        super.updateEntityServer();

        MachineRecipe output = this.output;
        if (this.need_put_check) {
            if (!this.inputSlot.get(0).isEmpty()) {
                if (this.inputSlot.get(0).getCount() > 1) {
                    int count = this.inputSlot.get(0).getCount() / 2;
                    this.inputSlot.get(0).shrink(count);
                    ItemStack stack = this.inputSlot.get(0).copy();
                    stack.setCount(count);
                    this.inputSlot.set(1, stack);
                } else if (!this.inputSlot.get(1).isEmpty()) {
                    if (this.inputSlot.get(1).getCount() > 1) {
                        int count = this.inputSlot.get(1).getCount() / 2;
                        this.inputSlot.get(1).shrink(count);
                        ItemStack stack = this.inputSlot.get(1).copy();
                        stack.setCount(count);
                        this.inputSlot.set(0, stack);

                    }
                }
            }

        }
        if (!queue) {
            if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items)) {

                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                if (!this.getActive()) {
                    initiate(0);
                    setActive(true);
                    setOverclockRates();
                }


                this.progress = this.energy.getEnergy();
                double k = this.progress;
                this.guiProgress = (Math.ceil(k) / this.energy.getCapacity());
                if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                    operate(output);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    initiate(2);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    initiate(1);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }

        } else {
            if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items)) {
                if (!this.getActive()) {
                    initiate(2);
                    setActive(true);
                    setOverclockRates();
                }
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();

                int size = 0;
                int size2 = 0;
                ItemStack output1;
                output1 = this.output.getRecipe().getOutput().items.get(0);

                final List<ItemStack> list = this.output.getRecipe().input.getInputs().get(0).getInputs();
                boolean is = false;
                for (ItemStack stack : list) {
                    if (stack.is(this.inputSlot.get(0).getItem())) {
                        is = true;
                        size = stack.getCount();
                        size2 = this.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                        break;
                    }
                }
                if (!is) {
                    size = this.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                    size2 = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();

                }
                size = (int) Math.floor((float) this.inputSlot.get(0).getCount() / size);
                size2 = (int) Math.floor((float) this.inputSlot.get(1).getCount() / size2);
                size = Math.min(size, size2);
                int size1 = !this.outputSlot.isEmpty()
                        ? (64 - this.outputSlot.get(0).getCount()) / output1.getCount()
                        : 64 / output1.getCount();
                size = Math.min(size1, size);
                size = Math.min(output1.getMaxStackSize(), size);
                if (size != this.size_recipe) {
                    this.setOverclockRates();
                }
                this.progress = this.energy.getEnergy();
                double k = this.progress;
                double p = (k / (this.energy.getCapacity()));
                if (p <= 1) {
                    this.guiProgress = p;
                }
                if (p > 1) {
                    this.guiProgress = 1;
                }
                if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                    operate(output, size);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    initiate(2);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    initiate(1);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                this.setActive(false);
            }
        }
        if (this.getActive() && output == null) {
            this.setActive(false);
        }
        if (!this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlot.process();
        if (this.output != null) {
            output_stack = this.output.getRecipe().output.items.get(0);
        } else {
            output_stack = new ItemStack(Items.AIR);
        }

        return this.output;
    }

    public double getProgress() {
        return Math.min(this.guiProgress, 1);
    }


    public String getStartSoundFile() {
        return "Machines/molecular.ogg";
    }


    @Override
    public void onUpdate() {
        for (int i = 0; i < this.inputSlot.size(); i++) {
            if (this.inputSlot.get(i).getItem() instanceof EnchantedBookItem) {
                this.need = true;
                return;
            }
            if (this.inputSlot.get(i).getItem() instanceof PotionItem) {
                this.need = true;
                return;
            }
        }
        this.need = false;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        if (this.output != null) {
            output_stack = this.output.getRecipe().output.items.get(0);
        } else {
            output_stack = new ItemStack(Items.AIR);
        }

        new PacketUpdateFieldTile(this, "output", this.output_stack);
        this.setOverclockRates();

    }

    @Override
    public int getMode() {
        return this.redstoneMode;
    }

}
