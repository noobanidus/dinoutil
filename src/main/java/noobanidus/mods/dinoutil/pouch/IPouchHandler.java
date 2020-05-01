package noobanidus.mods.dinoutil.pouch;

import net.minecraftforge.items.IItemHandlerModifiable;

public interface IPouchHandler {

  IItemHandlerModifiable getInventory();

  void markDirty();
}
