/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.mixin.ScreenAccessor
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 */
package net.blay09.mods.waystones.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.ITooltipProvider;
import net.blay09.mods.waystones.client.gui.widget.RemoveWaystoneButton;
import net.blay09.mods.waystones.client.gui.widget.SortWaystoneButton;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.network.message.RemoveWaystoneMessage;
import net.blay09.mods.waystones.network.message.RequestEditWaystoneMessage;
import net.blay09.mods.waystones.network.message.SelectWaystoneMessage;
import net.blay09.mods.waystones.network.message.SortWaystoneMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public abstract class WaystoneSelectionScreenBase
extends AbstractContainerScreen<WaystoneSelectionMenu> {
    private final List<IWaystone> waystones;
    private List<IWaystone> filteredWaystones;
    private final List<ITooltipProvider> tooltipProviders = new ArrayList<ITooltipProvider>();
    private String searchText = "";
    private Button btnPrevPage;
    private Button btnNextPage;
    private EditBox searchBox;
    private int pageOffset;
    private int headerY;
    private boolean isLocationHeaderHovered;
    private int buttonsPerPage;
    private static final int headerHeight = 64;
    private static final int footerHeight = 25;
    private static final int entryHeight = 25;

    public WaystoneSelectionScreenBase(WaystoneSelectionMenu container, Inventory playerInventory, Component title) {
        super((AbstractContainerMenu)container, playerInventory, title);
        this.waystones = container.getWaystones();
        this.filteredWaystones = new ArrayList<IWaystone>(this.waystones);
        this.f_97726_ = 270;
        this.f_97727_ = 200;
    }

    public void m_7856_() {
        int maxContentHeight = (int)((float)this.f_96544_ * 0.6f);
        int maxButtonsPerPage = (maxContentHeight - 64 - 25) / 25;
        this.buttonsPerPage = Math.max(4, Math.min(maxButtonsPerPage, this.waystones.size()));
        int contentHeight = 64 + this.buttonsPerPage * 25 + 25;
        this.f_97726_ = this.f_96543_;
        this.f_97727_ = contentHeight;
        super.m_7856_();
        this.tooltipProviders.clear();
        this.btnPrevPage = Button.m_253074_((Component)Component.m_237115_((String)"gui.waystones.waystone_selection.previous_page"), button -> {
            this.pageOffset = Screen.m_96638_() ? 0 : this.pageOffset - 1;
            this.updateList();
        }).m_252794_(this.f_96543_ / 2 - 100, this.f_96544_ / 2 + 40).m_253046_(95, 20).m_253136_();
        this.m_142416_(this.btnPrevPage);
        this.btnNextPage = Button.m_253074_((Component)Component.m_237115_((String)"gui.waystones.waystone_selection.next_page"), button -> {
            this.pageOffset = Screen.m_96638_() ? (this.waystones.size() - 1) / this.buttonsPerPage : this.pageOffset + 1;
            this.updateList();
        }).m_252794_(this.f_96543_ / 2 + 5, this.f_96544_ / 2 + 40).m_253046_(95, 20).m_253136_();
        this.m_142416_(this.btnNextPage);
        this.updateList();
        this.searchBox = new EditBox(this.f_96547_, this.f_96543_ / 2 - 99, this.f_97736_ + 64 - 24, 198, 20, (Component)Component.m_237119_());
        this.searchBox.m_94151_(text -> {
            this.pageOffset = 0;
            this.searchText = text;
            this.updateList();
        });
        this.m_142416_(this.searchBox);
    }

    protected <T extends GuiEventListener & Renderable> T m_142416_(T widget) {
        if (widget instanceof ITooltipProvider) {
            this.tooltipProviders.add((ITooltipProvider)widget);
        }
        return (T)super.m_142416_(widget);
    }

    private void updateList() {
        this.filteredWaystones = this.waystones.stream().filter(waystone -> waystone.getName().toLowerCase().contains(this.searchText.toLowerCase())).collect(Collectors.toList());
        this.headerY = 0;
        this.btnPrevPage.f_93623_ = this.pageOffset > 0;
        this.btnNextPage.f_93623_ = this.pageOffset < (this.filteredWaystones.size() - 1) / this.buttonsPerPage;
        this.tooltipProviders.clear();
        Predicate<Object> removePredicate = button -> button instanceof WaystoneButton || button instanceof SortWaystoneButton || button instanceof RemoveWaystoneButton;
        ((ScreenAccessor)this).balm_getChildren().removeIf(removePredicate);
        ((ScreenAccessor)this).balm_getNarratables().removeIf(removePredicate);
        ((ScreenAccessor)this).balm_getRenderables().removeIf(removePredicate);
        int y = this.f_97736_ + 64 + this.headerY;
        for (int i = 0; i < this.buttonsPerPage; ++i) {
            int entryIndex = this.pageOffset * this.buttonsPerPage + i;
            if (entryIndex < 0 || entryIndex >= this.filteredWaystones.size()) continue;
            IWaystone waystone2 = this.filteredWaystones.get(entryIndex);
            this.m_142416_(this.createWaystoneButton(y, waystone2));
            if (this.allowSorting()) {
                SortWaystoneButton sortUpButton = new SortWaystoneButton(this.f_96543_ / 2 + 108, y + 2, -1, y, 20, it -> this.sortWaystone(entryIndex, -1));
                if (entryIndex == 0) {
                    sortUpButton.f_93623_ = false;
                }
                this.m_142416_(sortUpButton);
                SortWaystoneButton sortDownButton = new SortWaystoneButton(this.f_96543_ / 2 + 108, y + 13, 1, y, 20, it -> this.sortWaystone(entryIndex, 1));
                if (entryIndex == this.filteredWaystones.size() - 1) {
                    sortDownButton.f_93623_ = false;
                }
                this.m_142416_(sortDownButton);
            }
            if (this.allowDeletion()) {
                RemoveWaystoneButton removeButton = new RemoveWaystoneButton(this.f_96543_ / 2 + 122, y + 4, y, 20, waystone2, button -> {
                    LocalPlayer player = Minecraft.m_91087_().f_91074_;
                    PlayerWaystoneManager.deactivateWaystone((Player)Objects.requireNonNull(player), waystone2);
                    Balm.getNetworking().sendToServer((Object)new RemoveWaystoneMessage(waystone2.getWaystoneUid()));
                    this.updateList();
                });
                if (!waystone2.isGlobal() || Minecraft.m_91087_().f_91074_.m_150110_().f_35937_) {
                    this.m_142416_(removeButton);
                }
            }
            y += 22;
        }
        this.btnPrevPage.m_253211_(this.f_97736_ + this.headerY + 64 + this.buttonsPerPage * 22 + (this.filteredWaystones.size() > 0 ? 10 : 0));
        this.btnNextPage.m_253211_(this.f_97736_ + this.headerY + 64 + this.buttonsPerPage * 22 + (this.filteredWaystones.size() > 0 ? 10 : 0));
    }

    private WaystoneButton createWaystoneButton(int y, IWaystone waystone) {
        IWaystone waystoneFrom = ((WaystoneSelectionMenu)this.f_97732_).getWaystoneFrom();
        LocalPlayer player = Minecraft.m_91087_().f_91074_;
        int xpLevelCost = Math.round(PlayerWaystoneManager.predictExperienceLevelCost((Entity)Objects.requireNonNull(player), waystone, ((WaystoneSelectionMenu)this.f_97732_).getWarpMode(), waystoneFrom));
        WaystoneButton btnWaystone = new WaystoneButton(this.f_96543_ / 2 - 100, y, waystone, xpLevelCost, button -> this.onWaystoneSelected(waystone));
        if (waystoneFrom != null && waystone.getWaystoneUid().equals(waystoneFrom.getWaystoneUid())) {
            btnWaystone.f_93623_ = false;
        }
        return btnWaystone;
    }

    protected void onWaystoneSelected(IWaystone waystone) {
        Balm.getNetworking().sendToServer((Object)new SelectWaystoneMessage(waystone.getWaystoneUid()));
    }

    private void sortWaystone(int index, int sortDir) {
        int otherIndex;
        if (index < 0 || index >= this.waystones.size()) {
            return;
        }
        if (Screen.m_96638_()) {
            otherIndex = sortDir == -1 ? -1 : this.waystones.size();
        } else {
            otherIndex = index + sortDir;
            if (otherIndex < 0 || otherIndex >= this.waystones.size()) {
                return;
            }
        }
        PlayerWaystoneManager.swapWaystoneSorting((Player)Minecraft.m_91087_().f_91074_, index, otherIndex);
        Balm.getNetworking().sendToServer((Object)new SortWaystoneMessage(index, otherIndex));
        this.updateList();
    }

    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        if (this.isLocationHeaderHovered && ((WaystoneSelectionMenu)this.f_97732_).getWaystoneFrom() != null) {
            Balm.getNetworking().sendToServer((Object)new RequestEditWaystoneMessage(((WaystoneSelectionMenu)this.f_97732_).getWaystoneFrom().getWaystoneUid()));
            return true;
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }

    public void m_88315_(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(guiGraphics, mouseX, mouseY);
        for (ITooltipProvider tooltipProvider : this.tooltipProviders) {
            if (!tooltipProvider.shouldShowTooltip()) continue;
            guiGraphics.m_280677_(Minecraft.m_91087_().f_91062_, tooltipProvider.getTooltipComponents(), Optional.empty(), mouseX, mouseY);
        }
    }

    protected void m_7286_(GuiGraphics guiGraphics, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
    }

    protected void m_280003_(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Font font = Minecraft.m_91087_().f_91062_;
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        IWaystone fromWaystone = ((WaystoneSelectionMenu)this.f_97732_).getWaystoneFrom();
        guiGraphics.m_280653_(font, this.m_96636_(), this.f_97726_ / 2, this.headerY + (fromWaystone != null ? 20 : 0), 0xFFFFFF);
        if (fromWaystone != null) {
            this.drawLocationHeader(guiGraphics, fromWaystone, mouseX, mouseY, this.f_97726_ / 2, this.headerY);
        }
        if (this.waystones.size() == 0) {
            guiGraphics.m_280137_(font, String.valueOf(ChatFormatting.RED) + I18n.m_118938_((String)"gui.waystones.waystone_selection.no_waystones_activated", (Object[])new Object[0]), this.f_97726_ / 2, this.f_97727_ / 2 - 20, 0xFFFFFF);
        }
    }

    /*
     * Unable to fully structure code
     */
    private void drawLocationHeader(GuiGraphics guiGraphics, IWaystone waystone, int mouseX, int mouseY, int x, int y) {
        font = Minecraft.m_91087_().f_91062_;
        locationPrefix = String.valueOf(ChatFormatting.YELLOW) + I18n.m_118938_((String)"gui.waystones.waystone_selection.current_location", (Object[])new Object[0]) + " ";
        locationPrefixWidth = font.m_92895_(locationPrefix);
        effectiveName = waystone.getName();
        if (effectiveName.isEmpty()) {
            effectiveName = I18n.m_118938_((String)"gui.waystones.waystone_selection.unnamed_waystone", (Object[])new Object[0]);
        }
        locationWidth = font.m_92895_(effectiveName);
        fullWidth = locationPrefixWidth + locationWidth;
        startX = x - fullWidth / 2 + locationPrefixWidth;
        startY = y + this.f_97736_;
        if (mouseX < startX || mouseX >= startX + locationWidth + 16 || mouseY < startY) ** GOTO lbl-1000
        Objects.requireNonNull(font);
        if (mouseY < startY + 9) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = false;
        }
        this.isLocationHeaderHovered = v0;
        player = Minecraft.m_91087_().f_91074_;
        waystoneEditPermissions = PlayerWaystoneManager.mayEditWaystone((Player)player, player.m_9236_(), waystone);
        fullText = locationPrefix + String.valueOf(ChatFormatting.WHITE);
        if (this.isLocationHeaderHovered && waystoneEditPermissions == WaystoneEditPermissions.ALLOW) {
            fullText = fullText + String.valueOf(ChatFormatting.UNDERLINE);
        }
        fullText = fullText + effectiveName;
        guiGraphics.m_280488_(font, fullText, x - fullWidth / 2, y, 0xFFFFFF);
        if (this.isLocationHeaderHovered && waystoneEditPermissions == WaystoneEditPermissions.ALLOW) {
            poseStack = guiGraphics.m_280168_();
            poseStack.m_85836_();
            scale = 0.5f;
            poseStack.m_252880_((float)x + (float)fullWidth / 2.0f + 4.0f, (float)y, 0.0f);
            poseStack.m_85841_(scale, scale, scale);
            guiGraphics.m_280480_(new ItemStack((ItemLike)Items.f_42614_), 0, 0);
            poseStack.m_85849_();
        }
    }

    protected boolean allowSorting() {
        return true;
    }

    protected boolean allowDeletion() {
        return true;
    }

    public boolean m_7933_(int key, int scanCode, int modifiers) {
        if (!this.searchBox.m_93696_() || key == 256 && this.m_6913_()) {
            return super.m_7933_(key, scanCode, modifiers);
        }
        return this.searchBox.m_7933_(key, scanCode, modifiers);
    }
}

