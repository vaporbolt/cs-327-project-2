import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

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
		while (inE.compareTo(BigInteger.ONE) == 1) { //inE > 1?

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
		BigInteger p = new BigInteger("639709281763605975586752107");
		BigInteger q = new BigInteger("1027348307662006800312732683");
		BigInteger n = q.multiply(p);
		BigInteger d = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		BigInteger[] keypair = keygen (p, q, new BigInteger("17"));
		System.out.println ("p = 0x" +p.toString(16));
		System.out.println ("q = 0x" +q.toString(16));
		System.out.println ("e = 0x" + keypair[0].toString(16));
		System.out.println ("N = 0x" + keypair[1].toString(16));
		System.out.println ("N has " + calculateBits(keypair[1]) + " bits");
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
	public BigInteger modExpTwo (BigInteger a, BigInteger b, BigInteger n) {
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
		//int res = resBigInt.intValue ();
		return resBigInt;
}



	public BigInteger encrypt (BigInteger message, BigInteger inE, BigInteger inN) {
		return modExpTwo(message, inE, inN); //Not too sure on this
	}
	

	//Appears to be not working
	public BigInteger decrypt (BigInteger ciphertext, BigInteger inD, BigInteger inN) {
		      return modExpTwo(ciphertext, inD, inN); //Not too sure on this
	}

	public void testRSA () {
		BigInteger p = new BigInteger("1821618209305989613382418596494169687036420364965421129134963360400595769749807455142959409976126078995241914312291577495610519423772929527936396420605114874419154232836561671414350490460228113441075618411363050789426976000007599763364547629060889440700125327266998298962685896768388148785353373454263657387997238076986729475125791062877101582571654678950990289476131477912683746651579092962395182436124323465567001310002387782529415237149777816727121008438674607");
		BigInteger q = new BigInteger("1494404911228773568193595402920077013873360069861757539492815312919545935612505886795166517041806871759211066574482771633837336160456475335794352168201909436016357498289576035796443968197486506507562345028145200146534022861262664272791455051489900178563372605412245630620825231595435981956459729485957099861955463839266901249472054558057680898955786240266154203652122975190083730014332915313854176372296535807082155414293933903181782339003802051762625955299586519");
		
		BigInteger[] keypair = keygen (p, q, BigInteger.valueOf(65537));

		BigInteger m1 = BigInteger.valueOf(3);
		BigInteger c1 = encrypt (m1, keypair[0], keypair[1]);
		System.out.println ("The encryption of (m1=0x" + m1.toString(16) + ") is 0x" + c1.toString(16));
		BigInteger cleartext1 = decrypt (c1, keypair[2], keypair[1]);
		System.out.println ("The decryption of (c=0x" + c1.toString(16) + ") is 0x" + cleartext1.toString(16));
		calculateDecryption(c1, keypair[2], keypair[1]);

		BigInteger m2  = BigInteger.valueOf(5);
		BigInteger c2 = encrypt (m2, keypair[0], keypair[1]);
		System.out.println ("The encryption of (m2=0x" + m2.toString(16) + ") is 0x" + c2.toString(16));
		BigInteger cleartext2 = decrypt (c2, keypair[2], keypair[1]);
		System.out.println ("The decryption of (c2=0x" + c2.toString(16) + ") is 0x" + cleartext2.toString(16));
	}

	public double calculateDecryption(BigInteger cipherText, BigInteger inD, BigInteger inN)
	{
		long startTime = System.nanoTime();
		for(int i = 0; i < 1000; i++)
		{
			decrypt(cipherText, inD, inN);
		}
		long endTime = System.nanoTime();
		double calcTime = (endTime - startTime) / 1000000000.0;
		double result = ((double)(1000.0 * calculateBits(inN)) / calcTime) / 1000.0;
		System.out.print("KiloBits decrypted per second: ");
		System.out.printf("%.4f\n", result);
		return result;
	}

	public int calculateBits(BigInteger n)
	{
		return n.bitLength();
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
