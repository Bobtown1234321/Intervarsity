package interVarsityUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class audioToolbarController {
    @FXML
    private ChoiceBox audio;


    @FXML
    private void onTest() {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();

        ObservableList<String> observableList = FXCollections.observableArrayList();
        List<Line.Info> availableLines = new ArrayList<Line.Info>();

        for (Mixer.Info mixerInfo : mixers) {
            System.out.println("Found Mixer: " + mixerInfo);
            observableList.add(mixerInfo.getDescription());
            Mixer m = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lines = m.getTargetLineInfo();
            System.out.println(mixerInfo.getName());
            System.out.println(mixerInfo.getDescription());
            System.out.println(mixerInfo.getVendor());
            System.out.println(mixerInfo.getVersion());
            System.out.println(m.getLineInfo());
            System.out.println(m.getSourceLines());
            System.out.println(m.getTargetLines());
            System.out.println();


            for (Line.Info li : lines) {
                System.out.println();
                //System.out.println("Found target line: " + li);
                try {
                    m.open();
                    System.out.println("Line in: " + m.isLineSupported(Port.Info.LINE_IN));
                    System.out.println("Line out " + m.isLineSupported(Port.Info.LINE_OUT));
                    System.out.println("CD: " + m.isLineSupported(Port.Info.COMPACT_DISC));
                    System.out.println("H: " + m.isLineSupported(Port.Info.HEADPHONE));
                    System.out.println("S: " + m.isLineSupported(Port.Info.SPEAKER));
                    System.out.println("M: " + m.isLineSupported(Port.Info.MICROPHONE));
                    availableLines.add(li);
                } catch (LineUnavailableException e) {

                    System.out.println("Line unavailable.");
                }
            }
        }
        //audio.setItems(observableList);
        //System.out.println("Available lines: " + availableLines);
    }

}



