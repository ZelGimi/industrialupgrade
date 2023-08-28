package com.denfop.tiles.mechanism.dual;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiEnriched;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEnrichment extends TileDoubleElectricMachine implements IHasRecipe {

    public TileEnrichment() {
        super(1, 300, 1, EnumDoubleElectricMachine.ENRICH);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addenrichment(ItemStack container, ItemStack fill, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "enrichment",
                new BaseMachineRecipe(
                        new Input(input.getInput(container), input.getInput(fill)),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addenrichment(ItemStack container, String fill, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("enrichment", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(null, output)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.enrichment;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.enrichment.getSoundEvent();
    }

    public void init() {
        addenrichment(
                new ItemStack(IUItem.toriy),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(IUItem.radiationresources, 1, 4)
        );
        addenrichment(
                new ItemStack(IUItem.preciousgem, 4, 1),
                "blockCobalt",
                new ItemStack(IUItem.crafting_elements, 1, 269)
        );
        addenrichment(
                new ItemStack(Blocks.GLOWSTONE, 1),
                "ingotUranium",
                new ItemStack(IUItem.itemiu, 1, 0)
        );
        addenrichment(new ItemStack(IUItem.itemiu, 1, 0), IUItem.reinforcedGlass, new ItemStack(IUItem.itemiu, 2, 1));

        addenrichment(
                new ItemStack(IUItem.sunnarium, 1, 3),
                new ItemStack(IUItem.itemiu, 1, 0),
                new ItemStack(IUItem.sunnarium, 1, 0)
        );

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiEnriched(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return "Machines/enrichment.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
