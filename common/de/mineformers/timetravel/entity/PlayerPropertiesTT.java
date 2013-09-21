package de.mineformers.timetravel.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * TimeTravel
 * 
 * PlayerPropertiesTT
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class PlayerPropertiesTT implements IExtendedEntityProperties {

	public static final String IDENTIFIER = "TimeTravelProperties";

	private long timeStarted;
	private int secondsAvail;
	private int secondsLeft;
	private int tmDimension;
	private Vec3 tmCoordinates;

	public PlayerPropertiesTT() {
		setTmData(0, 0, 0, 0);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setLong("TimeStarted", timeStarted);
		compound.setInteger("SecondsAvail", secondsAvail);
		compound.setInteger("SecondsLeft", secondsLeft);
		compound.setIntArray("TMData", new int[] { tmDimension,
		        (int) tmCoordinates.xCoord, (int) tmCoordinates.yCoord,
		        (int) tmCoordinates.zCoord });
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		timeStarted = compound.getLong("TimeStarted");
		secondsAvail = compound.getInteger("SecondsAvail");
		secondsLeft = compound.getInteger("SecondsLeft");

		int[] data = compound.getIntArray("TMData");
		setTmData(data[0], data[1], data[2], data[3]);
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public long getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(long timeStarted) {
		this.timeStarted = timeStarted;
	}

	public int getSecondsAvail() {
		return secondsAvail;
	}

	public void setSecondsAvail(int secondsAvail) {
		this.secondsAvail = secondsAvail;
	}

	public int getSecondsLeft() {
		return secondsLeft;
	}

	public void setSecondsLeft(int secondsLeft) {
		this.secondsLeft = secondsLeft;
	}

	public Vec3 getTmCoordinates() {
		return tmCoordinates;
	}

	public int getTmDimension() {
		return tmDimension;
	}

	public void setTmData(int dim, int x, int y, int z) {
		this.tmDimension = dim;
		this.tmCoordinates = Vec3.createVectorHelper(x, y, z);
	}

	public static PlayerPropertiesTT getByEntity(EntityPlayer player) {
		if (player.getExtendedProperties(IDENTIFIER) == null)
			player.registerExtendedProperties(IDENTIFIER,
			        new PlayerPropertiesTT());

		return (PlayerPropertiesTT) player.getExtendedProperties(IDENTIFIER);
	}

}