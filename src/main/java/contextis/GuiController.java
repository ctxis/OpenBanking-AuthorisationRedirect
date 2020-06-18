package contextis;

import com.google.gson.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GuiController {

    private Utils utils;
    private File currentSaveFile;
    private Gui gui;
    private OpenBankingAuthorisationRedirect obRedirect;

    public void initialiseRedirect() {
        this.obRedirect = new OpenBankingAuthorisationRedirect();
        this.utils = new Utils();
        this.gui = new Gui(this.utils);

        initButtons();
    }

    /**
     * Initialise button events
     */
    public void initButtons() {
        this.gui.getBtnSaveConfig().addActionListener(this::saveConfig);
        this.gui.getBtnLoadConfig().addActionListener(this::loadConfig);
        this.gui.getBtnLoadPriv().addActionListener(this::loadCert);
        this.gui.getBtnLoadPub().addActionListener(this::loadCert);
        this.gui.getBtnCopy().addActionListener(this::copyOutput);
        this.gui.getBtnGenerate().addActionListener(this::generateRedirect);
    }

    /**
     * This function grabs the data from the "Settings" tab input fields and creates a JSON object.
     *
     * @return settings data as JSON object
     */
    public String getConfigData() {
        JsonObject data = new JsonObject();
        data.addProperty("alg", this.gui.getTextAlg().getText());
        data.addProperty("kid", this.gui.getTextKid().getText());
        data.addProperty("private-key", this.gui.getTextPrivateKey().getText());
        data.addProperty("public-key", this.gui.getTextPublicKey().getText());
        data.addProperty("client-id", this.gui.getTextClientId().getText());
        data.addProperty("redirect-url", this.gui.getTextRedirectUrl().getText());
        data.addProperty("scope", this.gui.getTextScope().getText());
        data.addProperty("response-type", this.gui.getTextResponseType().getText());
        data.addProperty("audience", this.gui.getTextAudience().getText());
        data.addProperty("consent-id", this.gui.getTextConsentId().getText());
        data.addProperty("chbox-use-consent-id", this.gui.getCheckBoxUseConsentId().isSelected());
        data.addProperty("state", this.gui.getTextState().getText());
        data.addProperty("nonce", this.gui.getTextNonce().getText());

        JsonArray header = new JsonArray();
        data.add("redirect-header", header);

        return data.toString();
    }

    /**
     * File browser to select a location to save the config file.
     *
     * @param e "Settings" tab - "Save" button
     */
    public void saveConfig(ActionEvent e){
        File f = utils.getFileFromDialog(true, (currentSaveFile != null ? currentSaveFile.getPath() : "open-banking-redirect-config.json"), this.gui.getTabs());

        if(f != null) {
            currentSaveFile = f;

            try {
                FileWriter fw = new FileWriter(f);
                fw.write(getConfigData());
                fw.flush();
                fw.close();

            } catch(IOException exc) {
                System.out.println(exc.getMessage());
            }
        }
    }

    /**
     * This will take the data provided by the loadConfig function and set the loaded values in the settings tab.
     *
     * @param configData: data from the json config file
     */
    private void setConfigData(JsonObject configData) {
        this.gui.getTextAlg().setText(configData.get("alg").getAsString());
        this.gui.getTextKid().setText(configData.get("kid").getAsString());
        this.gui.getTextPrivateKey().setText(configData.get("private-key").getAsString());
        this.gui.getTextPublicKey().setText(configData.get("public-key").getAsString());
        this.gui.getTextClientId().setText(configData.get("client-id").getAsString());
        this.gui.getTextRedirectUrl().setText(configData.get("redirect-url").getAsString());
        this.gui.getTextScope().setText(configData.get("scope").getAsString());
        this.gui.getTextResponseType().setText(configData.get("response-type").getAsString());
        this.gui.getTextAudience().setText(configData.get("audience").getAsString());
        this.gui.getTextConsentId().setText(configData.get("consent-id").getAsString());
        this.gui.getCheckBoxUseConsentId().setSelected(Boolean.parseBoolean(configData.get("chbox-use-consent-id").getAsString()));
        this.gui.getTextState().setText(configData.get("state").getAsString());
        this.gui.getTextNonce().setText(configData.get("nonce").getAsString());
    }

    /**
     * File browser for finding and loading the json config for this tool.
     *
     * @param e "Settings" tab - "Load" button
     */
    public void loadConfig(ActionEvent e){
        File file;
        try {
            if((file = utils.getFileFromDialog(false, (currentSaveFile != null ? currentSaveFile.getPath() : ""), this.gui.getTabs())) != null){
                currentSaveFile = file;

                if(file.exists() && file.isFile() && file.canRead()){
                    byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
                    String config = new String(encoded, StandardCharsets.UTF_8);

                    JsonObject configObject = new JsonParser().parse(config).getAsJsonObject();
                    setConfigData(configObject);
                }
            }
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }
    }

    /**
     * This is the file browser for selecting the private and public keys. It will fill in the absolute path to
     * the Open Banking signing keys. Both public and private key need to be in DER format.
     *
     * @param e "Settings" tab - "Choose File" buttons
     */
    public void loadCert(ActionEvent e){
        File file;
        if((file = utils.getFileFromDialog(false, (""), this.gui.getTabs())) != null){
            if(file.exists() && file.isFile() && file.canRead()){
                String absolutePath = file.getAbsolutePath();

                if(e.getSource() == this.gui.getBtnLoadPriv()) {
                    this.gui.getTextPrivateKey().setText(absolutePath);
                } else if(e.getSource() == this.gui.getBtnLoadPub()) {
                    this.gui.getTextPublicKey().setText(absolutePath);
                }
            }
        }
    }

    /**
     * Upon click on the "Copy" button in the "Signing" tab, the detached JWS is saved into the clipboard.
     *
     * @param e "Copy" button
     */
    public void copyOutput(ActionEvent e) {
        StringSelection stringSelection = new StringSelection(this.gui.getTextOutput().getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    /**
     * Generates redirect GET Request parameters
     *
     * @param e "Redirect" tab - "Generate" button
     */
    public void generateRedirect(ActionEvent e) {
        String alg = this.gui.getTextAlg().getText();
        String audience = this.gui.getTextAudience().getText();
        String scope = this.gui.getTextScope().getText();
        String clientId = this.gui.getTextClientId().getText();
        String consentId = this.gui.getTextConsentId().getText();
        String responseType = this.gui.getTextResponseType().getText();
        String redirectUrl = this.gui.getTextRedirectUrl().getText();
        String state = this.gui.getTextState().getText();
        String nonce = this.gui.getTextNonce().getText();
        String kid = this.gui.getTextKid().getText();
        String privateKeyFilePath = this.gui.getTextPrivateKey().getText();
        String publicKeyFilePath = this.gui.getTextPublicKey().getText();
        boolean useConsentId = this.gui.getCheckBoxUseConsentId().isSelected();

        if(useConsentId) {
            state = consentId;
            nonce = consentId;
        }
        String paramRedirect = redirectUrl.replace(":", "%3A").replace("/", "%2F");

        String payload = this.obRedirect.generatePayload(audience, scope, clientId, consentId, responseType, redirectUrl, state, nonce);
        String jwt = this.obRedirect.generateSignedJwt(payload, this.utils.getPrivateKey(privateKeyFilePath), kid, alg);
        String params = this.obRedirect.generateUrlParams(clientId, paramRedirect, responseType, state, scope, jwt);
        this.gui.getTextOutput().setText(params);

        if(!publicKeyFilePath.isEmpty()) {
            this.gui.getLblIsValid().setText(Boolean.toString(this.obRedirect.verifyJwt(jwt, alg, this.utils.getPublicKey(publicKeyFilePath), payload)));
        } else {
            this.gui.getLblIsValid().setText("Please provide a public key to verify the signature.");
        }

        //verify tab
        java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
        String[] parts = jwt.split("\\."); // split out the "parts" (header, payload and signature)

        String headerJson = new String(decoder.decode(parts[0]));
        String payloadJson = new String(decoder.decode(parts[1]));
        String signatureJson = parts[2];

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        String prettyHeaderString = gson.toJson(jp.parse(headerJson));
        String prettyPayloadString = gson.toJson(jp.parse(payloadJson));

        this.gui.getTextSignature().setText(signatureJson);
        this.gui.getTextDecodedJwt().setText(prettyHeaderString);
        this.gui.getTextDecodedJwt().append(prettyPayloadString);
    }
}
