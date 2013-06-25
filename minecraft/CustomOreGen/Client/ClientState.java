package CustomOreGen.Client;

import CustomOreGen.CustomOreGenBase;
import CustomOreGen.CustomPacketPayload;
import CustomOreGen.CustomPacketPayload.PayloadType;
import CustomOreGen.GeometryData;
import CustomOreGen.GeometryRequestData;

import CustomOreGen.Client.ClientState$WireframeRenderMode;
import CustomOreGen.Util.GeometryStream;
import CustomOreGen.Util.GeometryStream$GeometryStreamException;
import CustomOreGen.Util.IGeometryBuilder$PrimitiveType;
import CustomOreGen.Util.Transform;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ClientState
{
    public static ClientState$WireframeRenderMode dgRenderingMode = ClientState$WireframeRenderMode.WIREFRAMEOVERLAY;
    public static boolean dgEnabled = true;
    private static World _world = null;
    private static int _dgScanCounter = 0;
    private static int _dgBatchID = 0;
    @SideOnly(Side.CLIENT)
    private static Map _dgListMap = new HashMap();
    @SideOnly(Side.CLIENT)
    private static Set _chunkDGRequests = new HashSet();
    @SideOnly(Side.CLIENT)
    private static IntBuffer _chunkDGListBuffer = null;

    @SideOnly(Side.CLIENT)
    public static void onRenderWorld(Entity var0, double var1)
    {
        if (_world != null && dgEnabled && dgRenderingMode != null && dgRenderingMode != ClientState$WireframeRenderMode.NONE)
        {
            double var3 = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * var1;
            double var5 = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * var1;
            double var7 = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * var1;

            if (_dgScanCounter == 0)
            {
                _dgScanCounter = 60;
                byte var9 = 16;
                int var10 = 50 - _chunkDGRequests.size() * 20;
                int var11 = (int)var3 / 16;
                int var12 = (int)var7 / 16;

                for (int var13 = 0; var10 > 0 && var13 <= var9; ++var13)
                {
                    int var14 = var11 - var13;
                    int var15 = var12 - var13;

                    for (int var16 = 0; var10 > 0 && var16 <= var13 * 8; ++var16)
                    {
                        if (var16 < var13 * 2)
                        {
                            ++var14;
                        }
                        else if (var16 < var13 * 4)
                        {
                            ++var15;
                        }
                        else if (var16 < var13 * 6)
                        {
                            --var14;
                        }
                        else if (var16 < var13 * 8)
                        {
                            --var15;
                        }
                        else if (var13 != 0)
                        {
                            continue;
                        }

                        long var17 = (long)var14 << 32 | (long)var15 & 4294967295L;

                        if (!_dgListMap.containsKey(Long.valueOf(var17)) && _chunkDGRequests.add(Long.valueOf(var17)))
                        {
                            GeometryRequestData var19 = new GeometryRequestData(_world, var14, var15, _dgBatchID);
                            (new CustomPacketPayload(PayloadType.DebuggingGeometryRequest, var19)).sendToServer();
                            --var10;
                        }
                    }
                }
            }
            else
            {
                --_dgScanCounter;
            }

            if (_chunkDGListBuffer != null)
            {
                GL11.glPushMatrix();
                GL11.glTranslated(-var3, -var5, -var7);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                if (dgRenderingMode == ClientState$WireframeRenderMode.WIREFRAMEOVERLAY)
                {
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                }

                if (dgRenderingMode != ClientState$WireframeRenderMode.POLYGON)
                {
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                }

                _chunkDGListBuffer.rewind();
                GL11.glCallLists(_chunkDGListBuffer);

                if (dgRenderingMode != ClientState$WireframeRenderMode.POLYGON)
                {
                    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                }

                if (dgRenderingMode == ClientState$WireframeRenderMode.WIREFRAMEOVERLAY)
                {
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                }

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glPopMatrix();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean hasWorldChanged(World var0)
    {
        return _world != var0;
    }

    @SideOnly(Side.CLIENT)
    public static void onWorldChanged(World var0)
    {
        _world = var0;
        CustomOreGenBase.log.finer("Client world changed to " + (_world == null ? null : _world.getWorldInfo().getWorldName()));
        clearDebuggingGeometry();
    }

    @SideOnly(Side.CLIENT)
    public static void addDebuggingGeometry(GeometryData var0)
    {
        if (_world != null)
        {
            if (var0.batchID == _dgBatchID)
            {
                if (var0.dimensionID == _world.provider.dimensionId)
                {
                    GeometryRenderer var1 = new GeometryRenderer();

                    try
                    {
                        Iterator var2 = var0.geometry.iterator();

                        while (var2.hasNext())
                        {
                            GeometryStream var3 = (GeometryStream)var2.next();
                            var3.executeStream(var1);
                        }
                    }
                    catch (GeometryStream$GeometryStreamException var8)
                    {
                        throw new RuntimeException(var8);
                    }

                    int var9 = GL11.glGenLists(1);

                    if (var9 != 0)
                    {
                        GL11.glNewList(var9, GL11.GL_COMPILE);
                        var1.setPositionTransform((new Transform()).translate((float)(var0.chunkX * 16 + 8), -1.0F, (float)(var0.chunkZ * 16 + 8)).scale(7.5F, 1.0F, 7.5F));
                        var1.setColor(new float[] {0.0F, 1.0F, 0.0F, 0.15F});
                        var1.setVertexMode(IGeometryBuilder$PrimitiveType.LINE, new int[] {1});
                        var1.addVertex(new float[] { -1.0F, 0.0F, -1.0F});
                        var1.addVertex(new float[] { -1.0F, 0.0F, 1.0F});
                        var1.addVertex(new float[] {1.0F, 0.0F, 1.0F});
                        var1.addVertex(new float[] {1.0F, 0.0F, -1.0F});
                        var1.addVertexRef(4);
                        var1.draw();
                        GL11.glEndList();
                        long var10 = (long)var0.chunkX << 32 | (long)var0.chunkZ & 4294967295L;
                        Integer var5 = (Integer)_dgListMap.get(Long.valueOf(var10));
                        int var6;

                        if (var5 != null && var5.intValue() != 0)
                        {
                            for (var6 = 0; var6 < _chunkDGListBuffer.limit(); ++var6)
                            {
                                if (_chunkDGListBuffer.get(var6) == var5.intValue())
                                {
                                    _chunkDGListBuffer.put(var6, var9);
                                    break;
                                }
                            }

                            GL11.glDeleteLists(var5.intValue(), 1);
                        }
                        else if (_chunkDGListBuffer == null)
                        {
                            _chunkDGListBuffer = ByteBuffer.allocateDirect(512).order(ByteOrder.nativeOrder()).asIntBuffer();
                            _chunkDGListBuffer.limit(1);
                            _chunkDGListBuffer.put(0, var9);
                        }
                        else
                        {
                            var6 = _chunkDGListBuffer.limit();

                            if (var6 == _chunkDGListBuffer.capacity())
                            {
                                IntBuffer var7 = _chunkDGListBuffer;
                                _chunkDGListBuffer = ByteBuffer.allocateDirect(var6 * 8).order(ByteOrder.nativeOrder()).asIntBuffer();
                                var7.rewind();
                                _chunkDGListBuffer.put(var7);
                            }

                            _chunkDGListBuffer.limit(var6 + 1);
                            _chunkDGListBuffer.put(var6, var9);
                        }

                        _dgListMap.put(Long.valueOf(var10), Integer.valueOf(var9));
                        _chunkDGRequests.remove(Long.valueOf(var10));
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void clearDebuggingGeometry()
    {
        Iterator var0 = _dgListMap.values().iterator();

        while (var0.hasNext())
        {
            Integer var1 = (Integer)var0.next();

            if (var1 != null && var1.intValue() != 0)
            {
                GL11.glDeleteLists(var1.intValue(), 1);
            }
        }

        _dgListMap.clear();
        _chunkDGRequests.clear();

        if (_chunkDGListBuffer != null)
        {
            _chunkDGListBuffer.limit(0);
        }

        dgEnabled = true;
        _dgScanCounter = (new Random()).nextInt(40);
        ++_dgBatchID;
    }

    @SideOnly(Side.CLIENT)
    public static void addMystcraftSymbol()
    {
    }
}
