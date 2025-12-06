/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.world.item.crafting.RecipeManager
 */
package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;

@Deprecated(forRemoval=true, since="1.21.5")
public class RecipesUpdatedEvent
extends BalmEvent {
    private final RecipeManager recipeManager;
    private final RegistryAccess registryAccess;

    public RecipesUpdatedEvent(RecipeManager recipeManager, RegistryAccess registryAccess) {
        this.recipeManager = recipeManager;
        this.registryAccess = registryAccess;
    }

    public RecipeManager getRecipeManager() {
        return this.recipeManager;
    }

    public RegistryAccess getRegistryAccess() {
        return this.registryAccess;
    }
}

