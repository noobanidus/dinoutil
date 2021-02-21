package noobanidus.mods.dinoutil.sugar_pin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.NonNullList;
import noobanidus.mods.dinoutil.DinoUtil;

import java.util.Collections;


public class Itemsugar_pin extends ItemTool {
  public Itemsugar_pin(String name) {
    super(0f,1f, DinoUtil.SUGAR, Collections.emptySet());
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
      EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.KNOCKBACK, 20), stack);
      items.add(stack);
    }
  }
}
