package emasher.sockets.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import emasher.sockets.modules.ModBlockPlacer;

public class ClientPacketHandler implements IPacketHandler
{
	public static ClientPacketHandler instance = new ClientPacketHandler();
	public final static String networkChannel = "Emasher_Sockets";
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		try
		{
			if(packet.channel.equals(networkChannel))
			{
				if(packet.data[0] == 0)
				{
					int x = toInteger(packet.data, 10);
					int y = toInteger(packet.data, 14);
					int z = toInteger(packet.data, 18);
					int side = packet.data[1];
					
					World world = ((EntityPlayer) player).worldObj;
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if(te != null && te instanceof TileSocket)
					{
						TileSocket ts = (TileSocket)te;
						
						SideConfig c = ts.configs[side];
						
						c.meta = toInteger(packet.data, 6);
						
						ts.sides[side] = (int)packet.data[1];
						c.tank = (int)packet.data[2];
						c.inventory = (int)packet.data[3];
						c.rsControl[0] = (packet.data[4] & 1) != 0;
						c.rsControl[1] = (packet.data[4] & 2) != 0;
						c.rsControl[2] = (packet.data[4] & 4) != 0;
						c.rsLatch[0] = (packet.data[5] & 1) != 0;
						c.rsLatch[1] = (packet.data[5] & 2) != 0;
						c.rsLatch[2] = (packet.data[5] & 4) != 0;
						ts.sides[side] = toInteger(packet.data, 22);
						ts.sideLocked[side] = byteToBool(packet.data[26]);
						ts.facID[side] = toInteger(packet.data, 27);
						ts.facMeta[side] = toInteger(packet.data, 31);
						
						world.markBlockForUpdate(x, y, z);
						world.notifyBlockChange(x, y, z, SocketsMod.socket.blockID);
					}
				}
				else if(packet.data[0] == 1)
				{
					int id = toInteger(packet.data, 1);
					int damage = toInteger(packet.data, 5);
					int x = toInteger(packet.data, 9);
					int y = toInteger(packet.data, 13);
					int z = toInteger(packet.data, 17);
					int inventory = packet.data[22];
					int size = toInteger(packet.data, 23);
					
					World world = ((EntityPlayer) player).worldObj;
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if(te != null && te instanceof TileSocket)
					{
						TileSocket ts = (TileSocket)te;
						
						if(id != -1) ts.inventory.setInventorySlotContents(inventory, new ItemStack(id, size, damage));
						else ts.inventory.setInventorySlotContents(inventory, null);
						
						world.markBlockForUpdate(x, y, z);
						world.notifyBlockChange(x, y, z, SocketsMod.socket.blockID);
					}
				}
				else if(packet.data[0] == 2)
				{
					int x = toInteger(packet.data, 1);
					int y = toInteger(packet.data, 5);
					int z = toInteger(packet.data, 9);
					int id = toInteger(packet.data, 13);
					int meta = toInteger(packet.data, 17);
					int amnt = toInteger(packet.data, 22);
					int tank = (int)packet.data[26];
					
					World world = ((EntityPlayer) player).worldObj;
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if(te != null && te instanceof TileSocket)
					{
						TileSocket ts = (TileSocket)te;
						
						ts.tanks[tank].setFluid(new FluidStack(id, amnt));
						
						world.markBlockForUpdate(x, y, z);
						world.notifyBlockChange(x, y, z, SocketsMod.socket.blockID);
					}
				}
				else if(packet.data[0] == 3)
				{
					int x = toInteger(packet.data, 1);
					int y = toInteger(packet.data, 5);
					int z = toInteger(packet.data, 9);
					int side = (int)packet.data[13];
					
					World world = ((EntityPlayer) player).worldObj;
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if(te != null && te instanceof TileSocket)
					{
						TileSocket ts = (TileSocket)te;
						
						SocketModule m = ts.getSide(ForgeDirection.getOrientation(side));
						if(m instanceof ModBlockPlacer)
						{
							((ModBlockPlacer)m).doClick(ts.configs[side], ts, ForgeDirection.getOrientation(side));
						}
					}
				}
			}
		}				
		catch (Exception e)
		{
			System.err.println("[Engineer's Toolbox] Network Error");
			
		}
	}
	
	public void requestSideData(TileSocket ts, byte side)
	{
		byte[] out = new byte[18];
		
		out[0] = 0;
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, ts.worldObj.provider.dimensionId, 13);
		out[13] = side;
		
		PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(networkChannel, out));
	}
	
	public void requestInventoryData(TileSocket ts, byte inventory)
	{
		byte[] out = new byte[18];
		
		out[0] = 1;
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, ts.worldObj.provider.dimensionId, 13);
		out[13] = inventory;
		
		PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(networkChannel, out));
	}
	
	public void requestTankData(TileSocket ts, byte tank)
	{
		byte[] out = new byte[18];
		
		out[0] = 2;
		toByte(out, ts.xCoord, 1);
		toByte(out, ts.yCoord, 5);
		toByte(out, ts.zCoord, 9);
		toByte(out, ts.worldObj.provider.dimensionId, 13);
		out[13] = tank;
		
		PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(networkChannel, out));
	}
	
	private void toByte(byte[] out, int in, int start)
	{
		out[start++] = (byte) (in >> 24);
		out[start++] = (byte) (in >> 16);
		out[start++] = (byte) (in >> 8);
		out[start++] = (byte) in;
	}
	
	private static int toInteger(byte[] in, int start)
	{
		int value = 0;
		for (int i = start; i < start + 4; i++) {
			value = (value << 8) + (in[i] & 0xFF);
		}
		return value;
	}
	
	private static boolean byteToBool(byte b)
	{
		if(b == 0) return false;
		return true;
	}
}
