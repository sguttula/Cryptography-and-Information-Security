package security;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CryptoInputStream extends FilterInputStream {
	
	
	protected Hash hash;
	
	protected byte[] oneTimeKey;
	
	private byte[] messageBuffer;
	
	private int bufferPointer;
	

	public CryptoInputStream(InputStream stream, byte oneTimeKey[], Hash hash) {
		
		super(stream);
		
		this.oneTimeKey = oneTimeKey;
		
		this.hash = hash;
		
		bufferPointer = 0;
		
	}

	@Override
	public int read() throws IOException {
		
		if (bufferPointer == 0) {
			int i = 0;
			
//			this.oneTimeKey = oneTimeKey;
//			
//			this.hash = hash;
//			
//			bufferPointer = 0;
			
			byte[] buffer = new byte[hash.getPacketSize()];
			
			for (int k = 0; k < hash.getPacketSize(); k++) {
				
				int m = super.in.read();
				if (m == -1) {
					
					if (k == 0) {
						
						
						
						
						return -1;
					}
					throw new IOException("data not sliced");
				}
				buffer[(i++)] = ((byte) m);
			}
			buffer = OneTimeKey.xor(buffer, oneTimeKey);
			
			messageBuffer = hash.unpack(buffer);
		}
		
		int a = messageBuffer[bufferPointer];
		
		bufferPointer = ((bufferPointer + 1) % messageBuffer.length);
		
		return a;
	}

	@Override
	public int available() throws IOException {
		
		return super.available() / hash.getPacketSize() * hash.getNumberOfnDataBytes();
	}
}
