import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SRCDataLineTutorial {
    public static void main(String[] args) {
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
                    16, 2, 4, 44100, false);

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);


            info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);


            final ByteArrayOutputStream out = new ByteArrayOutputStream();

                targetLine.open();
                sourceLine.open();
                Thread sourceThread = new Thread() {
                    @Override
                    public void run() {

                        //System.out.println(out.size());
                        //System.out.println(targetLine.getBufferSize() / 5);
                        while (true) {
                            //System.out.println(sourceLine.getFramePosition());


                        }

                    }

                };
                Thread targetThread = new Thread() {
                    @Override
                    public void run() {
                        targetLine.start();
                        sourceLine.start();
                        byte[] data = new byte[32];
                        int readBytes;
                        while (true) {
                            readBytes = targetLine.read(data, 0, data.length);
                            out.write(data, 0, readBytes);
                            sourceLine.write(out.toByteArray(), 0, data.length);

                        }


                    }

                };


            targetThread.start();
            //System.out.println(targetThread.getState());
            System.out.println("Started");
            //System.out.println(sourceLine.available());
            //System.out.println(out.size());
            Thread.sleep(1000000);

            System.out.println(sourceLine.available());
            System.out.println(out.size());
            System.out.println("Data Collected");
            //System.out.println(targetThread.getState());
            //sourceThread.start();
            //Thread.sleep(1000);
            targetLine.stop();
            sourceLine.stop();
            targetLine.close();
            sourceLine.close();
            System.out.println("Stopped");
            //System.out.println(targetThread.getState())

        } catch (LineUnavailableException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
