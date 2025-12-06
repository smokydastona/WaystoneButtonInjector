/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  net.minecraft.client.gui.GuiGraphics
 */
package net.blay09.mods.balm.api.event.client;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.GuiGraphics;

public abstract class GuiDrawEvent
extends BalmEvent {
    private final Window window;
    private final GuiGraphics guiGraphics;
    private final Element element;

    public GuiDrawEvent(Window window, GuiGraphics guiGraphics, Element element) {
        this.window = window;
        this.guiGraphics = guiGraphics;
        this.element = element;
    }

    public Window getWindow() {
        return this.window;
    }

    public GuiGraphics getGuiGraphics() {
        return this.guiGraphics;
    }

    public Element getElement() {
        return this.element;
    }

    public static enum Element {
        ALL,
        HEALTH,
        CHAT,
        DEBUG,
        BOSS_INFO,
        PLAYER_LIST;

    }

    public static class Post
    extends GuiDrawEvent {
        public Post(Window window, GuiGraphics guiGraphics, Element element) {
            super(window, guiGraphics, element);
        }
    }

    public static class Pre
    extends GuiDrawEvent {
        public Pre(Window window, GuiGraphics guiGraphics, Element element) {
            super(window, guiGraphics, element);
        }
    }
}

