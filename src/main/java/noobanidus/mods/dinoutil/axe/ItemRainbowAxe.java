package noobanidus.mods.dinoutil.axe;

import com.google.common.collect.ImmutableMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import noobanidus.mods.dinoutil.DinoUtil;

public class ItemRainbowAxe extends ItemAxe {
  public ItemRainbowAxe(String name) {
    super(DinoUtil.RAINBOW, 9.5f, -3.0f);
    setTranslationKey(name);
    setRegistryName(DinoUtil.MODID, name);
  }

  @Override
  public void setDamage(ItemStack stack, int damage) {
    // NOOP
  }

  @Override
  public boolean isDamageable() {
    return false;
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    if (this.isInCreativeTab(tab)) {
      ItemStack stack = new ItemStack(this);
      EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.UNBREAKING, 20), stack);
      items.add(stack);
    }
  }
}
