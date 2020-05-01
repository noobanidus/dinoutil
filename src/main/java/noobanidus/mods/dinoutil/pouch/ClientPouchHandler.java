package noobanidus.mods.dinoutil.pouch;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class ClientPouchHandler implements IPouchHandler {
  private ItemStackHandler inventory = new ItemStackHandler(12);

  private final ItemStack pouch;

  public ClientPouchHandler(ItemStack stack) {
    this.pouch = stack;
  }

  @Override
  public IItemHandlerModifiable getInventory() {
    return inventory;
  }

  @Override
  public void markDirty() {
  }
}
