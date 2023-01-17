package me.flamboyant.utils;

import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnchantmentUtils {
    public static final List<Enchantment> enchantmentValues = Collections.unmodifiableList(Arrays.asList(Enchantment.values()));
    public static final int enchantmentValuesSize = enchantmentValues.size();
}
