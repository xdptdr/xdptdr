package xpdtr.acme.gui.interactions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.commons.io.IOUtils;

import xdptdr.acme.v2.AcmeSession;
import xdptdr.asn.OIDS;
import xdptdr.asn.builders.X509Builder;
import xdptdr.common.Common;
import xpdtr.acme.gui.components.UILogger;
import xpdtr.acme.gui.utils.U;

public class FooInteraction extends UIInteraction {

	private UILogger logger;

	private AcmeSession session;

	private Runnable after;

	public FooInteraction(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		super(interacter, container);
		this.logger = logger;
		this.session = session;
		this.after = after;
	}

	@Override
	protected void perform() {
		logger.beginGroup("Foo");
		logger.message("Doing something...");
		doSomething();
	}

	private void doSomething() {

		final String site = getSite();
		final String token = getToken();

		SwingWorker<byte[], Void> worker = new SwingWorker<byte[], Void>() {
			@Override
			protected byte[] doInBackground() throws Exception {

				// https://tools.ietf.org/html/draft-ietf-acme-tls-alpn-01

				KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
				KeyPair pair = kpg.generateKeyPair();
				PublicKey publicKey = pair.getPublic();
				PrivateKey privateKey = pair.getPrivate();
				int serialNumber = 0;
				Date notBefore = new Date(Long.MIN_VALUE);
				Date notAfter = new Date(Long.MAX_VALUE);
				byte[] keyIdentifier = getIdentifier(publicKey);
				byte[] caSubjectKeyIdentifier = keyIdentifier;
				byte[] clientSubjectKeyIdentifier = keyIdentifier;
				String issuerName = "CN=ME";
				String subjectName = "CN=ME";

				byte[] digest = sign(token);

				X509Builder b = new X509Builder();
				b.setEncodedPublicKey(publicKey.getEncoded());
				b.setSerial(serialNumber);
				b.setNotBefore(notBefore);
				b.setNotAfter(notAfter);
				b.setAuthorityKeyIdentifier(caSubjectKeyIdentifier);
				b.setSubjectKeyIdentifier(clientSubjectKeyIdentifier);
				b.setExtKeyUsage(OIDS.CLIENT_AUTH);
				b.setIssuerName(issuerName);
				b.setSubjectName(subjectName);
				b.setAcmeExtension(digest);
				b.addSubjectAlternativeNameExtension("DNS", site);
				return b.encode(privateKey);

			}

			private byte[] getIdentifier(PublicKey clientPublicKey) throws Exception {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				return md.digest(clientPublicKey.getEncoded());
			}

			private byte[] sign(String string) throws Exception {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				return md.digest(string.getBytes());
			}

			protected void done() {
				try {
					byte[] bytes = get();
					interactionDone(bytes, null);
				} catch (InterruptedException | ExecutionException e) {
					interactionDone(null, e);
				}
			};

		};
		worker.execute();

	}

	private String getSite() {
		if (session.getAuthorization() != null) {
			return session.getAuthorization().getIdentifier().getValue();
		}
		return "example.com";
	}

	private String getToken() {

		if (session.getChallenge() != null) {
			return session.getChallenge().getToken();
		}

		return "token";
	}

	private void interactionDone(byte[] bytes, Exception e) {
		interacter.perform(() -> {

			if (e != null) {
				logger.exception(e);
			} else {
				logger.message(Common.bytesToString(bytes));
				try {
					File f = new File("foo.output");
					FileOutputStream fos = new FileOutputStream(f);
					IOUtils.copy(new ByteArrayInputStream(bytes), fos);
					logger.message("Saved to " + f.getAbsolutePath());
					logger.getDestination().add(U.button("Show in explorer", () -> {
						try {
							FileSystem d = FileSystems.getDefault();
							Path p = d.getPath("C:", "Windows", "explorer.exe");
							Path path = p.toAbsolutePath();
							String pf = new File(f.getAbsolutePath()).getParentFile().getAbsolutePath();
							String ps = path.toString();
							ProcessBuilder pb = new ProcessBuilder(ps, pf);
							pb.start();
						} catch (Exception e1) {
							logger.exception(e1);
						}
					}));
				} catch (Exception e1) {
					logger.exception(e);
				}
			}

			logger.endGroup();
			after.run();
		});
	}

	public static void perform(Interacter interacter, JPanel container, UILogger logger, AcmeSession session,
			Runnable after) {
		new FooInteraction(interacter, container, logger, session, after).start();

	}

}
