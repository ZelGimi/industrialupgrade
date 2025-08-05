package com.denfop.api.crafting;

import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputItemStack;

import java.io.IOException;

public class PartRecipe {

    private final String index;
    private final IInputItemStack input;

    public PartRecipe(String index, IInputItemStack input) {
        this.index = index;
        this.input = input;
    }

    public IInputItemStack getInput() {
        return input;
    }

    public String getIndex() {
        return index;
    }

    public void encode(CustomPacketBuffer customPacketBuffer) {
        try {
            EncoderHandler.encode(customPacketBuffer,index);
            EncoderHandler.encode(customPacketBuffer,input.writeNBT());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
