package xpdtr.acme.gui.interactions;

import java.awt.Container;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import xdptdr.acme.jw.KeyPairWithJWK;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.components.ExceptionUI;
import xpdtr.acme.gui.components.MessageUI;
import xpdtr.acme.gui.utils.U;

public class KeyPairInteractions {

	private AcmeSession session;
	private Container sessionContainer;
	private JFrame frame;
	private Runnable after;

	public KeyPairInteractions(AcmeSession session, Container sessionContainer, JFrame frame, Runnable after) {
		this.session = session;
		this.sessionContainer = sessionContainer;
		this.frame = frame;
		this.after = after;
	}

	public void saveKeyPair() {

		JFileChooser jfc = new JFileChooser();
		int r = jfc.showOpenDialog(frame);
		U.addM(sessionContainer, MessageUI.render("Returned value : " + r));
		if (r == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			U.addM(sessionContainer, MessageUI.render(file.getAbsolutePath()));
			try {
				FileWriter fw = new FileWriter(file);
				Map<String, String> jwk = session.getKeyPairWithJWK().getFullJWK();
				session.getOm().writeValue(fw, jwk);
				fw.close();
				U.addM(sessionContainer, MessageUI.render("Key pair saved"));
			} catch (IOException e) {
				U.addM(sessionContainer, ExceptionUI.render(e));
			}
		} else {
			U.addM(sessionContainer, MessageUI.render("Cancelled"));
		}
		after.run();

	}

	public void loadKeyPair() {
		JFileChooser jfc = new JFileChooser();
		int r = jfc.showOpenDialog(frame);
		U.addM(sessionContainer, MessageUI.render("Returned value : " + r));
		if (r == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			U.addM(sessionContainer, MessageUI.render(file.getAbsolutePath()));
			if (!file.exists() || !file.isFile() || !file.canRead()) {
				U.addM(sessionContainer, MessageUI.render("File is not a readable existing file"));
			} else {
				try {
					ObjectMapper om = session.getOm();
					Map<String, String> jwk = om.readValue(new FileReader(file),
							new TypeReference<Map<String, String>>() {
							});
					session.setKeyPairWithJWK(KeyPairWithJWK.fromJWK(jwk));
					U.addM(sessionContainer, MessageUI.render("Key loaded"));
				} catch (Exception e) {
					U.addM(sessionContainer, ExceptionUI.render(e));
				}
			}
		} else {
			U.addM(sessionContainer, MessageUI.render("Cancelled"));
		}
		after.run();
	}

}
