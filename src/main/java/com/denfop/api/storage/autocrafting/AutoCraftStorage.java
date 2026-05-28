package com.denfop.api.storage.autocrafting;

public record AutoCraftStorage(SameStack stack, int amount) {


    @Override
    public boolean equals(Object o) {
        return (this == o) || (o instanceof AutoCraftStorage other && stack.equals(other.stack));
    }

    @Override
    public int hashCode() {
        return stack.hashCode();
    }

    public SameStack getSameStack() {
        return stack;
    }

    public int getAmount() {
        return amount;
    }
}
