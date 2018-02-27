package fr.slaynash.creatibox.common;

import java.nio.ByteBuffer;

public class BytesUtils {
	public static byte[] toBytes(int i){
		return new byte[]{
				(byte) (i >> 24),
				(byte) (i >> 16),
				(byte) (i >> 8),
				(byte) (i >> 0)
		};
	}
	
	public static int toInt(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}
	
	public static float toFloat(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getFloat(); //TODO use shift (FLOAT)
	}

	public static byte[] toBytes(float f) {
		return ByteBuffer.allocate(4).putFloat(f).array();
	}
}
