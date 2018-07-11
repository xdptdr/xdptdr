package xpdtr.acme.gui.interactions;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;

import javax.swing.JPanel;

import xdptdr.acme.v2.Acme2;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xdptdr.asn.builders.CSRBuilder;
import xdptdr.common.Common;
import xpdtr.acme.gui.components.UILogger;

public class FinalizeInteraction extends UIInteraction {

	private UILogger logger;
	private AcmeSession session;
	private Runnable after;

	public FinalizeInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.after = after;

	}

	@Override
	protected void perform() {
		logger.beginGroup("Finalize");
		logger.message("Finalizing");

		try {

			if (session.getKeyPairWithJWK() == null) {
				throw new Exception("Please load or create a key pair");
			}

			if (session.getAuthorization() == null) {
				throw new Exception("Please get an authorization");
			}

			byte[] csrBytes = createCsr(getKeyPair(session), session.getAuthorization().getIdentifier().getValue());

			logger.message("CSR bytes are as follow : ");
			logger.message(Common.bytesToString(csrBytes));

			AcmeResponse<Void> response = Acme2.finalize(session, csrBytes);

			if (response.isFailed()) {
				logger.message(response.getFailureDetails());
			} else {
				logger.message(response.getResponseText());
			}

		} catch (Exception ex) {
			logger.exception(ex);
		}

		after.run();
	}

	private KeyPair getKeyPair(AcmeSession session2) throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048, new SecureRandom());
		KeyPair keyPair = kpg.generateKeyPair();
		logger.message("Public key pair bytes : " + Common.bytesToString(keyPair.getPublic().getEncoded()));
		logger.message("Private key pair bytes : " + Common.bytesToString(keyPair.getPrivate().getEncoded()));
		return keyPair;
	}

	private byte[] createCsr(KeyPair keyPair, String site)
			throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		CSRBuilder builder = new CSRBuilder();
		builder.setPublickKey(keyPair.getPublic());
		builder.setSubjectName("CN=" + site);
		builder.setSubjectKeyIdentifier(null);
		builder.setVersion(2);
		return builder.encode(keyPair.getPrivate());
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		new FinalizeInteraction(interacter, container, logger, session, after).start();
	}

}
