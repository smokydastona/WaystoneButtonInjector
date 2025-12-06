/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public class DeferredObject<T> {
    private final ResourceLocation identifier;
    private final Supplier<T> supplier;
    private final Supplier<Boolean> canResolveFunc;
    protected T object;

    protected DeferredObject(ResourceLocation identifier) {
        this(identifier, () -> null, () -> false);
    }

    public DeferredObject(ResourceLocation identifier, Supplier<T> supplier) {
        this(identifier, supplier, () -> false);
    }

    public DeferredObject(ResourceLocation identifier, Supplier<T> supplier, Supplier<Boolean> canResolveFunc) {
        this.identifier = identifier;
        this.supplier = supplier;
        this.canResolveFunc = canResolveFunc;
    }

    public static <T> DeferredObject<T> of(ResourceLocation identifier, T instance) {
        return new DeferredObject<Object>(identifier, () -> instance).resolveImmediately();
    }

    protected void set(T object) {
        this.object = object;
    }

    public boolean canResolve() {
        return this.canResolveFunc.get();
    }

    public T resolve() {
        if (this.object == null) {
            this.object = this.supplier.get();
        }
        return this.object;
    }

    public T get() {
        if (this.object == null) {
            if (this.canResolve()) {
                return this.resolve();
            }
            throw new IllegalStateException("Tried to access deferred object before it was resolved.");
        }
        return this.object;
    }

    public DeferredObject<T> resolveImmediately() {
        this.resolve();
        return this;
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }
}

