package IV;

import javax.sound.sampled.*;


public class audioIO {
    private static final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100,
            16, 2, 4, 44100, false);
    private static DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    private static SourceDataLine source;
    private static TargetDataLine target;
    private static final int BUFFERSIZE = 5520;
    private static int readBytes;

    public static void main(String[] args) {
        try {
            source = (SourceDataLine) AudioSystem.getLine(info);
            info = new DataLine.Info(TargetDataLine.class, format);
            target = (TargetDataLine) AudioSystem.getLine(info);
            info = new DataLine.Info(Port.class, format);
            target.open(format, BUFFERSIZE);
            source.open(format, BUFFERSIZE);
        } catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

        byte[] data = new byte[BUFFERSIZE / 4];

        Thread single = new Thread(() -> {
            target.start();
            source.start();
            while (true) {
                readBytes = target.read(data, 0, data.length);
                source.write(data, 0, readBytes);
            }
        });
        //running
        single.start();

    }
}
