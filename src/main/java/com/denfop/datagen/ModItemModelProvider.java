package com.denfop.datagen;

import com.denfop.api.crop.Crop;
import com.denfop.api.crop.CropNetwork;
import com.denfop.items.crop.ItemCrops;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, "industrialupgrade", existingFileHelper);
    }

    @Override
    protected void registerModels() {


        //   simple(IUItem.crops.getRegistryObject(0));

    }

    private void simple(RegistryObject<ItemCrops> item) {
        ItemModelBuilder builder = getBuilder(item.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("industrialupgrade:item/default"))
                .texture("layer0", "industrialupgrade:item/crop_seed");

        for (Map.Entry<Integer, Crop> entry : CropNetwork.instance.getCropMap().entrySet()) {
            int id = entry.getKey();
            Crop crop = entry.getValue();
            List<ItemStack> drops = crop.getDrop();

            if (drops.isEmpty()) {
                continue;
            }

            ResourceLocation dropId = ForgeRegistries.ITEMS.getKey(drops.get(0).getItem());
            if (dropId == null) {
                continue;
            }

            builder.override()
                    .predicate(new ResourceLocation("id"), id)
                    .model(new ModelFile.UncheckedModelFile(dropId));
        }


    }

}
