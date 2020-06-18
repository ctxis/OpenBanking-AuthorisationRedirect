package contextis;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Gui {

    private Utils utils;

    //buttons
    private JButton btnSaveConfig;
    private JButton btnLoadConfig;
    private JButton btnGenerate;
    private JButton btnOpen;
    private JButton btnLoadPriv;
    private JButton btnLoadPub;
    private JButton btnCopy;

    private JTextField textKid;
    private JTextField textAlg;
    private JTextField textClientId;
    private JTextField textRedirectUrl;
    private JTextField textScope;
    private JTextField textResponseType;
    private JTextField textAudience;
    private JTextField textConsentId;
    private JCheckBox checkBoxUseConsentId;
    private JTextField textState;
    private JTextField textNonce;
    private JTextField textPublicKey;
    private JTextField textPrivateKey;

    private JTextArea textOutput;

    private JLabel lblIsValid;
    private JTextArea textDecodedJwt;
    private JTextArea textSignature;

    private JTabbedPane tabs;

    public Gui(Utils utils) {
        this.utils = utils;
        initGui();
    }

    private void initGui() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Open Banking Authorisation Redirect");
        dialog.setSize(970,650);
        dialog.add(createTabs());
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public JTabbedPane createTabs() {
        tabs = new JTabbedPane();
        tabs.add("Settings", createSettingsPanel());
        tabs.add("Redirect", createRedirectPanel());
        tabs.add("Verify", createVerifyPanel());
        return tabs;
    }


    /**
     * Panel for verify tab
     *
     * @return JPanel
     */
    public JPanel createVerifyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblValid = new JLabel("Valid signature: ");
        lblValid.setSize(170, 20);
        lblValid.setLocation(20, 20);
        panel.add(lblValid);

        this.lblIsValid = new JLabel();
        this.lblIsValid.setSize(450, 20);
        this.lblIsValid.setLocation(190, 20);
        panel.add(this.lblIsValid);

        this.textDecodedJwt = new JTextArea();
        this.textDecodedJwt.setSize(900, 300);
        this.textDecodedJwt.setLocation(20, 60);
        JScrollPane jwtScrollPane = new JScrollPane(this.textDecodedJwt);
        jwtScrollPane.setBounds(20, 60, 900, 300);
        panel.add(jwtScrollPane);

        this.textSignature = new JTextArea();
        this.textSignature.setSize(900, 150);
        this.textSignature.setLocation(20, 400);
        this.textSignature.setLineWrap(true);
        panel.add(this.textSignature);

        return panel;
    }


    /**
     * Panel for setting tab
     *
     * @return JPanel
     */
    public JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        int textLength = 350;
        int labelRow1 = 20;
        int textRow1 = 110;
        int labelRow2 = 490;
        int textRow2 = 580;

        this.btnSaveConfig = new JButton("Save");
        this.btnSaveConfig.setSize(100, 20);
        this.btnSaveConfig.setLocation(20, 20);
        panel.add(btnSaveConfig);

        this.btnLoadConfig = new JButton("Load");
        this.btnLoadConfig.setSize(100, 20);
        this.btnLoadConfig.setLocation(130, 20);
        panel.add(btnLoadConfig);

        //row
        panel.add(this.utils.createLabel("ALG", labelRow1, 70, "Open Banking Specification: PS256 or RS256"));
        this.textAlg = this.utils.createTextField(textLength,  textRow1, 70);
        this.textAlg.setText("PS256");
        panel.add(this.textAlg);

        panel.add(this.utils.createLabel("KID", labelRow2, 70, "Certificate Signing Key"));
        this.textKid = this.utils.createTextField(textLength,  textRow2, 70);
        panel.add(this.textKid);

        //row
        panel.add(this.utils.createLabel("Client ID", labelRow1, 100, ""));
        this.textClientId = this.utils.createTextField(textLength,  textRow1, 100);
        panel.add(this.textClientId);

        panel.add(this.utils.createLabel("TPP URL", labelRow2, 100, "TPP Redirect URL"));
        this.textRedirectUrl = this.utils.createTextField(textLength,  textRow2, 100);
        panel.add(this.textRedirectUrl);

        //row
        panel.add(this.utils.createLabel("Scope", labelRow1, 130, ""));
        this.textScope = this.utils.createTextField(textLength,  textRow1, 130);
        this.textScope.setText("openid accounts payments fundsconfirmations");
        panel.add(this.textScope);

        panel.add(this.utils.createLabel("Resp. Type", labelRow2, 130, ""));
        this.textResponseType = this.utils.createTextField(textLength,  textRow2, 130);
        this.textResponseType.setText("code id_token");
        panel.add(this.textResponseType);

        //row
        panel.add(this.utils.createLabel("Audience", labelRow1, 160, ""));
        this.textAudience = this.utils.createTextField(textLength,  textRow1, 160);
        panel.add(this.textAudience);

        //row
        panel.add(this.utils.createLabel("Consent ID", labelRow1, 200, ""));
        this.textConsentId = this.utils.createTextField(textLength,  textRow1, 200);
        panel.add(this.textConsentId);

        JLabel useConsentIdLabel = new JLabel("Use ConsentId for Nonce & State: ");
        useConsentIdLabel.setSize(250, 30);
        useConsentIdLabel.setLocation(labelRow2, 200);
        panel.add(useConsentIdLabel);

        this.checkBoxUseConsentId = new JCheckBox();
        this.checkBoxUseConsentId.setLocation(750, 200);
        this.checkBoxUseConsentId.setSelected(true);
        this.checkBoxUseConsentId.setSize(30, 30);
        panel.add(this.checkBoxUseConsentId);

        //row
        panel.add(this.utils.createLabel("State", labelRow1, 230, ""));
        this.textState = this.utils.createTextField(textLength,  textRow1, 230);
        panel.add(this.textState);

        panel.add(this.utils.createLabel("Nonce", labelRow2, 230, ""));
        this.textNonce = this.utils.createTextField(textLength,  textRow2, 230);
        panel.add(this.textNonce);

        //row
        panel.add(this.utils.createLabel("Private Key", labelRow1, 270, ""));
        this.textPrivateKey = this.utils.createTextField(textLength,  textRow1, 270);
        panel.add(this.textPrivateKey);

        this.btnLoadPriv = new JButton("Choose File");
        this.btnLoadPriv.setSize(150, 20);
        this.btnLoadPriv.setLocation(labelRow2, 270);
        panel.add(btnLoadPriv);

        //row
        panel.add(this.utils.createLabel("Public Key", labelRow1, 300, ""));
        this.textPublicKey = this.utils.createTextField(textLength,  textRow1, 300);
        panel.add(this.textPublicKey);

        this.btnLoadPub = new JButton("Choose File");
        this.btnLoadPub.setSize(150, 20);
        this.btnLoadPub.setLocation(labelRow2, 300);
        panel.add(btnLoadPub);

        return panel;
    }

    /**
     * Panel for redirect tab
     *
     * @return JPanel
     */
    public JPanel createRedirectPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        this.btnGenerate = new JButton("Generate");
        this.btnGenerate.setSize(150, 20);
        this.btnGenerate.setLocation(20, 20);
        panel.add(btnGenerate);

        this.btnCopy = new JButton("Copy");
        this.btnCopy.setSize(100, 20);
        this.btnCopy.setLocation(390, 20);
        panel.add(btnCopy);

        this.textOutput = new JTextArea();
        this.textOutput.setLocation(20, 50);
        this.textOutput.setSize(900, 500);
        this.textOutput.setLineWrap(true);
        panel.add(this.textOutput);

        return panel;
    }

    public JButton getBtnSaveConfig() {
        return btnSaveConfig;
    }

    public JButton getBtnLoadConfig() {
        return btnLoadConfig;
    }

    public JButton getBtnGenerate() {
        return btnGenerate;
    }

    public JButton getBtnOpen() {
        return btnOpen;
    }

    public JButton getBtnLoadPriv() {
        return btnLoadPriv;
    }

    public JButton getBtnLoadPub() {
        return btnLoadPub;
    }

    public JButton getBtnCopy() {
        return btnCopy;
    }

    public JTextField getTextKid() {
        return textKid;
    }

    public JTextField getTextAlg() {
        return textAlg;
    }

    public JTextField getTextClientId() {
        return textClientId;
    }

    public JTextField getTextRedirectUrl() {
        return textRedirectUrl;
    }

    public JTextField getTextScope() {
        return textScope;
    }

    public JTextField getTextResponseType() {
        return textResponseType;
    }

    public JTextField getTextAudience() {
        return textAudience;
    }

    public JTextField getTextConsentId() {
        return textConsentId;
    }

    public JCheckBox getCheckBoxUseConsentId() {
        return checkBoxUseConsentId;
    }

    public JTextField getTextState() {
        return textState;
    }

    public JTextField getTextNonce() {
        return textNonce;
    }

    public JTextField getTextPublicKey() {
        return textPublicKey;
    }

    public JTextField getTextPrivateKey() {
        return textPrivateKey;
    }

    public JTextArea getTextOutput() {
        return textOutput;
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    public JLabel getLblIsValid() {
        return lblIsValid;
    }

    public JTextArea getTextDecodedJwt() {
        return textDecodedJwt;
    }

    public JTextArea getTextSignature() {
        return textSignature;
    }
}
