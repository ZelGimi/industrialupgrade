
package com.denfop.integration.crafttweaker;

import crafttweaker.IAction;

public abstract class BaseAction implements IAction {
    protected final String name;
    protected boolean success = false;

    protected BaseAction(String name) {
        this.name = name;
    }

    protected String getRecipeInfo() {
        return "Unknown item";
    }

    public String describe() {
        return String.format("Altering %s Recipe(s) for %s", this.name, this.getRecipeInfo());
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof BaseAction)) {
            return false;
        } else {
            BaseAction u = (BaseAction)obj;
            return this.name.equals(u.name);
        }
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
