package CustomOreGen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;

public class CustomPacketPayload {
	public final PayloadType type;
	public final Serializable data;
	private static Map _xpacketMap = new HashMap();
	private static AtomicInteger _xpacketNextID = new AtomicInteger(0);
	private static final String CHANNEL = "CustomOreGen";
	private static final String XCHANNEL = "CustomOreGenX";
	private static final int MAX_SIZE = 32000;

	public CustomPacketPayload(PayloadType var1,
			Serializable var2) {
		this.type = var1;
		this.data = var2;
	}

	public void sendToServer() {
		Packet250CustomPayload[] var1 = this.createPackets();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			Packet250CustomPayload var4 = var1[var3];
			ModLoader.sendPacket(var4);
		}
	}

	public void sendToClient(NetServerHandler var1) {
		Packet250CustomPayload[] var2 = this.createPackets();
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			Packet250CustomPayload var5 = var2[var4];
			ModLoader.serverSendPacket(var1, var5);
		}
	}

	public void sendToAllClients() {
		Packet250CustomPayload[] var1 = this.createPackets();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			Packet250CustomPayload var4 = var1[var3];
			MinecraftServer.getServer().getConfigurationManager()
					.sendPacketToAllPlayers(var4);
		}
	}

	private Packet250CustomPayload[] createPackets() {
		Object var1 = null;
		boolean var2 = false;
		byte[] var11;

		try {
			AutoCompressionStream var3 = new AutoCompressionStream(1024);
			ObjectOutputStream var4 = new ObjectOutputStream(var3);
			var4.writeByte((byte) this.type.ordinal());
			var4.writeObject(this.data);
			var4.close();
			var3.close();
			var11 = var3.toByteArray();
			var2 = var3.isCompressed();
		} catch (IOException var10) {
			throw new RuntimeException(var10);
		}

		if (!var2) {
			return new Packet250CustomPayload[] { new Packet250CustomPayload(
					"CustomOreGen", var11) };
		} else {
			int var12 = (var11.length + 32000 - 1) / 32000;
			Packet250CustomPayload[] var13 = new Packet250CustomPayload[var12];
			int var5 = _xpacketNextID.incrementAndGet();
			int var6 = 1;

			for (int var7 = 0; var6 <= var12; ++var6) {
				int var8 = Math.min(32000, var11.length - var7);
				byte[] var9 = new byte[8 + var8];
				var9[0] = (byte) var5;
				var9[1] = (byte) (var5 >> 8);
				var9[2] = (byte) (var5 >> 16);
				var9[3] = (byte) (var5 >> 24);
				var9[4] = (byte) var12;
				var9[5] = (byte) (var12 >> 8);
				var9[6] = (byte) var6;
				var9[7] = (byte) (var6 >> 8);
				System.arraycopy(var11, var7, var9, 8, var8);
				var7 += var8;
				var13[var6 - 1] = new Packet250CustomPayload("CustomOreGenX",
						var9);
			}

			return var13;
		}
	}

	public static CustomPacketPayload decodePacket(Packet250CustomPayload var0) {
		try {
			Object var1 = null;

			if (var0.channel.equals("CustomOreGenX")) {
				int var2 = var0.data[0] & 255;
				var2 |= (var0.data[1] & 255) << 8;
				var2 |= (var0.data[2] & 255) << 16;
				var2 |= (var0.data[3] & 255) << 24;
				int var3 = var0.data[4] & 255;
				var3 |= (var0.data[5] & 255) << 8;
				int var4 = var0.data[6] & 255;
				var4 |= (var0.data[7] & 255) << 8;

				if (var3 > 1) {
					Map var5 = _xpacketMap;

					synchronized (_xpacketMap) {
						ByteArrayOutputStream var6 = (ByteArrayOutputStream) _xpacketMap
								.get(Integer.valueOf(var2));

						if (var6 == null) {
							var6 = new ByteArrayOutputStream(32000 * (var3 + 1));
							_xpacketMap.put(Integer.valueOf(var2), var6);
						}

						if (var6.size() != (var4 - 1) * 32000) {
							throw new RuntimeException(
									"Packet # "
											+ var4
											+ "/"
											+ var3
											+ " in group "
											+ var2
											+ " does not match next position in buffer "
											+ (var6.size() / 32000 + 1));
						}

						var6.write(var0.data, 8, var0.data.length - 8);

						if (var4 < var3) {
							return null;
						}

						_xpacketMap.remove(Integer.valueOf(var2));
						var6.close();
						var1 = new InflaterInputStream(
								new ByteArrayInputStream(var6.toByteArray()));
					}
				} else {
					var1 = new InflaterInputStream(new ByteArrayInputStream(
							var0.data, 8, var0.data.length - 8));
				}
			} else {
				if (!var0.channel.equals("CustomOreGen")) {
					CustomOreGenBase.log
							.warning("Invalid custom packet channel: \'"
									+ var0.channel + "\'");
					return null;
				}

				var1 = new ByteArrayInputStream(var0.data);
			}

			TranslatingObjectInputStream var10 = new TranslatingObjectInputStream(
					(InputStream) var1);
			PayloadType var11 = PayloadType
					.values()[var10.readByte()];
			Serializable var12 = (Serializable) var10.readObject();
			return new CustomPacketPayload(var11, var12);
		} catch (Exception var9) {
			CustomOreGenBase.log
					.warning("Error while decoding custom packet payload: "
							+ var9.getMessage());
			return null;
		}
	}

	public static void registerChannels(BaseMod var0) {
		ModLoader.registerPacketChannel(var0, "CustomOreGen");
		ModLoader.registerPacketChannel(var0, "CustomOreGenX");
	}

	public enum PayloadType {
		DebuggingGeometryRequest, DebuggingGeometryReset, DebuggingGeometryData, DebuggingGeometryRenderMode, MystcraftSymbolData, CommandResponse;
	}


	private static class AutoCompressionStream extends OutputStream {
		private int compressionThreshold;
		private ByteArrayOutputStream backingStream;
		private DeflaterOutputStream compressionStream;

		public AutoCompressionStream(int var1) {
			this.compressionThreshold = var1;
			this.backingStream = new ByteArrayOutputStream();
			this.compressionStream = null;
		}

		public void write(int b) throws IOException {
			if (compressionStream != null)
				compressionStream.write(b);
			else if (backingStream.size() < compressionThreshold) {
				backingStream.write(b);
			} else {
				byte data[] = backingStream.toByteArray();
				backingStream.reset();
				compressionStream = new DeflaterOutputStream(backingStream,
						new Deflater(9));
				compressionStream.write(data);
				compressionStream.write(b);
			}
		}

		public void close() throws IOException {
			if (this.compressionStream != null) {
				this.compressionStream.close();
			}

			this.backingStream.close();
		}

		public void flush() throws IOException {
			if (this.compressionStream != null) {
				this.compressionStream.flush();
			}

			this.backingStream.flush();
		}

		public boolean isCompressed() {
			return this.compressionStream != null;
		}

		public byte[] toByteArray() throws IOException {
			this.flush();
			return this.backingStream.toByteArray();
		}
	}

	public static class TranslatingObjectInputStream extends ObjectInputStream {
		protected Class resolveClass(ObjectStreamClass desc)
				throws IOException, ClassNotFoundException {
			try {
				return super.resolveClass(desc);
			} catch (ClassNotFoundException var3) {
				return desc.getName().startsWith("net.minecraft.src.") ? CustomOreGenBase.class
						.getClassLoader().loadClass(
								desc.getName().substring(18))
						: CustomOreGenBase.class.getClassLoader().loadClass(
								"net.minecraft.src." + desc.getName());
			}
		}

		public TranslatingObjectInputStream(InputStream in) throws IOException {
			super(in);
		}
	}

}
