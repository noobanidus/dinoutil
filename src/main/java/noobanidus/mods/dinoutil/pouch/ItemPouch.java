package noobanidus.mods.dinoutil.pouch;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noobanidus.mods.dinoutil.DinoUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPouch extends Item implements IItemPouch {
  public ItemPouch(@Nonnull String name) {
    super();
    this.setMaxStackSize(1);
    setTranslationKey(name);
    setRegistryName(DinoUtil.MODID, name);
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!world.isRemote) {
      player.openGui(DinoUtil.instance, GuiHandler.POUCH_ID, world, 0, 0, 0);
    }
    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
