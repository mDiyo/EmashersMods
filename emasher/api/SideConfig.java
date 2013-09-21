package emasher.api;

import net.minecraft.nbt.NBTTagCompound;

public class SideConfig
{
	public int tank;
	public int inventory;
	public boolean[] rsControl;
	public boolean[] rsLatch;
	public int meta;
	
	public SideConfig(int t, int i, int m)
	{
		tank = t;
		inventory = i;
		rsControl = new boolean[]{false, false, false};
		rsLatch = new boolean[]{false, false, false};
		meta = m;
	}
	
	public SideConfig()
	{
		tank = -1;
		inventory = -1;
		rsControl = new boolean[]{false, false, false};
		rsLatch = new boolean[]{false, false, false};
		meta = 0;
	}
	
	public void writeToNBT(NBTTagCompound data)
	{
		data.setInteger("tank", tank);
		data.setInteger("inventory", inventory);
		for(int i = 0; i < 3; i++)
		{
			data.setBoolean("rsControl" + i, rsControl[i]);
			data.setBoolean("rsLatch" + i, rsLatch[i]);
		}
		data.setInteger("meta", meta);
	}
	
	public void readFromNBT(NBTTagCompound data)
	{
		tank = data.getInteger("tank");
		inventory = data.getInteger("inventory");
		for(int i = 0; i < 3; i++)
		{
			rsControl[i] = data.getBoolean("rsControl" + i);
			rsLatch[i] = data.getBoolean("rsLatch" + i);
		}
		meta = data.getInteger("meta");
	}
}
