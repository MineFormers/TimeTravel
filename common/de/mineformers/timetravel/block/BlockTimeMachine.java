package de.mineformers.timetravel.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.mineformers.timetravel.TimeTravel;
import de.mineformers.timetravel.core.util.NetworkHelper;
import de.mineformers.timetravel.lib.GuiIds;
import de.mineformers.timetravel.lib.Strings;
import de.mineformers.timetravel.tileentity.TileTT;
import de.mineformers.timetravel.tileentity.TileTimeMachine;
import de.mineformers.timetravel.travelling.timemachine.TMPartBase;
import de.mineformers.timetravel.travelling.timemachine.TMPartModule;
import de.mineformers.timetravel.travelling.timemachine.TMPartModule.ModuleType;
import de.mineformers.timetravel.travelling.timemachine.TMPartPanel;
import de.mineformers.timetravel.travelling.timemachine.TimeMachinePart;

/**
 * TimeTravel
 * 
 * BlockTimeMachine
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockTimeMachine extends BlockTT {

	private int direction = 0;

	public BlockTimeMachine(int id) {
		super(id, Material.iron, Strings.TIMEMACHINE_NAME);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
	        int x, int y, int z) {
		return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int id, CreativeTabs tab, List subItems) {
		subItems.add(new ItemStack(id, 1, 0));
		subItems.add(new ItemStack(id, 1, 1));
		subItems.add(new ItemStack(id, 1, 2));
		subItems.add(new ItemStack(id, 1, 3));
		subItems.add(new ItemStack(id, 1, 4));
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		blockIcon = Block.blockIron.getBlockTextureFromSide(0);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileTimeMachine(metadata);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
	        float hitX, float hitY, float hitZ, int meta) {
		if (side == 3) {
			direction = ForgeDirection.SOUTH.ordinal();
		} else if (side == 4) {
			direction = ForgeDirection.WEST.ordinal();
		} else if (side == 2) {
			direction = ForgeDirection.NORTH.ordinal();
		} else if (side == 5) {
			direction = ForgeDirection.EAST.ordinal();
		} else {
			direction = -1;
		}

		return meta;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
	        EntityLivingBase entityLiving, ItemStack itemStack) {
		if (direction == -1) {
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
		}

		if (world.blockHasTileEntity(x, y, z)) {
			if (itemStack.hasDisplayName()) {
				((TileTT) world.getBlockTileEntity(x, y, z))
				        .setCustomName(itemStack.getDisplayName());
			}
			((TileTT) world.getBlockTileEntity(x, y, z))
			        .setOrientation(direction);
			if (world.getBlockMetadata(x, y, z) == TimeMachinePart.TYPE_BASE) {
				((TMPartBase) ((TileTimeMachine) world.getBlockTileEntity(x, y,
				        z)).getPart()).validateMultiblock();
			}
		}
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int oldMeta) {
		super.onBlockPreDestroy(world, x, y, z, oldMeta);

		if (oldMeta == TimeMachinePart.TYPE_MODULE) {
			TileTimeMachine tile = (TileTimeMachine) world.getBlockTileEntity(
			        x, y, z);
			TMPartModule module = (TMPartModule) tile.getPart();
			ItemStack reward = module.getTypeItem(module.getType());
			if (reward != null) {
				EntityItem entityItem = new EntityItem(world, x, y, z, reward);
				world.spawnEntityInWorld(entityItem);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z,
	        AxisAlignedBB bounds, List list, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		switch (meta) {
			case 0:
				this.setBlockBounds(0, 0, 0, 1F, 0.375F, 1F);
				super.addCollisionBoxesToList(world, x, y, z, bounds, list,
				        entity);
				this.setBlockBounds(0.125F, 0.375F, 0.125F, 0.875F, 0.5F,
				        0.875F);
				super.addCollisionBoxesToList(world, x, y, z, bounds, list,
				        entity);
				this.setBlockBounds(0, 0, 0, 1F, 0.5F, 1F);
				return;
			case 2:
				this.setBlockBounds(0, 0, 0, 1F, 0.375F, 1F);
				super.addCollisionBoxesToList(world, x, y, z, bounds, list,
				        entity);
				this.setBlockBounds(0, 0, 0, 1F, 0.375F, 1F);
				return;
			case 3:
				this.setBlockBounds(0F, 0F, 0F, 1F, 2F, 1F);
				super.addCollisionBoxesToList(world, x, y, z, bounds, list,
				        entity);
				this.setBlockBounds(0F, 0F, 0F, 1F, 2F, 1F);
				return;
			case 4:
				TileTimeMachine tile = (TileTimeMachine) world
				        .getBlockTileEntity(x, y, z);
				switch (tile.getOrientation()) {
					case NORTH:
						this.setBlockBounds(0.375F, 0, 0.0625F, 0.9375F,
						        0.5625F, 0.625F);
						super.addCollisionBoxesToList(world, x, y, z, bounds,
						        list, entity);
						break;
					case SOUTH:
						this.setBlockBounds(1 - 0.9375F, 0, 1 - 0.625F,
						        1 - 0.375F, 0.5625F, 1 - 0.0625F);
						super.addCollisionBoxesToList(world, x, y, z, bounds,
						        list, entity);
						break;
					case EAST:
						this.setBlockBounds(0.375F, 0, 0.375F, 0.9375F,
						        0.5625F, 0.9375F);
						super.addCollisionBoxesToList(world, x, y, z, bounds,
						        list, entity);
						break;
					case WEST:
						this.setBlockBounds(1 - 0.9375F, 0, 1 - 0.9375F,
						        1 - 0.375F, 0.5625F, 1 - 0.375F);
						super.addCollisionBoxesToList(world, x, y, z, bounds,
						        list, entity);
						break;
					default:
						this.setBlockBounds(0, 0, 0, 1, 0.5625F, 1);
						super.addCollisionBoxesToList(world, x, y, z, bounds,
						        list, entity);
						break;
				}
				return;
			default:
				this.setBlockBounds(0, 0, 0, 1F, 1F, 1F);
				super.addCollisionBoxesToList(world, x, y, z, bounds, list,
				        entity);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y,
	        int z) {
		int meta = access.getBlockMetadata(x, y, z);
		switch (meta) {
			case 1:
				this.setBlockBounds(0F, 0F, 0F, 1.0F, 0.375F, 1.0F);
				return;
			case 2:
				this.setBlockBounds(0, 0, 0, 1F, 0.375F, 1F);
				return;
			case 3:
				this.setBlockBounds(0F, 0F, 0F, 1F, 2F, 1F);
				return;
			case 4:
				TileTimeMachine tile = (TileTimeMachine) access
				        .getBlockTileEntity(x, y, z);
				switch (tile.getOrientation()) {
					case NORTH:
						this.setBlockBounds(0.375F, 0, 0.0625F, 0.9375F,
						        0.5625F, 0.625F);
						break;
					case SOUTH:
						this.setBlockBounds(1 - 0.9375F, 0, 1 - 0.625F,
						        1 - 0.375F, 0.5625F, 1 - 0.0625F);
						break;
					case EAST:
						this.setBlockBounds(0.375F, 0, 0.375F, 0.9375F,
						        0.5625F, 0.9375F);
						break;
					case WEST:
						this.setBlockBounds(1 - 0.9375F, 0, 1 - 0.9375F,
						        1 - 0.375F, 0.5625F, 1 - 0.375F);
						break;
					default:
						this.setBlockBounds(0, 0, 0, 1, 0.5625F, 1);
						break;
				}
				return;
			default:
				this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
	        int blockId) {
		if (!world.isRemote) {
			TileTimeMachine tile = (TileTimeMachine) world.getBlockTileEntity(
			        x, y, z);
			tile.getPart().invalidateMultiblock();
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
	        EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileTimeMachine tile = (TileTimeMachine) world.getBlockTileEntity(
			        x, y, z);
			if (tile.getTypeMeta() == TimeMachinePart.TYPE_PANEL) {
				if (tile.getPart().isValidMultiblock())
					player.openGui(TimeTravel.instance, GuiIds.TIMEMACHINE,
					        world, x, y, z);
				return true;
			} else if (tile.getTypeMeta() == TimeMachinePart.TYPE_MODULE) {
				TMPartModule module = (TMPartModule) tile.getPart();
				if (player.getHeldItem() != null) {
					ItemStack item = player.getHeldItem();
					ModuleType prevType = module.getType();
					if (module.switchType(item)) {
						item.stackSize -= 1;
						if (item.stackSize <= 0)
							item = null;

						ItemStack reward = module.getTypeItem(prevType);
						if (reward != null) {
							EntityItem entityItem = new EntityItem(world, x, y,
							        z, reward);
							world.spawnEntityInWorld(entityItem);
						}

						player.addChatMessage("Changed mode to: "
						        + module.getType().toString().substring(0, 1)
						        + module.getType().toString().substring(1)
						                .toLowerCase());

						NetworkHelper.sendTilePacket(world, x, z, y);
						return true;
					}
				} else {
					if (player.isSneaking()) {
						ItemStack reward = module.getTypeItem(module.getType());
						if (reward != null) {
							EntityItem entityItem = new EntityItem(world, x, y,
							        z, reward);
							world.spawnEntityInWorld(entityItem);
							module.setType(ModuleType.DEFAULT);
							player.addChatMessage("Changed mode to: "
							        + module.getType().toString()
							                .substring(0, 1)
							        + module.getType().toString().substring(1)
							                .toLowerCase());

							NetworkHelper.sendTilePacket(world, x, z, y);
						}
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z,
	        Random random) {
		if (world.blockHasTileEntity(x, y, z)) {
			TileTimeMachine tile = (TileTimeMachine) world.getBlockTileEntity(
			        x, y, z);
			if (tile.getTypeMeta() == TimeMachinePart.TYPE_PANEL) {
				TMPartPanel panel = (TMPartPanel) tile.getPart();
				if (panel.getCountdown() == 0) {
					x = (int) panel.getBasePosition().xCoord;
					z = (int) panel.getBasePosition().zCoord;
					for (int i = 0; i <= 10; i++) {
						double xMod = (double) ((float) x + 0.5F + random
						        .nextFloat()
						        * MathHelper.getRandomIntegerInRange(random,
						                -1, 1));
						double yMod = (double) ((float) y + random.nextFloat()
						        * 6.0F / 10.0F + 0.5F);
						double zMod = (double) ((float) z + 0.5F + random
						        .nextFloat()
						        * MathHelper.getRandomIntegerInRange(random,
						                -1, 1));
						int multi = random.nextInt();
						multi = multi < 0 ? -1 : 1;
						float mod1 = random.nextFloat() * 0.3F * multi;
						float mod2 = random.nextFloat() * 0.3F * multi;
						world.spawnParticle("portal", (double) (xMod - mod1),
						        yMod, (double) (zMod + mod2), 0.0D, 0.0D, 0.0D);
					}
				}
			}
		}
	}

}