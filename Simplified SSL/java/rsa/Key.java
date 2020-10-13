package rsa;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Key {

	protected BigInteger key;
	protected BigInteger n; 

	private static final BigInteger zero = BigInteger.ZERO;

	public Key() {
		this(zero, zero);
	}

	public Key(BigInteger a1, BigInteger a2) {
		this.key = a1;
		this.n = a2;
	}

	public BigInteger getKey() {
		return key;
	}

	public BigInteger getN() {
		return n;
	}


	public void read(byte m[]) throws IOException {
		
		read(new ByteArrayInputStream(m));
	}

	public void read(InputStream input) throws IOException, NumberFormatException {
		
		StringBuilder textBuilder = new StringBuilder();
		try (Reader reader = new BufferedReader(new InputStreamReader(input, Charset.forName(StandardCharsets.UTF_8.name())))) {
			int c = 0;
			
			
			
			while ((c = reader.read()) != -1) {
				
				
				
				textBuilder.append((char) c);
			}
		}
		int leftBrace = textBuilder.indexOf("{");
		
		
		key = new BigInteger(textBuilder.substring(leftBrace + 1, textBuilder.indexOf(",")));
		
		
		n = new BigInteger(textBuilder.substring(textBuilder.indexOf(",") + 1, textBuilder.indexOf("}")));
	}

	public String toString() {
		
		return "{" + key + ", " + n + "}";
	}

}
