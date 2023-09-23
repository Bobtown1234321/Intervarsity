package IV;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class audioToolbarController implements Initializable {

    private final ArrayList<Mixer> audioInput = new ArrayList<>();
    private final ArrayList<Mixer> audioOutput = new ArrayList<>();
    private final ArrayList<Mixer> others = new ArrayList<>();


    @FXML
    private void displayOtherInfo() {
        for (Mixer mix : others) {
            System.out.println(mix.getMixerInfo().getName());
            System.out.println(Arrays.toString(mix.getSourceLineInfo()));
            System.out.println(Arrays.toString(mix.getTargetLineInfo()) + "\n");
        }
    }

    @FXML
    private void displayInputInfo() {
        for (Mixer mix : audioInput) {
            System.out.println(mix.getMixerInfo().getName());
            System.out.println(Arrays.toString(mix.getSourceLines()));
            System.out.println(Arrays.toString(mix.getTargetLines()));
        }
    }

    @FXML
    private void displayOutputInfo() {
        for (Mixer mix : audioOutput) {
            System.out.println(mix.getMixerInfo().getName());
            System.out.println(Arrays.toString(mix.getSourceLines()));
            System.out.println(Arrays.toString(mix.getTargetLines()));
        }
    }

    /**
     * Gets the audio I/O from available and sorts them into Inputs, Outputs, or Others
     *
     * @param mixer Gets the mixers from the audio System
     */
    private void sortTypeofAudioIO(Mixer.Info[] mixer) {
        audioOutput.clear();
        audioInput.clear();
        others.clear();
        for (Mixer.Info mixerInfo : mixer) {
            Mixer m = AudioSystem.getMixer(mixerInfo);
            boolean isInput = m.isLineSupported(Port.Info.LINE_IN) || m.isLineSupported(Port.Info.MICROPHONE);
            boolean isOutput = m.isLineSupported(Port.Info.LINE_OUT) || m.isLineSupported(Port.Info.SPEAKER)
                    || m.isLineSupported(Port.Info.HEADPHONE);
            if (isInput) {
                audioInput.add(m);
            } else if (isOutput) {
                audioOutput.add(m);
            } else {
                others.add(m);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        sortTypeofAudioIO(mixers);
    }
}



