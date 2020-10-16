package security;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CryptoOutputStream extends FilterOutputStream {
	
	protected Hash hash;
	protected byte[] oneTimeKey;
	private byte[] encodedMessageBuffer;
	
	private int bufferPointer;

	public CryptoOutputStream(OutputStream stream, byte[] oneTimeKey, Hash hash) {
		super(stream);
		this.oneTimeKey = oneTimeKey;
		this.hash = hash;
		encodedMessageBuffer = new byte[hash.getNumberOfnDataBytes()];
		bufferPointer = 0;
	}

	@Override
	public void write(int b) throws IOException {
		encodedMessageBuffer[bufferPointer++] = (byte) b;
		
//		this.oneTimeKey = oneTimeKey;
//		this.hash = hash;
//		encodedMessageBuffer = new byte[hash.getNumberOfnDataBytes()];
//		bufferPointer = 0;
		
		
		if (bufferPointer == encodedMessageBuffer.length) {
			
			bufferPointer = 0;
//			System.out.println(encodedMessageBuffer.length);
			
			write(encodedMessageBuffer, 0, encodedMessageBuffer.length);
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		
		byte[] buffer = new byte[len];
		
		System.arraycopy(b, off, buffer, 0, len);
		
		byte[] messageData = hash.pack(buffer);
		
		messageData = OneTimeKey.xor(messageData, oneTimeKey);
		
		out.write(messageData);
		
	//	System.out.println("messsageData");
	}

	

	@Override
	public void flush() throws IOException {
		if (bufferPointer != 0)
			shallowFlush();
		super.flush();
	}
	
	
	
	
	
	
	
	
	
	protected 
	void shallowFlush() throws IOException {
		if (bufferPointer != 0) {
			
			write(encodedMessageBuffer, 0, bufferPointer);
			
			bufferPointer = 0;
		}
	}
}
