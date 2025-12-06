/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Checkbox
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 */
package net.blay09.mods.waystones.client.gui.screen;

import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.blay09.mods.waystones.network.message.EditWaystoneMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class WaystoneSettingsScreen
extends AbstractContainerScreen<WaystoneSettingsMenu> {
    private final MutableComponent isGlobalText = Component.m_237115_((String)"gui.waystones.waystone_settings.is_global");
    private EditBox textField;
    private Button doneButton;
    private Checkbox isGlobalCheckbox;
    private boolean focusTextFieldNextTick;

    public WaystoneSettingsScreen(WaystoneSettingsMenu container, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)container, playerInventory, title);
        this.f_97726_ = 270;
        this.f_97727_ = 200;
    }

    public void m_7856_() {
        this.f_97726_ = this.f_96543_;
        super.m_7856_();
        IWaystone waystone = ((WaystoneSettingsMenu)this.f_97732_).getWaystone();
        String oldText = waystone.getName();
        if (this.textField != null) {
            oldText = this.textField.m_94155_();
        }
        this.textField = new EditBox(Minecraft.m_91087_().f_91062_, this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 20, 200, 20, this.textField, (Component)Component.m_237119_());
        this.textField.m_94199_(128);
        this.textField.m_94144_(oldText);
        this.m_142416_((GuiEventListener)this.textField);
        this.m_264313_((GuiEventListener)this.textField);
        this.doneButton = Button.m_253074_((Component)Component.m_237115_((String)"gui.done"), button -> {
            if (this.textField.m_94155_().isEmpty()) {
                this.focusTextFieldNextTick = true;
                return;
            }
            Balm.getNetworking().sendToServer((Object)new EditWaystoneMessage(waystone.getWaystoneUid(), this.textField.m_94155_(), this.isGlobalCheckbox.m_93840_()));
        }).m_252794_(this.f_96543_ / 2, this.f_96544_ / 2 + 10).m_253046_(100, 20).m_253136_();
        this.m_142416_((GuiEventListener)this.doneButton);
        this.isGlobalCheckbox = new Checkbox(this.f_96543_ / 2 - 100, this.f_96544_ / 2 + 10, 20, 20, (Component)Component.m_237119_(), waystone.isGlobal());
        this.isGlobalCheckbox.f_93624_ = waystone.getWaystoneType().equals((Object)WaystoneTypes.WAYSTONE) && PlayerWaystoneManager.mayEditGlobalWaystones((Player)Objects.requireNonNull(Minecraft.m_91087_().f_91074_));
        this.m_142416_((GuiEventListener)this.isGlobalCheckbox);
    }

    public boolean m_6375_(double mouseX, double mouseY, int button) {
        int chkGlobalLabelX = this.f_96543_ / 2 - 100 + 25;
        int chkGlobalLabelY = this.f_96544_ / 2 + 16;
        int chkGlobalLabelWidth = this.f_96541_.f_91062_.m_92895_(I18n.m_118938_((String)"gui.waystones.waystone_settings.is_global", (Object[])new Object[0]));
        if (mouseX >= (double)chkGlobalLabelX && mouseX < (double)(chkGlobalLabelX + chkGlobalLabelWidth) && mouseY >= (double)chkGlobalLabelY) {
            Objects.requireNonNull(this.f_96541_.f_91062_);
            if (mouseY < (double)(chkGlobalLabelY + 9)) {
                this.isGlobalCheckbox.m_5691_();
                return true;
            }
        }
        if (this.textField.m_5953_(mouseX, mouseY) && button == 1) {
            this.textField.m_94144_("");
            return true;
        }
        return super.m_6375_(mouseX, mouseY, button);
    }

    public boolean m_7933_(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257) {
            this.doneButton.m_5691_();
            return true;
        }
        if (this.textField.m_7933_(keyCode, scanCode, modifiers) || this.textField.m_93696_()) {
            if (keyCode == 256) {
                Objects.requireNonNull(this.f_96541_.f_91074_).m_6915_();
            }
            return true;
        }
        return super.m_7933_(keyCode, scanCode, modifiers);
    }

    protected void m_181908_() {
        this.textField.m_94120_();
        if (this.focusTextFieldNextTick) {
            this.m_264313_((GuiEventListener)this.textField);
            this.focusTextFieldNextTick = false;
        }
    }

    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.m_280430_(this.f_96547_, this.m_96636_(), this.f_96543_ / 2 - 100, this.f_96544_ / 2 - 35, 0xFFFFFF);
        if (this.isGlobalCheckbox.f_93624_) {
            guiGraphics.m_280430_(this.f_96547_, (Component)this.isGlobalText, this.f_96543_ / 2 - 100 + 25, this.f_96544_ / 2 + 16, 0xFFFFFF);
        }
    }

    protected void m_7286_(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    }

    protected void m_280003_(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}

