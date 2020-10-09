# Project 1
## Simplified DES Block cipher
----------------------------------------------------------------------------------------------------------------
### 1. SDES

In this section, you will implement a simplified version of the DES block cipher algorithm. Naturally enough, it is called SDES, and it is designed to have the features of the DES algorithm but scaled down so it is more tractable to understand. (Note however, that SDES is in no way secure and should not be used for serious cryptographic applications.) Here is the detailed specifications of SDES.


SDES encryption takes a 10 bit raw key (from which two 8 bit keys are generated as describedin the handout) and encrypts an 8 bit plaintext to produce an 8 bit ciphertext.
Implement the SDES algorithm in a class called SDES. The encryption an decryption methods should match the interface below:
 
public static byte[] Encrypt(byte[] rawkey, byte[] plaintext)
public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext)
 
Here, rather than compactly representing the SDES plaintext and ciphertext using byte-sized (8-bit) variables, the data is represented using byte arrays of length 8. Similarly the 10 bit keys are represented as arrays of length 10. Although this design is extremely inefficient (it uses 8 times more space), it makes the algorithm easier to implement and experiment with.
For example, one might declare a 10-bit raw key in a test program like this:
byte key1[] = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0};
To verify that your implementation of SDES is correct, try the following test cases:

Raw Key           Plaintext             Ciphertext

0000000000     10101010            00010001

1110001110     10101010            11001010

1110001110     01010101            01110000

1111111111     10101010            00000100

Use your implementation to complete the following table:

Raw Key         Plaintext             Ciphertext
 
0000000000     00000000                 ?
1111111111     11111111                 ?
0000011111     00000000                 ?
0000011111     11111111                 ?
1000101110     ?                       00011100
1000101110     ?                       11000010
0010011111     ?                       10011101
0010011111     ?                       10010000
 

Pleas check here for details of SDES.


### 2. TripleSDES
 
The DES algorithm uses keys of length 56 bits, which, when DES was originally designed, was thought to be secure enough to meet most needs. However, the increase in computing power makes it more tractable to brute-force crack a 56-bit key. Thus, an alternative version of DES using longer keys was desirable. The result, known as Triple DES uses two 56-bit raw keys k1 and k2 and is implemented by composing DES with itself three times in the following way:
E3DES(p) = EDES(k1,DDES(k2,EDES(k1, p)))
Here, p is the plaintext to encrypt, EDES is the usual DES encryption algorithm and DDES is the DES decryption algorithm. This strategy doubles the number of bits in the key, at the expense of performing three times as many calculations. This approach was shown to offer only the security of a 57-bit key rather than 112 bits as you might expect.)
The TripleDES decryption algorithm is just the reverse:
D3DES(c) = DDES(k1,EDES(k2,DDES(k1, c)))
For this part of the project, implement a class called TripleSDES that provides the following methods and calculates the TripleSDES encryption.
 
public static byte[] Encrypt( byte[] rawkey1, byte[] rawkey2, byte[] plaintext )
public static byte[] Decrypt( byte[] rawkey1, byte[] rawkey2, byte[] ciphertext )
 
Use your implementation to complete the following table:
 
Raw Key 1        Raw Key        Plaintext         Ciphertext
 
0000000000     0000000000     00000000         ?
1000101110     0110101110     11010111         ?
1000101110     0110101110     10101010         ?
1111111111     1111111111     10101010         ?
1000101110     0110101110     ?                     11100110
1011101111     0110101110     ?                     01010000
0000000000     0000000000     ?                     10000000
1111111111     1111111111     ?                     10010010
 
### 3. Cracking SDES and TripleSDES
 
For this part of the project, you will use your SDES imlementation and brute-force search to crack some encoded English messages. This would be quite straightforward if the input text used standard ASCII encodings, because you can test each key to see if it generates output that is purely alphanumeric (almost all of the wrong outputs will contain random ASCII gibberish). To make the problem more interesting, the text in the messages here are encoded using Compact ASCII, or CASCII for short.
 
CASCII characters are 5 bits long, which gives just enough space for the upper case letters and some punctuation: 0 = ’ ’ (space), 1–26 = ’A’–’Z’, 27=’,’, 28 = ’?’, 29=’:’, 30=’.’, 31 = ’‘’. The file CASCII.java  provides a definition of CASCII constants and some useful conversion functions. See the file comments for details. Although it should not affect the code you write for the project, CASCII uses big-endian encodings. For example, the letter ’T’ = 0 is represented by the bit sequence 00101. This may be useful when debugging your programs.
Since SDES and TripleSDES only work on blocks of size 8 bits, when converting a CASCII string it is necessary to pad the bit representation with 0’s to obtain a multiple of 8. See the convert and toString methods in CASCII.java.
 
1). Give the SDES encoding of the following CASCII plaintext using the key 0111001101. (The answer
is 64 bits long.)
CRYPTOGRAPHY
 
2). The message in the file msg1.txt was encoded using SDES. Decrypt it, and find the 10-bit raw
key used for its encryption.
 
3). The mesage in the file msg2.txt was encoded using TripleSDES. Decrypt it, and find the two
10-bit raw keys used for its encryption.
