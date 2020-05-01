package noobanidus.mods.dinoutil.pouch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommonHerbUtil {
  public static ItemStack getFirstPouch(EntityPlayer player) {
    List<ItemStack> pouches = getPouches(player);
    if (pouches.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return pouches.get(0);
  }

  public static List<ItemStack> getPouches(EntityPlayer player) {
    List<ItemStack> result = new ArrayList<>();
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        result.add(player.inventory.getStackInSlot(i));
      }
    }

    return result;
  }
}
