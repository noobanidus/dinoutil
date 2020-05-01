package noobanidus.mods.dinoutil.pouch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class ServerHerbUtil {
  public static ItemStack getFirstPouch(EntityPlayer player) {
    if (player.world.isRemote) {
      return ItemStack.EMPTY;
    }

    return CommonHerbUtil.getFirstPouch(player);
  }

  public static List<ItemStack> getPouches(EntityPlayer player) {
    if (player.world.isRemote) {
      return Collections.emptyList();
    }

    return CommonHerbUtil.getPouches(player);
  }
}