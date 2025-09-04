package com.denfop.integration.jade;

import com.denfop.Constants;
import com.denfop.blocks.Deposits;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

public class DepositsComponentProvider implements IBlockComponentProvider {
    public static final DepositsComponentProvider INSTANCE = new DepositsComponentProvider();


    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Constants.MOD_ID, "deposists_provider_blockentity");
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlock() instanceof Deposits) {
            Deposits deposits = (Deposits) blockAccessor.getBlock();
            final List<String> stringList = deposits.getInformationFromMeta();
            for (String s : stringList) {
                iTooltip.add(Component.literal(s));
            }
        }
    }
}
