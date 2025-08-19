package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.effects.EffectsRegister;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class TileEntityAnvil extends TileEntityInventory implements IUpdateTick, IHasRecipe, IAudioFixer {

    public static final List<String> list_string = itemNames();
    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, -1, 1, 1.0D,
            2
    ));
    private static final List<AABB> aabbs1 = Collections.singletonList(new AABB(-1, 0.0D, 0, 2, 1.0D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int durability = 300;
    public Map<UUID, Double> data;

    public TileEntityAnvil(BlockPos pos, BlockState state) {
        super(BlockAnvil.block_anvil, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "anvil", this) {
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4) {
                    return super.accepts(itemStack, 0);
                }
                return false;
            }
        };
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    public static List<String> itemNames() {
        List<String> list = new ArrayList<>();
        list.add("Mikhail");//0
        list.add("Aluminium");//1
        list.add("Vanady");//2
        list.add("Tungsten");//3
        list.add("Invar");//4
        list.add("Caravky");//5
        list.add("Cobalt");//6
        list.add("Magnesium");//7
        list.add("Nickel");//8
        list.add("Platinum");//9
        list.add("Titanium");//10
        list.add("Chromium");//11
        list.add("Spinel");//12
        list.add("Electrum");//13
        list.add("Silver");//14
        list.add("Zinc");//15
        list.add("Manganese");//16
        list.add("Iridium");//17
        list.add("Germanium");//18
        return list;
    }

    public static List<String> itemNames7() {
        return Arrays.asList(
                "Arsenic",
                "Barium",
                "Bismuth",
                "Gadolinium",
                "Gallium",
                "Hafnium",
                "Yttrium",
                "Molybdenum",
                "Neodymium",
                "Niobium",
                "Palladium",
                "Polonium",
                "Strontium",
                "Thallium",
                "Zirconium"
        );
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.empty();
        }
        return super.getCapability(cap, facing);
    }

    public List<AABB> getAabbs(boolean forCollision) {
        if (!(facing == 4 || facing == 5)) {
            return aabbs1;
        }
        return aabbs;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair"));
        tooltip.add(Localization.translate("iu.primal_repair.info"));

        tooltip.add(Localization.translate("primitive_rcm.info"));
        tooltip.add(Localization.translate("primitive_use.info") + IUItem.ForgeHammer.getItem().getDescription().getString());

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.anvil.getBlock();
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockAnvil.block_anvil;
    }

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        ItemStack stack = drop.get(0);
        final CompoundTag nbt = ModUtils.nbt(stack);
        nbt.putInt("durability", durability);
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        final CompoundTag nbt = ModUtils.nbt(stack);
        if (nbt.contains("durability")) {
            durability = nbt.getInt("durability");
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        data = PrimitiveHandler.getPlayersData(EnumPrimitive.ANVIL);
        if (!this.getWorld().isClientSide) {
            inputSlotA.load();
            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
            new PacketUpdateFieldTile(this, "durability", this.durability);
            if (durability > 200) {
                this.setActive("");
            } else if (durability > 100) {
                this.setActive("1");
            } else if (durability > 0) {
                this.setActive("2");
            } else {
                this.setActive("3");
            }

        }
        this.output = inputSlotA.process();
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("slot")) {
            try {
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new CompoundTag()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("durability")) {
            try {
                durability = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot3")) {
            inputSlotA.set(0, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.set(0, ItemStack.EMPTY);
        }
        if (name.equals("effect")) {

            spawnItemParticles(level, pos, this.inputSlotA.get(0));
        }
    }
    @OnlyIn(Dist.CLIENT)
    private void spawnItemParticles(Level world, BlockPos pos, ItemStack stack) {
        Random rand = new Random();

        for (int i = 0; i < 1; i++) {
            double offsetX = -0.05;
            double offsetY = 0.05;
            double offsetZ = -0.05;
            world.addParticle(
                    EffectsRegister.ANVIL.get(),
                            pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
                            offsetX, offsetY, offsetZ
            );
        }
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new CompoundTag()));
            this.durability = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, inputSlotA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, outputSlot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            EncoderHandler.encode(customPacketBuffer, durability);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (durability >= 0 && durability < 300 && stack.getItem() == IUItem.iuingot.getItemFromMeta(10)) {
            durability += 50;
            if (durability > 300) {
                durability = 300;
            }
            stack.shrink(1);
            if (durability > 200) {
                this.setActive("");
            } else if (durability > 100) {
                this.setActive("1");
            } else if (durability > 0) {
                this.setActive("2");
            } else {
                this.setActive("3");
            }

            new PacketUpdateFieldTile(this, "durability", this.durability);
        }
        return super.onSneakingActivated(player, hand, side, vec3);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.getWorld().isClientSide) {

            if (durability > 0 && (stack.getItem() == IUItem.ForgeHammer.getItem() || stack.getItem() == IUItem.ObsidianForgeHammer.getItem()) && this.output != null && this.outputSlot.canAdd(
                    this.output.getRecipe().output.items.get(
                            0))) {
                progress += 10;
                new PacketUpdateFieldTile(this, "effect", true);
                this.getCooldownTracker().setTick(10);
                if (stack.getItem() == IUItem.ObsidianForgeHammer.getItem()) {
                    progress += 10;
                }
                progress += (int) (data.getOrDefault(player.getUUID(), 0.0) / 5D);
                if (!this.getWorld().isClientSide) {
                    this.initiate(0);
                }
                if (progress >= 100) {
                    this.progress = 0;
                    if (!this.getWorld().isClientSide) {
                        PrimitiveHandler.addExperience(EnumPrimitive.ANVIL, 0.5, player.getUUID());
                    }
                    durability--;
                    if (durability > 200) {
                        this.setActive("");
                    } else if (durability > 100) {
                        this.setActive("1");
                    } else if (durability > 0) {
                        this.setActive("2");
                    } else {
                        this.setActive("3");
                    }
                    player.setItemInHand(hand, stack.getItem().getCraftingRemainingItem(stack));
                    this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                    this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                    if (this.inputSlotA.isEmpty()) {
                        this.output = null;

                    }
                    if (!level.isClientSide) {
                        if (!this.inputSlotA.get(0).isEmpty()) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        } else {
                            new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                        }
                        new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                        new PacketUpdateFieldTile(this, "durability", this.durability);


                    }
                    if (WorldBaseGen.random.nextInt(100) < data.getOrDefault(player.getUUID(), 0.0) & this.output != null && this.outputSlot.canAdd(
                            this.output.getRecipe().output.items.get(
                                    0))){
                        this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                        this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                        if (this.inputSlotA.isEmpty() || this.outputSlot.get(0).getCount() >= 64) {
                            this.output = null;

                        }
                        if (!level.isClientSide) {
                            if (!this.inputSlotA.get(0).isEmpty()) {
                                new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                            } else {
                                new PacketUpdateFieldTile(this, "slot3", this.inputSlotA);
                            }
                            new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                            new PacketUpdateFieldTile(this, "durability", this.durability);


                        }
                    }
                }

                return this.getWorld().isClientSide;
            } else {
                if (!stack.isEmpty()) {
                    if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
                        this.inputSlotA.set(0, stack.copy());
                        stack.setCount(0);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        return true;
                    } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).is(stack.getItem())) {
                        int minCount = 64 - this.inputSlotA.get(0).getCount();
                        minCount = Math.min(stack.getCount(), minCount);
                        this.inputSlotA.get(0).grow(minCount);
                        stack.grow(-minCount);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                        }
                        return true;
                    }
                } else {
                    if (!outputSlot.isEmpty()) {
                        if (!level.isClientSide) {
                            ModUtils.dropAsEntity(level, pos, outputSlot.get(0));
                        }
                        outputSlot.set(0, ItemStack.EMPTY);
                        if (!level.isClientSide) {
                            new PacketUpdateFieldTile(this, "slot2", false);
                        }
                        return true;
                    } else {
                        if (!inputSlotA.isEmpty()) {
                            if (!level.isClientSide) {
                                ModUtils.dropAsEntity(level, pos, inputSlotA.get(0));
                            }
                            inputSlotA.set(0, ItemStack.EMPTY);
                            this.output = null;
                            if (!level.isClientSide) {
                                new PacketUpdateFieldTile(this, "slot3", false);
                            }
                            return true;
                        }
                    }
                }
            }

        }
        return true;
    }

    @Override
    public void onUpdate() {

    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.durability = nbttagcompound.getInt("durability");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("durability", durability);
        return nbttagcompound;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    @Override
    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        for (String s : list_string) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("forge:ingots/" + s.toLowerCase(), 1)

                            ),
                            new RecipeOutput(null, input.getInput("forge:plates/" + s.toLowerCase()).getInputs())
                    )
            );
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("forge:plates/" + s.toLowerCase(), 1)

                            ),
                            new RecipeOutput(null, input.getInput("forge:casings/" + s.toLowerCase(), 2).getInputs())
                    )
            );
        }
          for (String s : RegisterOreDictionary.list_baseore1) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("forge:ingots/" + s, 1)

                            ),
                            new RecipeOutput(null,  input.getInput("forge:plates/" + s).getInputs().get(0))
                    )
            );
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("forge:plates/" + s, 1)

                            ),
                            new RecipeOutput(null,  input.getInput("forge:casings/" + s,2).getInputs().get(0))
                    )
            );
        }
        for (String s : RegisterOreDictionary.list_string1) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("forge:ingots/" + s, 1)

                            ),
                            new RecipeOutput(null, input.getInput("forge:plates/" + s).getInputs().get(0))
                    )
            );
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("forge:plates/" + s, 1)

                            ),
                            new RecipeOutput(null,  input.getInput("forge:casings/" + s,2).getInputs().get(0))
                    )
            );
        }
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Iron", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Iron").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(504), 1), 1)

                        ),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(501), 1))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:plates/Iron", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:casings/Iron",2).getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Gold", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Gold").getInputs().get(0))
                )
        );

        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Lead", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Lead").getInputs().get(0))
                )
        );

        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Tin", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Tin").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:plates/Tin", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:casings/Tin",2).getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:plates/Bronze", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:casings/Bronze", 2).getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Osmium", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Osmium").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Tantalum", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Tantalum").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Cadmium", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Cadmium").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Steel", 1)

                        ),
                        new RecipeOutput(null, input.getInput("forge:plates/Steel").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Bronze", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:plates/Bronze").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:ingots/Copper", 1)

                        ),
                        new RecipeOutput(null, input.getInput("forge:plates/Copper").getInputs().get(0))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:plates/Copper", 1)

                        ),
                        new RecipeOutput(null,  input.getInput("forge:casings/Copper", 2).getInputs().get(0))
                )
        );
    }

    @Override
    public EnumTypeAudio getTypeAudio() {
        return EnumTypeAudio.ON;
    }

    @Override
    public void setType(final EnumTypeAudio type) {

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.molot.getSoundEvent();
    }

    @Override
    public void initiate(final int soundEvent) {
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundSource.BLOCKS, 0.2F, 1);
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

}
