package xpdtr.acme.gui.components;

import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import xdptdr.acme.v2.AcmeAuthorization;
import xdptdr.acme.v2.AcmeChallenge;
import xdptdr.acme.v2.AcmeDirectoryInfos2;
import xdptdr.acme.v2.AcmeIdentifier;
import xdptdr.acme.v2.AcmeOrder;
import xdptdr.acme.v2.AcmeSession;
import xpdtr.acme.gui.utils.U;

public class SessionUI {

	public static void render(AcmeSession session, JComponent stateContainer) {
		if (session.getVersion() != null) {
			U.addM(stateContainer, MessageUI.render("ACME Version : " + session.getVersion()));
		}
		if (session.getUrl() != null) {
			U.addM(stateContainer, MessageUI.render("Server URL : " + session.getUrl()));
		}
		if (session.getInfos() != null) {
			AcmeDirectoryInfos2 i = session.getInfos();
			JPanel directoryPanel = titledVPanel("Directory");
			U.addM(directoryPanel, MessageUI.render("New Nonce URL : " + i.getNewNonce()));
			U.addM(directoryPanel, MessageUI.render("New Account URL : " + i.getNewAccountURL()));
			U.addM(directoryPanel, MessageUI.render("New Order URL : " + i.getNewOrder()));
			U.addM(directoryPanel, MessageUI.render("Key Change URL : " + i.getKeyChange()));
			U.addM(directoryPanel, MessageUI.render("Revoke Cert URL : " + i.getRevokeCert()));
			U.addM(stateContainer, directoryPanel);
		}
		if (session.getNonce() != null) {
			JPanel noncePanel = titledVPanel("Nonce");
			U.addM(noncePanel, MessageUI.render(session.getNonce()));
			U.addM(stateContainer, noncePanel);

		}
		if (session.getAccount() != null) {

			JPanel accountPanel = titledVPanel("Account");
			U.addM(accountPanel, MessageUI.render("URL : " + session.getAccount().getUrl()));
			U.addM(stateContainer, accountPanel);
		}
		AcmeOrder order = session.getOrder();
		if (order != null) {
			JPanel orderPanel = titledVPanel("Order");
			for (AcmeIdentifier identifier : order.getIdentifiers()) {
				U.addM(orderPanel,
						MessageUI.render("Identifier : " + identifier.getType() + " " + identifier.getValue()));
			}
			U.addM(orderPanel, MessageUI.render("Status : " + order.getStatus()));
			U.addM(orderPanel, MessageUI.render("Expires : " + order.getStatus()));
			for (String authorizationURL : order.getAuthorizations()) {
				U.addM(orderPanel, MessageUI.render("Authorization URL : " + authorizationURL));
			}
			U.addM(orderPanel, MessageUI.render("Finalize URL : " + order.getFinalize()));
			U.addM(stateContainer, orderPanel);
		}
		if (session.getAuthorization() != null) {
			JPanel authorizationPanel = titledVPanel("Authorization");
			AcmeAuthorization a = session.getAuthorization();
			List<AcmeChallenge> challenges = a.getChallenges();

			AcmeIdentifier identifier = a.getIdentifier();
			U.addM(authorizationPanel,
					MessageUI.render("Authorization : " + identifier.getType() + " " + identifier.getValue()));
			U.addM(authorizationPanel, MessageUI.render("Status: " + a.getStatus()));
			U.addM(authorizationPanel, MessageUI.render("Expires: " + a.getExpires()));
			for (AcmeChallenge challenge : challenges) {
				JPanel challengePanel = titledVPanel("Challenge");
				U.addM(challengePanel, MessageUI.render("URL : " + challenge.getUrl()));
				U.addM(challengePanel, MessageUI.render("Type : " + challenge.getType()));
				U.addM(challengePanel, MessageUI.render("Status : " + challenge.getStatus()));
				U.addM(challengePanel, MessageUI.render("Token : " + challenge.getToken()));
				U.addM(authorizationPanel, challengePanel);
			}
			U.addM(stateContainer, authorizationPanel);
		}
	}

	private static JPanel titledVPanel(String title) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		TitledBorder b = BorderFactory.createTitledBorder(title);
		b.setTitleFont(b.getTitleFont().deriveFont(Font.BOLD));
		p.setBorder(b);
		return p;
	}

}
