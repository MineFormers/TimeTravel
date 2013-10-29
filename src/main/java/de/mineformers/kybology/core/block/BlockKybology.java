package de.mineformers.kybology.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.mineformers.kybology.KybologyCore;
import de.mineformers.kybology.core.lib.Reference;
import de.mineformers.kybology.core.tileentity.TileKybology;
import de.mineformers.kybology.timetravel.lib.Strings;

/**
 * Kybology
 * 
 * BlockKybology
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockKybology extends Block implements ITileEntityProvider {

    public BlockKybology(int id, Material material, String name) {
        super(id, material);
        this.setUnlocalizedName(Strings.RESOURCE_PREFIX + name);
        this.setCreativeTab(KybologyCore.tabKybology);
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(":") + 1);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,
            EntityLivingBase entityLiving, ItemStack itemStack) {
        int direction = 0;
        int facing = MathHelper
                .floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0) {
            direction = ForgeDirection.NORTH.ordinal();
        } else if (facing == 1) {
            direction = ForgeDirection.EAST.ordinal();
        } else if (facing == 2) {
            direction = ForgeDirection.SOUTH.ordinal();
        } else if (facing == 3) {
            direction = ForgeDirection.WEST.ordinal();
        }

        if (world.blockHasTileEntity(x, y, z)
                && world.getBlockTileEntity(x, y, z) != null) {
            if (itemStack.hasDisplayName()) {
                ((TileKybology) world.getBlockTileEntity(x, y, z))
                        .setCustomName(itemStack.getDisplayName());
            }

            ((TileKybology) world.getBlockTileEntity(x, y, z))
                    .setOrientation(direction);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.MOD_GROUP.toLowerCase()
                + ":"
                + KybologyCore.RESOURCE_PATH
                + super.getUnlocalizedName().substring(
                        super.getUnlocalizedName().indexOf(".") + 1));
    }

}