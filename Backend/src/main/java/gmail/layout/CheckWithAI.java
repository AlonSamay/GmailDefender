package gmail.layout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CheckWithAI {

    public boolean checkWithAI(String body) {
        boolean delete = false;
        try {
            ProcessBuilder builder = new ProcessBuilder("C:\\Users\\Alon\\AppData\\Local\\Programs\\Python\\Python38-32\\python",
                    "C:\\SpamDetection\\spamDetect.py", body);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            InputStream stdout = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if ((delete = line.equals("spam"))){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delete;
    }
}
