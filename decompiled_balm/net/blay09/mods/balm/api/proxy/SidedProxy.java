/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.api.proxy;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.BalmEnvironment;

public class SidedProxy<T> {
    private final Supplier<BalmEnvironment> environmentResolver;
    private final String commonName;
    private final String clientName;
    private T proxy;

    public SidedProxy(Supplier<BalmEnvironment> environmentResolver, String commonName, String clientName) {
        this.environmentResolver = environmentResolver;
        this.commonName = commonName;
        this.clientName = clientName;
    }

    public Supplier<T> buildLazily() {
        return new Supplier<T>(){
            private T instance;

            @Override
            public T get() {
                if (this.instance == null) {
                    this.instance = SidedProxy.this.build();
                }
                return this.instance;
            }
        };
    }

    public T build() {
        String classNameForEnvironment = switch (this.environmentResolver.get()) {
            default -> throw new IncompatibleClassChangeError();
            case BalmEnvironment.CLIENT -> this.clientName;
            case BalmEnvironment.SERVER -> this.commonName;
        };
        try {
            this.proxy = Class.forName(classNameForEnvironment).getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return this.proxy;
    }

    @Deprecated(forRemoval=true, since="1.22")
    public T get() {
        if (this.proxy == null) {
            this.proxy = this.build();
        }
        return this.proxy;
    }
}

