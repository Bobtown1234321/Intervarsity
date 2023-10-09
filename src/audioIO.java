import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.sound.sampled.*;

//Ryan Massel
//10/2/2023 Finished
//Takes in audio from the soundboard (Target) and outputs it via the HDMI cord(Source)
//So the tech team can utilize the speakers already in the room.

public class audioIO extends Application {
    private static final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
            16, 2, 4, 44100, false);
    private static DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    private static SourceDataLine source;
    private static TargetDataLine target;
    private static final int BUFFER_SIZE = 5520; //5520 smallest possible without issues
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
        stage.setOnCloseRequest(this::onClose);
        stage.setTitle("AudioIO");
        stage.setScene(scene);
        stage.show();
    }

    //Thread running the reading from the target line and
    //writes it to the source line.
    private void startThread(){
        byte[] data = new byte[BUFFER_SIZE / 4]; //Divided by 4 to prevent blocking (Stuttering)
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

    //Restarts Audio Playback
    protected void onRestart(ActionEvent actionEvent){
        startThread();
        stopButton.setText("Stop");
    }
    //Pauses Audio Playback
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
    //When window closes ends the target and source Streams.
    protected void onClose(WindowEvent windowEvent){
        if(target.isActive()){
            target.stop();
            source.stop();
            target.flush();
            source.flush();
        }
        target.close();
        source.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
