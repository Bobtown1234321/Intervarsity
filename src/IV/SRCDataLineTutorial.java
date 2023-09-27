package IV;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SRCDataLineTutorial {
    public static void main(String[] args) {
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
                    16, 2, 4, 44100, false);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            System.out.println();
            sourceLine.open();

            info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            Thread sourceThread = new Thread() {
                @Override
                public void run() {
                    sourceLine.start();
                    while (true) {
                        sourceLine.write(out.toByteArray(), 0, out.size());
                    }
                }
            };

            Thread targetThread = new Thread() {
                @Override
                public void run() {
                    targetLine.start();
                    byte[] data = new byte[targetLine.getBufferSize() / 10];
                    int readBytes;
                    while (true) {
                        readBytes = targetLine.read(data, 0, data.length);

                        out.write(data, 0, readBytes);

                    }
                }
            };
            targetThread.start();
            System.out.println("Started");
            Thread.sleep(10);
            sourceThread.start();
            Thread.sleep(50000);
            targetLine.stop();
            targetLine.close();
            sourceLine.stop();
            sourceLine.close();
            System.out.println("Stopped");

        } catch (LineUnavailableException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
