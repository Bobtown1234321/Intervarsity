package IV;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.*;


public class audioToolbarController implements Initializable {

    private final List<Mixer> audioInput = new ArrayList<>();
    private final List<Mixer> audioOutput = new ArrayList<>();
    private final List<Mixer> others = new ArrayList<>();
    private final ObservableList<String> mixerOList = FXCollections.observableArrayList();
    private final AudioFormat analogIn = new AudioFormat(48000, 128,
            2, false, false);

    private Mixer testMixer;
    private TargetDataLine inputLine;
    
    @FXML
    private ChoiceBox inputLines;
    @FXML
    private ChoiceBox outputLines;


    @FXML
    private void displayOtherInfo() {
        int count = 0;
        for (Mixer mix : others) {
            System.out.println(count++);
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

    @FXML
    private void testPort() throws LineUnavailableException {
        Line inputPort = testMixer.getLine(Port.Info.MICROPHONE);
        System.out.println(inputPort.getLineInfo());
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, analogIn);
        System.out.println(info);

        try{
            inputLine = (TargetDataLine) AudioSystem.getLine(info);
            inputLine.open(analogIn);
        } catch (LineUnavailableException e){
            System.out.println("Line Unavailable");
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * This is called when the user selects an input from the choice-box
     * otherlines.
     *
     * @param e The event that was called
     */
    private void handleInputSel(Event e) {
        testMixer = audioInput.get(inputLines.getSelectionModel().getSelectedIndex());
        System.out.println(testMixer.getMixerInfo().getName());
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
        mixerOList.addAll(audioInput.stream()
                .map(e -> e.getMixerInfo().getName())
                .toList());

        inputLines.setItems(mixerOList);
        inputLines.setValue("Select Audio Input");
        inputLines.setOnAction(this::handleInputSel);

    }
}



