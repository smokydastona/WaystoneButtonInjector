/*
 * Decompiled with CFR 0.152.
 */
package net.blay09.mods.balm.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.BalmProxy;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.common.BaseModule;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.blay09.mods.balm.common.proxy.ModProxyImpl;
import net.blay09.mods.balm.common.proxy.PlatformProxyImpl;

public abstract class CommonBalmRuntime
implements BalmRuntime {
    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList());
    private static final List<BalmModule> modules = Collections.synchronizedList(new ArrayList());
    private final Supplier<BalmProxy> proxy = this.sidedProxy("net.blay09.mods.balm.api.BalmProxy", "net.blay09.mods.balm.api.client.BalmClientProxy").buildLazily();
    private boolean ready;

    @Override
    public BalmProxy getProxy() {
        return this.proxy.get();
    }

    @Override
    public boolean isReady() {
        return this.ready;
    }

    @Override
    public void onRuntimeAvailable(Runnable callback) {
        initCallbacks.add(callback);
        if (this.isReady()) {
            callback.run();
        }
    }

    @Override
    public void registerModule(BalmModule module) {
        modules.add(module);
        this.initializeModule(module);
    }

    @Override
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return new SidedProxy(this::getEnvironment, commonName, clientName);
    }

    @Override
    public <T> PlatformProxy<T> platformProxy() {
        return new PlatformProxyImpl(this.getPlatform());
    }

    @Override
    public <T> ModProxy<T> modProxy() {
        return new ModProxyImpl(this::isModLoaded);
    }

    public void initializeRuntime() {
        this.ready = true;
        for (Runnable callback : initCallbacks) {
            callback.run();
        }
        this.registerModule(new BaseModule());
        this.registerModule(new ConfigSync());
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (this.isModLoaded(modId)) {
            try {
                Class.forName(className).getConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}

