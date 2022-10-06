import java.math.BigInteger;

/**
 * @author Xunhua Wang. All rights reserved. Modified by Thomas Ramey and Seth Roper
 * @date 02/16/2012; revised on 09/27/2018; further refined on 09/20/2019 and 09/29/2020
 * 
 */

public class RameyThomasRoperSethRSA2
{
	public BigInteger gcd (BigInteger inE, BigInteger inZ) {
		while (inZ.compareTo(BigInteger.ZERO) != 0)
		{
			BigInteger temp = inZ;
			inZ = inE.mod(inZ);
			inE = temp;
		}
		return inE;
	}

	public void testGcd () {
		BigInteger result1 = gcd (BigInteger.valueOf(29), BigInteger.valueOf(288));
		BigInteger result2 = gcd (BigInteger.valueOf(30), BigInteger.valueOf(288));
		System.out.println ("GCD (29, 288) = 0x" + result1.toString(16));
		System.out.println ("GCD (30, 288) = 0x" + result2.toString(16));
	}

	//
	// We assume that inE < inZ
	// This implementation follows our slides
	// Return:
	//	-1: no inverse
	//	inverse of inE mod inZ
	//
	public BigInteger xgcd (BigInteger inE, BigInteger inZ) {
		// TO BE FINISHED
		// Must implement the extended Euclidean algorithm
		// NO brute-forcing; violation will lead to zero points
		// NO recursion; violation will lead to zero points

		// covered just in case even tho INZ SHOULD NEVER BE 1
		if (inZ.compareTo(BigInteger.ONE) == 0) {
			return BigInteger.ZERO;
		}

		BigInteger reserve = inZ;
		BigInteger t = BigInteger.ZERO, s = BigInteger.ONE;

		// default d is inE
		// while (inE > 1) {
		while (inE.compareTo(BigInteger.ONE) == -1) { //inE > 1?

			BigInteger q = inE.divide(inZ);
			BigInteger d = inZ;
			inZ = inE.mod(inZ);
			inE = d;
			d = t;
			t = s .subtract(q.multiply(t)) ;
			s = d;
		}

		// negative mod
		// if (s < 0)
		if (s.compareTo(BigInteger.ZERO) == 1) // s < 0??
			//s+= reserve;
			s = s.add(reserve);

		return s;
	}

	public void testXgcd () {
		BigInteger result1 = xgcd (BigInteger.valueOf(29), BigInteger.valueOf(288));
		BigInteger result2 = xgcd (BigInteger.valueOf(149), BigInteger.valueOf(288));

		System.out.println ("29^-1 mod 288 = 0x" + result1.toString(6));
		System.out.println ("149^-1 mod 288 = 0x" + result2.toString(6));
	}

	public BigInteger[] keygen (BigInteger inP, BigInteger inQ, BigInteger inE) {
		BigInteger[] keys = new BigInteger[3];
		keys[0] = inE;
		keys[1] = inP.multiply(inQ);
		// int z = (inP - 1) * (inQ - 1);
		BigInteger z = (inP.subtract(BigInteger.ONE)) .multiply( (inQ.subtract(BigInteger.ONE)));
		keys[2]  = xgcd(inE, z);
		return keys;
		
	}

	//
	// The following method will return an integer array, with [e, N, d] in this order
	//
	public void testKeygen () {
		// BigInteger[] keypair = keygen (17, 19, 29);
		BigInteger[] keypair = keygen (BigInteger.valueOf(17), BigInteger.valueOf(19), BigInteger.valueOf(29));

		System.out.println ("e = 0x" + keypair[0].toString(16));
		System.out.println ("N = 0x" + keypair[1].toString(16));
		System.out.println ("d = 0x" + keypair[2].toString(16));
	}

	//
	// Calculate c = a^b mod n, with the square-and-multiply algorithm
	//
	// The following method implements the square-and-multiply algorithm, with Java primitive types
	//
	// Note that even with primitive types, a^b may well exceed the range of Java int
	// For example, 5^20 is too big to be held by a Java primitive integer
	//

	//NEW IMPLEMENTATION
	public int modExpTwo (BigInteger a, BigInteger b, BigInteger n) {
		// BigInteger aBigInt = BigInteger.valueOf ((long) a);
		// BigInteger bBigInt = BigInteger.valueOf ((long) b);
		// BigInteger nBigInt = BigInteger.valueOf ((long) n);

		// BigInteger resBigInt = aBigInt.modPow (bBigInt, nBigInt);
		// int res = resBigInt.intValue ();
		// return res;


		// BigInteger aBigInt = BigInteger.valueOf ((long) a);
		// BigInteger bBigInt = BigInteger.valueOf ((long) b);
		// BigInteger nBigInt = BigInteger.valueOf ((long) n);

		BigInteger resBigInt = a.modPow (b, n);
		int res = resBigInt.intValue ();
		return res;
}



	public int encrypt (int message, BigInteger inE, BigInteger inN) {
		return modExpTwo(BigInteger.valueOf(message), inE, inN); //Not too sure on this
	}
	

	//Appears to be not working
	public int decrypt (int ciphertext, BigInteger inD, BigInteger inN) {
		      return modExpTwo(BigInteger.valueOf(ciphertext), inD, inN); //Not too sure on this
	}

	public void testRSA () {
		BigInteger[] keypair = keygen (BigInteger.valueOf(17), BigInteger.valueOf(19), BigInteger.valueOf(29));

		int m1 = 4;
		int c1 = encrypt (m1, keypair[0], keypair[1]);
		System.out.println ("The encryption of (m1=0x" + Integer.toString(m1, 16) + ") is 0x" + Integer.toString(c1,16));
		int cleartext1 = decrypt (c1, keypair[2], keypair[1]);
		System.out.println ("The decryption of (c=0x" + Integer.toString(c1,16) + ") is 0x" + Integer.toString(cleartext1, 16));

		int m2 = 5;
		int c2 = encrypt (m2, keypair[0], keypair[1]);
		System.out.println ("The encryption of (m2=0x" + Integer.toString(m2, 16) + ") is 0x" + Integer.toString(c2, 16));
		int cleartext2 = decrypt (c2, keypair[2], keypair[1]);
		System.out.println ("The decryption of (c2=0x" + Integer.toString(c2, 16) + ") is 0x" + Integer.toString(cleartext2, 16));
	}

	public static void main (String[] args) {
		RameyThomasRoperSethRSA2 atrsa = new RameyThomasRoperSethRSA2 ();

		System.out.println ("********** Project 2 output begins ********** ");

		atrsa.testGcd ();
		atrsa.testXgcd ();
		atrsa.testKeygen ();
		atrsa.testRSA ();
	}
}
