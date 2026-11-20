package com.isekai.item;
import com.isekai.IsekaiMod;
import com.isekai.registry.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;

public enum AetherArmorMaterial implements ArmorMaterial {
 AETHER("aether",34,new int[]{3,6,8,3},18,SoundEvents.ARMOR_EQUIP_DIAMOND,2.5F,0.08F),
 SUN("sun_aether",38,new int[]{3,6,8,3},22,SoundEvents.ARMOR_EQUIP_NETHERITE,3.0F,0.10F),
 MOON("moon_aether",32,new int[]{3,5,8,3},26,SoundEvents.ARMOR_EQUIP_DIAMOND,2.0F,0.12F);
 private final String name; private final int durability; private final int[] defense; private final int enchantment;
 private final SoundEvent sound; private final float toughness, knockback;
 AetherArmorMaterial(String n,int d,int[] f,int e,SoundEvent s,float t,float k){name=n;durability=d;defense=f;enchantment=e;sound=s;toughness=t;knockback=k;}
 @Override public int getDurabilityForType(ArmorItem.Type type){return durability*switch(type){case HELMET->11;case CHESTPLATE->16;case LEGGINGS->15;case BOOTS->13;};}
 @Override public int getDefenseForType(ArmorItem.Type type){return defense[switch(type){case BOOTS->0;case LEGGINGS->1;case CHESTPLATE->2;case HELMET->3;}];}
 @Override public int getEnchantmentValue(){return enchantment;}
 @Override public SoundEvent getEquipSound(){return sound;}
 @Override public Ingredient getRepairIngredient(){return Ingredient.of(name.startsWith("sun")?ModItems.SUN_AETHER_INGOT.get():name.startsWith("moon")?ModItems.MOON_AETHER_INGOT.get():ModItems.AETHER_INGOT.get());}
 @Override public String getName(){return IsekaiMod.MOD_ID+":"+name;}
 @Override public float getToughness(){return toughness;}
 @Override public float getKnockbackResistance(){return knockback;}
}
