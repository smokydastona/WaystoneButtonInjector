/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.common.proxy;

import java.lang.reflect.InvocationTargetException;
import net.blay09.mods.balm.api.proxy.PlatformProxy;

public class PlatformProxyImpl<T>
implements PlatformProxy<T> {
    private final String platform;
    private String clazzName;

    public PlatformProxyImpl(String platform) {
        this.platform = platform;
    }

    @Override
    public PlatformProxy<T> with(String platform, String clazzName) {
        if (this.platform.equals(platform)) {
            this.clazzName = clazzName;
        }
        return this;
    }

    @Override
    public T build() {
        try {
            return (T)Class.forName(this.clazzName).getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate platform proxy " + this.clazzName, e);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate platform proxy, missing no-arg constructor in " + this.clazzName, e);
        }
    }
}

