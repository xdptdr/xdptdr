package xdptdr.acme.crypto;

import java.math.BigInteger;

public class ECSignature {

	private BigInteger r;
	private BigInteger s;

	public ECSignature(BigInteger r, BigInteger s) {
		this.r = r;
		this.s = s;
	}

	public BigInteger getR() {
		return r;
	}

	public void setR(BigInteger r) {
		this.r = r;
	}

	public BigInteger getS() {
		return s;
	}

	public void setS(BigInteger s) {
		this.s = s;
	}


}
