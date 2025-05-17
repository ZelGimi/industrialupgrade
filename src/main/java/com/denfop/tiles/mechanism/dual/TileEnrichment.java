package com.denfop.tiles.mechanism.dual;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEnriched;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEnrichment extends TileDoubleElectricMachine implements IHasRecipe {

    public final ComponentBaseEnergy rad_energy;
    public final InvSlot input_slot;

    public TileEnrichment(BlockPos pos, BlockState state) {
        super(1, 300, 1, EnumDoubleElectricMachine.ENRICH, BlockBaseMachine1.enrichment, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.rad_energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.RADIATION, this, 10000));
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((TileEnrichment) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEnrichment) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
    }

    public static void addenrichment(ItemStack container, ItemStack fill, ItemStack output, int rad_amount) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("rad_amount", rad_amount);
        Recipes.recipes.addRecipe(
                "enrichment",
                new BaseMachineRecipe(
                        new Input(input.getInput(container), input.getInput(fill)),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void addenrichment(ItemStack container, String fill, ItemStack output, int rad_amount) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("rad_amount", rad_amount);
        Recipes.recipes.addRecipe("enrichment", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.enrichment;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine.getBlock(getTeBlock().getId());
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.enrichment.getSoundEvent();
    }

    public void init() {
        addenrichment(
                new ItemStack(IUItem.toriy.getItem()),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(IUItem.radiationresources.getItemFromMeta(4), 14), 25
        );
        addenrichment(
                new ItemStack(IUItem.preciousgem.getItemFromMeta(1), 4),
                "forge:storage_blocks/Cobalt",
                new ItemStack(IUItem.crafting_elements.getItemFromMeta(269), 1), 200
        );
        addenrichment(
                new ItemStack(Blocks.GLOWSTONE, 1),
                "forge:ingots/Uranium",
                new ItemStack(IUItem.itemiu.getItemFromMeta(0), 1), 10
        );
        addenrichment(new ItemStack(IUItem.itemiu.getItemFromMeta(0), 1), IUItem.reinforcedGlass, new ItemStack(IUItem.itemiu.getItemFromMeta(1), 2), 10);

        addenrichment(
                new ItemStack(IUItem.sunnarium.getItemFromMeta(3), 1),
                new ItemStack(IUItem.itemiu.getItemFromMeta(0), 1),
                new ItemStack(IUItem.sunnarium.getStack(0), 1), 20
        );
        addenrichment(
                new ItemStack(IUItem.itemiu.getItemFromMeta(0)),
                IUItem.advancedAlloy,
                new ItemStack(IUItem.crafting_elements.getStack(453), 1), 20
        );
        addenrichment(
                new ItemStack(Items.REDSTONE, 4),
                new ItemStack(IUItem.itemiu.getItemFromMeta(0), 1),
                new ItemStack(IUItem.crafting_elements.getStack(445), 1), 40
        );

        addenrichment(
                new ItemStack(Items.STRING, 2),
                new ItemStack(IUItem.nuclear_res.getStack(5), 1),
                new ItemStack(IUItem.crafting_elements.getStack(444), 1), 25
        );

        addenrichment(
                new ItemStack(Items.LAPIS_LAZULI, 8),
                new ItemStack(IUItem.Plutonium),
                new ItemStack(IUItem.crafting_elements.getStack(446), 1), 50
        );

        addenrichment(
                new ItemStack(IUItem.iudust.getStack(31), 2),
                new ItemStack(IUItem.iudust.getStack(27), 8),
                new ItemStack(IUItem.iudust.getStack(40), 1), 50
        );
        addenrichment(
                new ItemStack(IUItem.nuclear_res.getStack(16), 8),
                new ItemStack(IUItem.purifiedcrushed.getStack(24), 1),
                new ItemStack(IUItem.iudust.getStack(78), 1), 500
        );
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiEnriched((ContainerDoubleElectricMachine) isAdmin);
    }


}
