package util;

import org.markdown4j.Markdown4jProcessor;

import java.io.*;

public class Conversion {
    public static File MH(File file) throws IOException {
        String html = new Markdown4jProcessor().process(file);
        ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while((bytesRead = stream.read(buffer,0,8129))!=-1){
            os.write(buffer,0,bytesRead);
        }
        os.close();
        stream.close();

        return file;
    }
}







