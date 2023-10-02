import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.sound.sampled.*;
import java.beans.EventHandler;
//Ryan Massel
//10/1/2023 Finished

public class audioIO extends Application {
    private static final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
            16, 2, 4, 44100, false);
    private static DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    private static SourceDataLine source;
    private static TargetDataLine target;
    private static final int BUFFER_SIZE = 5520; //smallest possible without issues
    private static int readBytes;
    private final Button stopButton = new Button("Stop");

    @Override
    public void start(Stage stage) throws Exception {
        try {
            source = (SourceDataLine) AudioSystem.getLine(info);
            info = new DataLine.Info(TargetDataLine.class, format);
            target = (TargetDataLine) AudioSystem.getLine(info);
            info = new DataLine.Info(Port.class, format);
            target.open(format, BUFFER_SIZE);
            source.open(format, BUFFER_SIZE);
        } catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

        //running
        startThread();

        stopButton.setStyle("-fx-font-size:35");
        stopButton.setPrefSize(175,50);
        stopButton.setOnAction(this::onStop);

        Scene scene = new Scene(stopButton);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("AudioIO");
        stage.setScene(scene);
        stage.show();
    }

    private void startThread(){
        byte[] data = new byte[BUFFER_SIZE / 4];
        Thread single = new Thread(() -> {
            target.start();
            source.start();
            do {
                readBytes = target.read(data, 0, data.length);
                source.write(data, 0, readBytes);
            } while (target.isActive());
        });
        single.start();
    }
    protected void onRestart(ActionEvent actionEvent){
        startThread();
        stopButton.setText("Stop");
    }

    protected void onStop(ActionEvent actionEvent){
        if(stopButton.getText().matches("Resume")){
            onRestart(actionEvent);
        } else {
            target.stop();
            target.drain();
            source.stop();
            stopButton.setText("Resume");
        }
    }

    protected void onClose(ActionEvent actionEvent){
        target.close();
        source.close();
        System.out.println("Closed");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
