package emasher.sockets.modules;

import java.util.List;

import buildcraft.api.power.PowerHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;

public class ModWaterMill extends SocketModule
{
	public ModWaterMill(int id)
	{
		super(id, "sockets:waterMill");
	}

	@Override
	public String getLocalizedName()
	{
		return "Hydroelectric Turbine";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Generates power in a river");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_AQUA + "Generates 1 MJ/t");
		l.add("Can only be installed on the sides of a socket");
		l.add("Only one can be installed per socket");
		l.add("Only generates power when in some sort of river biome");
		l.add("Only generates power for y = [64, 40]");
		l.add("Only generates power when adjacent to a water source block");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "sps", "sps", "sbs", Character.valueOf('s'), Block.pressurePlateIron, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('u'), Block.blockDiamond, Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 7));
	}
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) return false;
		ForgeDirection d;
		for(int i = 0; i < 6; i++)
		{
			d = ForgeDirection.getOrientation(i);
			if(d != side)
			{
				SocketModule m = ts.getSide(d);
				if(m.moduleID == this.moduleID) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
			if(ts.powerHandler.getMaxEnergyStored() - ts.powerHandler.getEnergyStored() >= 1.0F)
			{
				int xo = ts.xCoord + side.offsetX;
				int yo = ts.yCoord + side.offsetY;
				int zo = ts.zCoord + side.offsetZ;
				
				int blockID = ts.worldObj.getBlockId(xo, yo, zo);
				if(blockID == Block.waterStill.blockID || blockID == Block.waterMoving.blockID)
				{
					int biomeID = ts.worldObj.getBiomeGenForCoords(xo, zo).biomeID;
					String biomeName = ts.worldObj.getBiomeGenForCoords(xo, zo).biomeName;
					if(biomeID == BiomeGenBase.river.biomeID || biomeID == BiomeGenBase.frozenRiver.biomeID || biomeName.contains("iver") || biomeName.contains("tream"))
						if(ts.yCoord <= 64 && ts.yCoord >= 40)
							if(side != ForgeDirection.UP && side != ForgeDirection.DOWN) ts.powerHandler.getPowerReceiver().receiveEnergy(PowerHandler.Type.PIPE, 1.0F, side);
				}
			}
	}
}
