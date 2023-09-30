package IV;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class audioIO {
    private static final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
            16, 2, 4, 44100, false);
    private static DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    private static SourceDataLine source;
    private static TargetDataLine target;

    private static final ByteArrayOutputStream audioOut = new ByteArrayOutputStream();


    public static void main(String[] args) {
        try {
            source = (SourceDataLine) AudioSystem.getLine(info);
            info = new DataLine.Info(TargetDataLine.class, format);
            target = (TargetDataLine) AudioSystem.getLine(info);
            target.open();
            source.open();
        } catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

        Thread targetThread = new Thread(() -> {
            byte[] data = new byte[8820];
            int readBytes;
            target.start();
            source.start();
            while (true) {
                readBytes = target.read(data, 0, data.length);
                source.write(data, 0, readBytes);
            }
        }); //End of targetThread

        //running
        targetThread.start();
        System.out.println("Recording");


    }
}
