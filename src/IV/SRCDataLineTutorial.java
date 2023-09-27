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
                    System.out.println(out.size());
                    System.out.println(targetLine.getBufferSize() / 5);
                    while (true) {
                        System.out.println(sourceLine.getFramePosition());
                        if (sourceLine.getFramePosition() == 44000) {
                            System.exit(1);
                        }
                        sourceLine.write(out.toByteArray(), 0, out.size());

                    }

                }

            };
            Thread targetThread = new Thread() {
                @Override
                public void run() {
                    targetLine.start();
                    byte[] data = new byte[targetLine.getBufferSize() / 5];
                    int readBytes;
                    while (true) {
                        //System.out.println(targetLine.getFramePosition());
//                        if (targetLine.getFramePosition() == 0){
//                            System.exit(4);
//                        }
                        readBytes = targetLine.read(data, 0, data.length);
                        out.write(data, 0, readBytes);

                    }
                }
            };

            targetThread.start();
            System.out.println(targetThread.getState());
            System.out.println("Started");
            Thread.sleep(1000);
            targetLine.stop();
            targetLine.close();
            System.out.println(targetThread.getState());
            sourceThread.start();
            Thread.sleep(1000);
            sourceLine.stop();
            sourceLine.close();
            System.out.println("Stopped");
            System.out.println(targetThread.getState());

        } catch (LineUnavailableException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}