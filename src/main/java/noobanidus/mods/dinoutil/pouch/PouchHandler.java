package noobanidus.mods.dinoutil.pouch;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.function.Function;

public class PouchHandler implements IPouchHandler {

  private IItemHandlerModifiable inventorySlots;
  private ItemStack pouch;
  private Runnable markDirty;

  public PouchHandler(ItemStack pouch) {
    this.pouch = pouch;

    NBTTagCompound tag = ItemUtil.getOrCreateTag(pouch);
    PouchHandlerData data;
    if (tag.hasUniqueId("bag_id")) {
      data = PouchHandlerRegistry.getData(tag.getUniqueId("bag_id"));
    } else {
      data = PouchHandlerRegistry.getNewData();
      tag.setUniqueId("bag_id", data.getUuid());
    }
    this.inventorySlots = data.getInventoryHandler();
    markDirty = data::markDirty;
  }

  @Override
  public IItemHandlerModifiable getInventory() {
    return inventorySlots;
  }

  @Override
  public void markDirty() {
    this.markDirty.run();
  }

  @Nullable
  public static PouchHandler getHandler(ItemStack stack) {
    if (!(stack.getItem() instanceof ItemPouch)) {
      return null;
    }
    return new PouchHandler(stack);
  }
}
