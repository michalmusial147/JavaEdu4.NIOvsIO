import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Random;
import  java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.CharBuffer;



public class Main {

    public static final int N = 1000;

    public static void generate(char[] array, int N){
        Random r = new Random();
        for (int it = 0; it < N; it++)
        {
            array[it] = (char) (r.nextInt(26) + 'a');
        }
    }

    static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }

    public static void writeIO (String filename, char[]x) throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filename));
        for (int it = 0; it < x.length; it++) {
            outputWriter.write(x[it]+"");
        }
        outputWriter.flush();
        outputWriter.close();
    }
    public static void readIO (String filename, char[]x) throws IOException{
        BufferedReader inputReader = null;
        inputReader = new BufferedReader(new FileReader(filename));
        for (int it = 0; it < x.length; it++) {
            x[it]= (char)inputReader.read();
        }
        inputReader.close();
    }

    public static void writeNIO(String fileName, char[] content) throws IOException {
        String path = System.getProperty("user.dir");
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("make dir failed!");
            }
        } else {
            if (!file.isDirectory()) {
                return;
            }
        }
        File temp = new File(path + "//" + fileName);
        if (!temp.exists()) {
            if (!temp.createNewFile()) {
                throw new IOException("create new file failed!");
            }
        }
        ByteBuffer buffer = ByteBuffer.wrap(toBytes(content));
        FileChannel out = new FileOutputStream(fileName).getChannel();
        out.write(buffer);
        buffer.clear();
        out.close();
    }

    public static void readNIO(String fileName, char[] destination) throws IOException {
        Path path = Paths.get(fileName);
        if (Files.notExists(path)) {
            throw new IOException("file doesn't exist!");
        }
        FileChannel inputCh = new FileInputStream(fileName).getChannel();
        MappedByteBuffer buffer = inputCh.map(FileChannel.MapMode.READ_ONLY, 0, inputCh.size());
        buffer.load();
        for(int i = 0; i < buffer.limit();i++) {
            destination[i]=(char)buffer.get();
        }
        buffer.clear();
        inputCh.close();
    }

    public static void main(String[] args)
    {
        String Filename = args[0];

        try
        {
            char[] array = new char[N];
            long millisActualTime;
            generate(array, N);

            millisActualTime = System.currentTimeMillis();
            writeIO(Filename, array);
            readIO(Filename, array);
            long IOexecutionTime = System.currentTimeMillis() - millisActualTime;
            Filename=Filename.replace(".txt", "NIO.txt");

            millisActualTime = System.currentTimeMillis();
            writeNIO(Filename, array);
            readNIO(Filename, array);
            long NIOexecutionTime = System.currentTimeMillis() - millisActualTime;
            System.out.println(Arrays.toString(array));
            System.out.println("IOexecutionTime: " + IOexecutionTime );
            System.out.println("NIOexecutionTime: "+NIOexecutionTime );
        }
        catch(IOException ex)
        {
             System.out.println (ex.toString());
             System.out.println("Could not find file " + Filename);
        }


    }
}
