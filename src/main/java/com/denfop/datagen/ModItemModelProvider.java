package com.denfop.datagen;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static com.denfop.DataBlock.objectsBlock;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, "industrialupgrade", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<?> registryObject : objectsBlock) {
            Block block = (Block) registryObject.get();
            try {
             //   simple((RegistryObject<Block>) registryObject);
            }catch (Exception e){};
        }
    }
    private void simple(RegistryObject<Block> item) {
        this.simple(item, "item/" + item.getId().getPath());
    }
    private <T extends Block> ItemModelBuilder simple(RegistryObject<T> item, String texturePath) {
        ModelResourceLocation model = BlockModelShaper.stateToModelLocation(item.get().defaultBlockState());

        return this.getBuilder(item.getId().getPath()).parent(new ItemModelBuilder(new ResourceLocation(model.getNamespace(),"block/"+model.getPath()), this.existingFileHelper));
    }

}
