/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package noobanidus.mods.dinoutil.pouch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import noobanidus.mods.dinoutil.DinoUtil;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ContainerPouch extends Container {

  private IItemHandlerModifiable inventoryHandler;
  private IPouchHandler handler;
  private EntityPlayer player;
  private ItemStack pouch;

  private int inventoryEnd;

  private boolean isServerSide;

  private SlotSupplier supplier;

  public ContainerPouch(EntityPlayer player, boolean isServerSide) {
    this.player = player;
    this.isServerSide = isServerSide;
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first;
    if (isServerSide) {
      first = ServerHerbUtil.getFirstPouch(player);
    } else {
      first = CommonHerbUtil.getFirstPouch(player);
    }

    ItemStack use = ItemStack.EMPTY;
    if (main.getItem() instanceof ItemPouch) {
      use = main;
    } else if (off.getItem() instanceof ItemPouch) {
      use = off;
    } else if (first.getItem() instanceof ItemPouch) {
      use = first;
    }

    if (isServerSide) {
      handler = PouchHandler.getHandler(use);
    } else {
      handler = new ClientPouchHandler(use);
    }
    inventoryHandler = handler.getInventory();
    supplier = PouchSlot::new;

    pouch = use;

    createPlayerInventory(player.inventory);
    createPouchSlots();
  }

  private void createPouchSlots() {
    int xOffset = 42;
    int yOffset = -15;
    int q = 0;
    for (int i = 0; i <= inventoryHandler.getSlots(); i++) {
      // Top Row
      if (i < 4) {
        addSlotToContainer(supplier.create(inventoryHandler, q++, xOffset + 11 + (i * 18), yOffset + 23));
      }
      // Middle Row
      if (i >= 5 && i < 9) {
        addSlotToContainer(supplier.create(inventoryHandler, q++, xOffset + 11 + ((i - 5) * 18), yOffset + 23 + 18));
      }
      // Bottom Row
      if (i >= 9 && i < 13) {
        addSlotToContainer(supplier.create(inventoryHandler, q++, xOffset + 11 + ((i - 9) * 18), yOffset + 23 + 18 + 18));
      }
      // Herb Pouch
    }
    inventoryEnd = q;
  }

  private void createPlayerInventory(InventoryPlayer inventoryPlayer) {

    int xOffset = 8;
    int yOffset = 86;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
    }
  }

  @Override
  public boolean canInteractWith(@Nonnull EntityPlayer player) {
    ItemStack main = player.getHeldItemMainhand();
    ItemStack off = player.getHeldItemOffhand();
    ItemStack first = ServerHerbUtil.getFirstPouch(player);
    return (main.equals(pouch) || off.equals(pouch) || first.equals(pouch));
  }

  @Override
  @Nonnull
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack slotStack = ItemStack.EMPTY;

    Slot slot = inventorySlots.get(index);

    int invStart = 36;
    int invEnd = invStart + inventoryEnd;

    if (slot != null && slot.getHasStack()) {
      ItemStack stack = slot.getStack();
      slotStack = stack.copy();

      if (index < 36) { // Player Inventory -> Inventory/herbs
        if (stack.getItem() != DinoUtil.POUCH_ITEM) {
          return ItemStack.EMPTY;
        } else if (!mergeItemStack(stack, invStart, invEnd, false)) {
          return ItemStack.EMPTY;
        }
      } else {
        if (!mergeItemStack(stack, 27, 36, false) && !mergeItemStack(stack, 0, 27, false)) {
          return ItemStack.EMPTY;
        }
      }

      if (stack.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      }

      slot.onSlotChanged();
    }

    return slotStack;
  }

  @Override
  @Nonnull
  public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
    if (slotId >= 0) {
      ItemStack stack = getSlot(slotId).getStack();
      if (stack.getItem() instanceof ItemPouch) {
        return ItemStack.EMPTY;
      }
    }

    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();

    handler.markDirty();
  }

  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);

    if (!player.world.isRemote) {
      Objects.requireNonNull(player.world.getMapStorage()).saveAllData();
    }
  }

  @FunctionalInterface
  public interface SlotSupplier {
    Slot create(IItemHandler itemHandler, int index, int xPosition, int yPosition);
  }

  public class PouchSlot extends SlotItemHandler {
    public PouchSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
      if (stack.getItem() instanceof ItemPouch) {
        return false;
      }

      if (stack.getItem() != DinoUtil.POUCH_ITEM) {
        return false;
      }

      return super.isItemValid(stack);
    }

    @Override
    public void onSlotChange(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
      super.onSlotChange(stack1, stack2);
      handler.markDirty();
    }

    @Override
    public void onSlotChanged() {
      super.onSlotChanged();
      handler.markDirty();
    }
  }
}
