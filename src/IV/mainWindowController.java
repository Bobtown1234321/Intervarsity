package IV;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;


public class mainWindowController implements Initializable {
    @FXML
    private WebView webView;

    @FXML
    private TextField webInput;

    //Variables
    private WebEngine webEngine;


    @FXML
    private void enterWebaddress(){

        webEngine.load(webInput.getText());
        System.out.println(webEngine.getHistory().getEntries().toString());

    }

    @FXML
    private void initialize(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();

        webEngine.setJavaScriptEnabled(true);
        webEngine.load("https://www.google.com");

        System.out.println(webEngine.getUserAgent());
        System.out.println(webEngine.getLocation());
        System.out.println(webEngine.getDocument());
        System.out.println(webEngine.getUserDataDirectory());

    }
}
