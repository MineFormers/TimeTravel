package de.mineformers.kybology.timetravel.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.MapGenBase;

/**
 * Kybology
 * 
 * MapGenTutorialCaves
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MapGenTutorialCaves extends MapGenBase {
	/**
	 * Generates a larger initial cave node than usual. Called 25% of the time.
	 */
	protected void generateLargeCaveNode(long par1, int par3, int par4,
	        byte[] par5ArrayOfByte, double par6, double par8, double par10) {
		this.generateCaveNode(par1, par3, par4, par5ArrayOfByte, par6, par8,
		        par10, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1,
		        0.5D);
	}

	/**
	 * Generates a node in the current cave system recursion tree.
	 */
	protected void generateCaveNode(long par1, int par3, int par4,
	        byte[] par5ArrayOfByte, double par6, double par8, double par10,
	        float par12, float par13, float par14, int par15, int par16,
	        double par17) {
		double d4 = (double) (par3 * 16 + 8);
		double d5 = (double) (par4 * 16 + 8);
		float f3 = 0.0F;
		float f4 = 0.0F;
		Random random = new Random(par1);

		if (par16 <= 0) {
			int j1 = this.range * 16 - 16;
			par16 = j1 - random.nextInt(j1 / 4);
		}

		boolean flag = false;

		if (par15 == -1) {
			par15 = par16 / 2;
			flag = true;
		}

		int k1 = random.nextInt(par16 / 2) + par16 / 4;

		for (boolean flag1 = random.nextInt(6) == 0; par15 < par16; ++par15) {
			double d6 = 1.5D + (double) (MathHelper.sin((float) par15
			        * (float) Math.PI / (float) par16)
			        * par12 * 1.0F);
			double d7 = d6 * par17;
			float f5 = MathHelper.cos(par14);
			float f6 = MathHelper.sin(par14);
			par6 += (double) (MathHelper.cos(par13) * f5);
			par8 += (double) f6;
			par10 += (double) (MathHelper.sin(par13) * f5);

			if (flag1) {
				par14 *= 0.92F;
			} else {
				par14 *= 0.7F;
			}

			par14 += f4 * 0.1F;
			par13 += f3 * 0.1F;
			f4 *= 0.9F;
			f3 *= 0.75F;
			f4 += (random.nextFloat() - random.nextFloat())
			        * random.nextFloat() * 2.0F;
			f3 += (random.nextFloat() - random.nextFloat())
			        * random.nextFloat() * 4.0F;

			if (!flag && par15 == k1 && par12 > 1.0F && par16 > 0) {
				this.generateCaveNode(random.nextLong(), par3, par4,
				        par5ArrayOfByte, par6, par8, par10,
				        random.nextFloat() * 0.5F + 0.5F, par13
				                - ((float) Math.PI / 2F), par14 / 3.0F, par15,
				        par16, 1.0D);
				this.generateCaveNode(random.nextLong(), par3, par4,
				        par5ArrayOfByte, par6, par8, par10,
				        random.nextFloat() * 0.5F + 0.5F, par13
				                + ((float) Math.PI / 2F), par14 / 3.0F, par15,
				        par16, 1.0D);
				return;
			}

			if (flag || random.nextInt(4) != 0) {
				double d8 = par6 - d4;
				double d9 = par10 - d5;
				double d10 = (double) (par16 - par15);
				double d11 = (double) (par12 + 2.0F + 16.0F);

				if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
					return;
				}

				if (par6 >= d4 - 16.0D - d6 * 2.0D
				        && par10 >= d5 - 16.0D - d6 * 2.0D
				        && par6 <= d4 + 16.0D + d6 * 2.0D
				        && par10 <= d5 + 16.0D + d6 * 2.0D) {
					int l1 = MathHelper.floor_double(par6 - d6) - par3 * 16 - 1;
					int i2 = MathHelper.floor_double(par6 + d6) - par3 * 16 + 1;
					int j2 = MathHelper.floor_double(par8 - d7) - 1;
					int k2 = MathHelper.floor_double(par8 + d7) + 1;
					int l2 = MathHelper.floor_double(par10 - d6) - par4 * 16
					        - 1;
					int i3 = MathHelper.floor_double(par10 + d6) - par4 * 16
					        + 1;

					if (l1 < 0) {
						l1 = 0;
					}

					if (i2 > 16) {
						i2 = 16;
					}

					if (j2 < 1) {
						j2 = 1;
					}

					if (k2 > 120) {
						k2 = 120;
					}

					if (l2 < 0) {
						l2 = 0;
					}

					if (i3 > 16) {
						i3 = 16;
					}

					boolean flag2 = false;
					int j3;
					int k3;

					for (j3 = l1; !flag2 && j3 < i2; ++j3) {
						for (int l3 = l2; !flag2 && l3 < i3; ++l3) {
							for (int i4 = k2 + 1; !flag2 && i4 >= j2 - 1; --i4) {
								k3 = (j3 * 16 + l3) * 128 + i4;

								if (i4 >= 0 && i4 < 128) {
									if (isOceanBlock(par5ArrayOfByte, k3, j3,
									        i4, l3, par3, par4)) {
										flag2 = true;
									}

									if (i4 != j2 - 1 && j3 != l1
									        && j3 != i2 - 1 && l3 != l2
									        && l3 != i3 - 1) {
										i4 = j2;
									}
								}
							}
						}
					}

					if (!flag2) {
						for (j3 = l1; j3 < i2; ++j3) {
							double d12 = ((double) (j3 + par3 * 16) + 0.5D - par6)
							        / d6;

							for (k3 = l2; k3 < i3; ++k3) {
								double d13 = ((double) (k3 + par4 * 16) + 0.5D - par10)
								        / d6;
								int j4 = (j3 * 16 + k3) * 128 + k2;
								boolean flag3 = false;

								if (d12 * d12 + d13 * d13 < 1.0D) {
									for (int k4 = k2 - 1; k4 >= j2; --k4) {
										double d14 = ((double) k4 + 0.5D - par8)
										        / d7;

										if (d14 > -0.7D
										        && d12 * d12 + d14 * d14 + d13
										                * d13 < 1.0D) {
											if (isTopBlock(par5ArrayOfByte, j4,
											        j3, k4, k3, par3, par4)) {
												flag3 = true;
											}

											digBlock(par5ArrayOfByte, j4, j3,
											        k4, k3, par3, par4, flag3);
										}

										--j4;
									}
								}
							}
						}

						if (flag) {
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Recursively called by generate() (generate) and optionally by itself.
	 */
	protected void recursiveGenerate(World par1World, int par2, int par3,
	        int par4, int par5, byte[] par6ArrayOfByte) {
		int i1 = this.rand
		        .nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);

		if (this.rand.nextInt(15) != 0) {
			i1 = 0;
		}

		for (int j1 = 0; j1 < i1; ++j1) {
			double d0 = (double) (par2 * 16 + this.rand.nextInt(16));
			double d1 = (double) this.rand.nextInt(this.rand.nextInt(120) + 8);
			double d2 = (double) (par3 * 16 + this.rand.nextInt(16));
			int k1 = 1;

			if (this.rand.nextInt(4) == 0) {
				this.generateLargeCaveNode(this.rand.nextLong(), par4, par5,
				        par6ArrayOfByte, d0, d1, d2);
				k1 += this.rand.nextInt(4);
			}

			for (int l1 = 0; l1 < k1; ++l1) {
				float f = this.rand.nextFloat() * (float) Math.PI * 2.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

				if (this.rand.nextInt(10) == 0) {
					f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F
					        + 1.0F;
				}

				this.generateCaveNode(this.rand.nextLong(), par4, par5,
				        par6ArrayOfByte, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
			}
		}
	}

	protected boolean isOceanBlock(byte[] data, int index, int x, int y, int z,
	        int chunkX, int chunkZ) {
		return data[index] == Block.waterMoving.blockID
		        || data[index] == Block.waterStill.blockID;// LEAVE THIS BLOCK
														   // !!!!!!
	}

	// Exception biomes to make sure we generate like vanilla
	private boolean isExceptionBiome(BiomeGenBase biome) {
		if (biome == BiomeGenBase.mushroomIsland)
			return true;
		if (biome == BiomeGenBase.beach)
			return true;
		if (biome == BiomeGenBase.desert)
			return true;
		return false;
	}

	// Determine if the block at the specified location is the top block for the
	// biome, we take into account
	// Vanilla bugs to make sure that we generate the map the same way vanilla
	// does.
	private boolean isTopBlock(byte[] data, int index, int x, int y, int z,
	        int chunkX, int chunkZ) {
		// TADO RENAME ALL BLOCKS TO YOUR CUTOM BLOCKS AND STONE THATS USED
		// UNDERGROUND
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z
		        + chunkZ * 16);
		return (isExceptionBiome(biome) ? data[index] == (byte) Block.grass.blockID
		        : data[index] == biome.topBlock);
	}

	/**
	 * Digs out the current block, default implementation removes stone, filler,
	 * and top block Sets the block to lava if y is less then 10, and air other
	 * wise. If setting to air, it also checks to see if we've broken the
	 * surface and if so tries to make the floor the biome's top block
	 * 
	 * @param data
	 *            Block data array
	 * @param index
	 *            Pre-calculated index into block data
	 * @param x
	 *            local X position
	 * @param y
	 *            local Y position
	 * @param z
	 *            local Z position
	 * @param chunkX
	 *            Chunk X position
	 * @param chunkZ
	 *            Chunk Y position
	 * @param foundTop
	 *            True if we've encountered the biome's top block. Ideally if
	 *            we've broken the surface.
	 */
	protected void digBlock(byte[] data, int index, int x, int y, int z,
	        int chunkX, int chunkZ, boolean foundTop) {
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z
		        + chunkZ * 16);
		int top = (isExceptionBiome(biome) ? (byte) Block.grass.blockID
		        : biome.topBlock);
		int filler = (isExceptionBiome(biome) ? (byte) Block.dirt.blockID
		        : biome.fillerBlock);
		int block = data[index];

		if (block == (byte) Block.stone.blockID || block == filler
		        || block == top) {
			if (y < 10) {
				data[index] = (byte) Block.lavaMoving.blockID;
			} else {
				data[index] = 0;

				if (foundTop && data[index - 1] == filler) {
					data[index - 1] = (byte) top;
				}
			}
		}
	}
}