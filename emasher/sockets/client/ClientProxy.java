package emasher.sockets.client;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import emasher.sockets.CommonProxy;
import emasher.sockets.TileSocket;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers() 
	{
		TileEntityRenderer.instance.specialRendererMap.put(TileSocket.class, SocketRenderer.instance);
		SocketRenderer.instance.setTileEntityRenderer(TileEntityRenderer.instance);
	}
}
