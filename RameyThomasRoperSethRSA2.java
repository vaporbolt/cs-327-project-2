/**
 * @author Xunhua Wang. All rights reserved. Modified by Thomas Ramey and Seth Roper
 * @date 02/16/2012; revised on 09/27/2018; further refined on 09/20/2019 and 09/29/2020
 * 
 */

public class RameyThomasRoperSethRSA
{
	public int gcd (int inE, int inZ) {
		while (inZ != 0)
		{
			int temp = inZ;
			inZ = inE % inZ;
			inE = temp;
		}
		return inE;
	}

	public void testGcd () {
		int result1 = gcd (29, 288);
		int result2 = gcd (30, 288);
		System.out.println ("GCD (29, 288) = 0x" + Integer.toString(result1, 16));
		System.out.println ("GCD (30, 288) = 0x" + Integer.toString(result2, 16));
	}

	//
	// We assume that inE < inZ
	// This implementation follows our slides
	// Return:
	//	-1: no inverse
	//	inverse of inE mod inZ
	//
	public int xgcd (int inE, int inZ) {
		// TO BE FINISHED
		// Must implement the extended Euclidean algorithm
		// NO brute-forcing; violation will lead to zero points
		// NO recursion; violation will lead to zero points

		// covered just in case even tho INZ SHOULD NEVER BE 1
		if (inZ == 1) {
			return 0;
		}

		int reserve = inZ;
		int t = 0, s = 1;

		// default d is inE
		while (inE > 1) {

			int q = inE / inZ;
			int d = inZ;
			inZ = inE % inZ;
			inE = d;
			d = t;
			t = s - q * t;
			s = d;
		}

		// negative mod
		if (s < 0)
			s+= reserve;

		return s;
	}

	public void testXgcd () {
		int result1 = xgcd (29, 288);
		int result2 = xgcd (149, 288);

		System.out.println ("29^-1 mod 288 = 0x" + Integer.toString(result1, 16));
		System.out.println ("149^-1 mod 288 = 0x" + Integer.toString(result2, 16));
	}

	public int[] keygen (int inP, int inQ, int inE) {
		int[] keys = new int[3];
		keys[0] = inE;
		keys[1] = inP * inQ;
		int z = (inP - 1) * (inQ - 1);
		keys[2]  = xgcd(inE, z);
		return keys;
		
	}

	//
	// The following method will return an integer array, with [e, N, d] in this order
	//
	public void testKeygen () {
		int[] keypair = keygen (17, 19, 29);

		System.out.println ("e = 0x" + Integer.toString(keypair[0], 16));
		System.out.println ("N = 0x" + Integer.toString(keypair[1], 16));
		System.out.println ("d = 0x" + Integer.toString(keypair[2], 16));
	}

	//
	// Calculate c = a^b mod n, with the square-and-multiply algorithm
	//
	// The following method implements the square-and-multiply algorithm, with Java primitive types
	//
	// Note that even with primitive types, a^b may well exceed the range of Java int
	// For example, 5^20 is too big to be held by a Java primitive integer
	//
	public int modExp (int a, int b, int n) {
		int c = 1;
		while (b > 0) 
		{
			int d = b % 2;
			b = b / 2;
			if (d == 1) 
			{
				long val = c * a;
				c = (int) (val % n);
			}

			long val2 = a * a;
			a = (int) (val2 % n);
		}
		return c;
	}

	public int encrypt (int message, int inE, int inN) {
		return modExp(message, inE, inN);
	}
	
	public int decrypt (int ciphertext, int inD, int inN) {
		      return modExp(ciphertext, inD, inN);
	}

	public void testRSA () {
		int[] keypair = keygen (17, 19, 29);

		int m1 = 4;
		int c1 = encrypt (m1, keypair[0], keypair[1]);
		System.out.println ("The encryption of (m1=0x" + Integer.toString(m1, 16) + ") is 0x" + Integer.toString(c1, 16));
		int cleartext1 = decrypt (c1, keypair[2], keypair[1]);
		System.out.println ("The decryption of (c=0x" + Integer.toString(c1, 16) + ") is 0x" + Integer.toString(cleartext1, 16));

		int m2 = 5;
		int c2 = encrypt (m2, keypair[0], keypair[1]);
		System.out.println ("The encryption of (m2=0x" + Integer.toString(m2, 16) + ") is 0x" + Integer.toString(c2, 16));
		int cleartext2 = decrypt (c2, keypair[2], keypair[1]);
		System.out.println ("The decryption of (c2=0x" + Integer.toString(c2, 16) + ") is 0x" + Integer.toString(cleartext2, 16));
	}

	public static void main (String[] args) {
		RameyThomasRoperSethRSA atrsa = new RameyThomasRoperSethRSA ();

		System.out.println ("********** Project 1 output begins ********** ");

		atrsa.testGcd ();
		atrsa.testXgcd ();
		atrsa.testKeygen ();
		atrsa.testRSA ();
	}
}
