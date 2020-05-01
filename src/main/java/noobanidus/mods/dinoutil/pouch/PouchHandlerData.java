package noobanidus.mods.dinoutil.pouch;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "NullableProblems"})
public class PouchHandlerData extends WorldSavedData {
  private static final String identifier = "DinoCashPouch-";

  private UUID uuid;
  private MarkDirtyHandler inventoryHandler;
  private boolean defer = false;

  public static String name(UUID uuid) {
    return identifier + uuid.toString();
  }

  public PouchHandlerData(String identifier) {
    super(identifier);
    this.uuid = UUID.fromString(identifier.replace(PouchHandlerData.identifier, ""));
    defer = true;
  }

  public PouchHandlerData(UUID uuid) {
    super(name(uuid));
    this.uuid = uuid;
    createHandler();
  }

  private void createHandler() {
    this.inventoryHandler = new MarkDirtyHandler(12);
  }

  public UUID getUuid() {
    return uuid;
  }

  public MarkDirtyHandler getInventoryHandler() {
    return inventoryHandler;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    createHandler();
    inventoryHandler.deserializeNBT(nbt.getCompoundTag("inventory"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag("inventory", inventoryHandler.serializeNBT());
    return compound;
  }

  public class MarkDirtyHandler extends ItemStackHandler {
    public MarkDirtyHandler(int size) {
      super(size);
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      markDirty();
    }

    @Override
    public void setSize(int size) {
      List<ItemStack> original = stacks;
      super.setSize(size);
      for (int i = 0; i < Math.min(original.size(), size); i++) {
        stacks.set(i, original.get(i));
      }
    }
  }
}
