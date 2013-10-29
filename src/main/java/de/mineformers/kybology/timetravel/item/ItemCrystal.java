package de.mineformers.kybology.timetravel.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.mineformers.kybology.KybologyCore;
import de.mineformers.kybology.api.energy.CrystalType;
import de.mineformers.kybology.api.util.CrystalHelper;
import de.mineformers.kybology.core.item.ItemKybology;
import de.mineformers.kybology.timetravel.lib.Strings;

/**
 * Kybology
 * 
 * ItemCrystal
 * 
 * @author Weneg
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemCrystal extends ItemKybology {

    /**
     * @param id
     * @param name
     */
    public ItemCrystal(int id) {
        super(id, Strings.CRYSTAL_NAME);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world,
            EntityPlayer player) {
        return itemStack;
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        CrystalType type = CrystalHelper.getType(stack);
        return type.getDisplayString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list,
            boolean advanced) {
        CrystalType type = CrystalHelper.getType(stack);
        list.add(type.getTranslatedFunction());
        switch (type.getColor()) {
            case BLUE:
                list.add("\u00A7b" + type.getTranslatedColor() + "\u00A7r");
                break;
            case RED:
                list.add("\u00A7c" + type.getTranslatedColor() + "\u00A7r");
                break;
            case GOLD:
                list.add("\u00A76" + type.getTranslatedColor() + "\u00A7r");
                break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(KybologyCore.MOD_ID
                .toLowerCase() + ":crystal");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int id, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    ItemStack stack = new ItemStack(this, 1, i);

                    list.add(CrystalHelper.construct(stack, new CrystalType(i,
                            j, k)));
                }
            }
        }
    }
}