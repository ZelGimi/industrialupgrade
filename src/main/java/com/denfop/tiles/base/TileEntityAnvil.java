package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.register.RegisterOreDictionary;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TileEntityAnvil extends TileEntityInventory implements IUpdateTick, IHasRecipe, IAudioFixer {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(0, 0.0D, -1, 1, 1.0D,
            2
    ));
    private static final List<AxisAlignedBB> aabbs1 = Collections.singletonList(new AxisAlignedBB(-1, 0.0D, 0, 2, 1.0D,
            1
    ));
    public final InvSlotRecipes inputSlotA;
    public final InvSlotOutput outputSlot;
    public int progress;
    public MachineRecipe output;
    public int durability = 300;

    public Map<UUID,Double> data = PrimitiveHandler.getPlayersData(EnumPrimitive.ANVIL);
    public TileEntityAnvil() {

        this.inputSlotA = new InvSlotRecipes(this, "anvil", this){
            @Override
            public boolean accepts(final ItemStack itemStack, final int index) {
                if (index == 4)
                    return super.accepts(itemStack,0);
                return false;
            }
        };
        this.progress = 0;
        this.outputSlot = new InvSlotOutput(this, 1);
        Recipes.recipes.addInitRecipes(this);
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        if (!(facing == 4 || facing == 5)) {
            return aabbs1;
        }
        return aabbs;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        tooltip.add(Localization.translate("iu.primal_repair"));
        tooltip.add(Localization.translate("primitive_rcm.info"));
        tooltip.add(Localization.translate("primitive_use.info") + IUItem.ForgeHammer.getItemStackDisplayName());
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.anvil;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockAnvil.block_anvil;
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

    @Override
    public List<ItemStack> getSelfDrops(final int fortune, final boolean wrench) {
        List<ItemStack> drop = super.getSelfDrops(fortune, wrench);
        ItemStack stack = drop.get(0);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setInteger("durability", durability);
        return drop;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        if (nbt.hasKey("durability")) {
            durability = nbt.getInteger("durability");
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
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
                inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("slot1")) {
            try {
                outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(is))).writeToNbt(new NBTTagCompound()));
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
            inputSlotA.put(0, ItemStack.EMPTY);
        }
        if (name.equals("slot2")) {
            outputSlot.put(0, ItemStack.EMPTY);
        }
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            inputSlotA.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
            outputSlot.readFromNbt(((InvSlot) (DecoderHandler.decode(customPacketBuffer))).writeToNbt(new NBTTagCompound()));
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
    public boolean onSneakingActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (durability >= 0 && durability < 300 && stack.getItem() == IUItem.iuingot && stack.getItemDamage() == 10) {
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
        return super.onSneakingActivated(player, hand, side, hitX, hitY, hitZ);
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
        ItemStack stack = player.getHeldItem(hand);

        if (durability > 0 && (stack.getItem() == IUItem.ForgeHammer || stack.getItem() == IUItem.ObsidianForgeHammer) && this.output != null && this.outputSlot.canAdd(
                this.output.getRecipe().output.items.get(
                        0))) {
            progress += 10;
            this.getCooldownTracker().setTick(10);
            if (stack.getItem() == IUItem.ObsidianForgeHammer) {
                progress += 10;
            }
            progress += (int) (data.getOrDefault(player.getUniqueID(),0.0)/5D);
            if (!this.getWorld().isRemote) {
                this.initiate(0);
            }
            if (progress >= 100) {
                this.progress = 0;
                if (!this.getWorld().isRemote)
                PrimitiveHandler.addExperience(EnumPrimitive.ANVIL,0.5,player.getUniqueID());
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
                player.setHeldItem(hand, stack.getItem().getContainerItem(stack));
                this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                this.inputSlotA.consume(0, this.output.getRecipe().input.getInputs().get(0).getAmount());
                if (this.inputSlotA.isEmpty() || this.outputSlot.get().getCount() >= 64) {
                    this.output = null;

                }
                if (!world.isRemote) {
                    new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    new PacketUpdateFieldTile(this, "slot1", this.outputSlot);
                    new PacketUpdateFieldTile(this, "durability", this.durability);


                }
            }

            return this.getWorld().isRemote;
        } else {
            if (!stack.isEmpty()) {
                if (this.inputSlotA.get(0).isEmpty() && this.inputSlotA.accepts(stack, 4)) {
                    this.inputSlotA.put(0, stack.copy());
                    stack.setCount(0);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                } else if (!this.inputSlotA.get(0).isEmpty() && this.inputSlotA.get(0).isItemEqual(stack)) {
                    int minCount = 64 - this.inputSlotA.get(0).getCount();
                    minCount = Math.min(stack.getCount(), minCount);
                    this.inputSlotA.get(0).grow(minCount);
                    stack.grow(-minCount);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot", this.inputSlotA);
                    }
                    return true;
                }
            } else {
                if (!outputSlot.isEmpty()) {
                    if (!world.isRemote) {
                        ModUtils.dropAsEntity(world, pos, outputSlot.get(), player);
                    }
                    outputSlot.put(0, ItemStack.EMPTY);
                    if (!world.isRemote) {
                        new PacketUpdateFieldTile(this, "slot2", false);
                    }
                    return true;
                } else {
                    if (!inputSlotA.isEmpty()) {
                        if (!world.isRemote) {
                            ModUtils.dropAsEntity(world, pos, inputSlotA.get(), player);
                        }
                        inputSlotA.put(0, ItemStack.EMPTY);
                        this.output = null;
                        if (!world.isRemote) {
                            new PacketUpdateFieldTile(this, "slot3", false);
                        }
                        return true;
                    }
                }
            }
        }


        return false;
    }

    @Override
    public void onUpdate() {

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.durability = nbttagcompound.getInteger("durability");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("durability", durability);
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
        for (String s : RegisterOreDictionary.list_string) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("ingot" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("plate" + s))
                    )
            );
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("plate" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("casing" + s), 2)
                    )
            );
        }
        for (String s : RegisterOreDictionary.list_baseore1) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("ingot" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("plate" + s))
                    )
            );
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("plate" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("casing" + s), 2)
                    )
            );
        }
        for (String s : RegisterOreDictionary.list_string1) {
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("ingot" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("plate" + s))
                    )
            );
            Recipes.recipes.addRecipe(
                    "anvil",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput("plate" + s, 1)

                            ),
                            new RecipeOutput(null, OreDictionary.getOres("casing" + s), 2)
                    )
            );
        }
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotIron", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateIron"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 504), 1)

                        ),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, 501))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("plateIron", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("casingIron"), 2)
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotGold", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateGold"))
                )
        );

        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotLead", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateLead"))
                )
        );

        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotTin", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateTin"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("plateTin", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("casingTin"), 2)
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("plateBronze", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("casingBronze"), 2)
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotOsmium", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateOsmium"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotTantalum", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateTantalum"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotCadmium", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateCadmium"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotSteel", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateSteel"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotBronze", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateBronze"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("ingotCopper", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("plateCopper"))
                )
        );
        Recipes.recipes.addRecipe(
                "anvil",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("plateCopper", 1)

                        ),
                        new RecipeOutput(null, OreDictionary.getOres("casingCopper"), 2)
                )
        );
    }

    @Override
    public EnumTypeAudio getType() {
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
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 0.2F, 1);
        }
    }

    @Override
    public boolean getEnable() {
        return true;
    }

}
