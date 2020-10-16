package security;

public class Hash {
	private int nDataBytes;
	private int nCheckBytes;
	private byte pattern;
	private int k;

	public Hash(int nDataBytes, int nCheckBytes, byte pattern, int k) {
		this.nDataBytes = nDataBytes;
		
		this.nCheckBytes = nCheckBytes;
		
		this.pattern = pattern;
		
		this.k = k;
		
	}

	public static void main(String argv[]) throws Exception {
		if (argv.length != 5) {
			System.out.println("java security.Hash <nDataBytes> <nCheckBytes> <pattern> <k> <text> [ <text> ... ]");
		} else {
			
			byte[] packedData = pack(argv[4].getBytes(), Integer.parseInt(argv[0]), Integer.parseInt(argv[1]),
					(byte) Integer.parseInt(argv[2]), Integer.parseInt(argv[3]));
			System.out.println("packed Bytes");
			
			String packedString = new String(packedData);
			System.out.println(packedString);
			
			byte[] unpackedData = unpack(packedData, Integer.parseInt(argv[0]), Integer.parseInt(argv[1]),
					(byte) Integer.parseInt(argv[2]), Integer.parseInt(argv[3]));
			System.out.println("unpacked Bytes");
			
			String unpackedString = new String(unpackedData);
			System.out.println(unpackedString);
		}
	}

	public int getNumberOfnDataBytes() {
		return nDataBytes;
	}

	public int getPacketSize() {
		return this.nDataBytes + this.nCheckBytes + 1;
	}

	public byte[] pack(byte data[]) {
		byte checksum = 0;
		for (byte a : data) {
//			System.out.println(checksum);
			checksum += a & this.pattern;
		}
		
		checksum = (byte) ((checksum & 0xFF) * k % Math.pow(2, 8 * nCheckBytes));
		byte[] packedData = new byte[getPacketSize()];
		packedData[0] = (byte) data.length;
		System.arraycopy(data, 0, packedData, 1, data.length);
		packedData[getPacketSize() - 1] = checksum;
		return packedData;
	}

	public byte[] pack(byte data[], int nused) {
//	this.nDataBytes = nDataBytes;
//		
//		this.nCheckBytes = nCheckBytes;
//		
//		this.pattern = pattern;
//		
//		this.k = k;
//		
		byte[] ndata = new byte[nused];
//		System.out.println(nDataBytes);
		System.arraycopy(data, 0, ndata, 0, nused);
		return pack(ndata);
	}
	
	
	public byte[] unpack(byte data[]) {
		int n = data[0];
		byte[] nData = new byte[n];
		System.arraycopy(data, 1, nData, 0, n);
		return nData;
	}

	public static byte[] pack(byte data[], int ndb, int ncb, byte p, int k) {
		Hash h = new Hash(ndb, ncb, p, k);
		return h.pack(data);
	}

	

	public static byte[] unpack(byte data[], int ndb, int ncb, byte p, int k) {
	Hash h = new Hash(ndb, ncb, p, k);
		return h.unpack(data);
	}
}
