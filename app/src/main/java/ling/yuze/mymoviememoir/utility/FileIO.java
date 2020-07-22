package ling.yuze.mymoviememoir.utility;


import android.content.Context;

import java.io.InputStream;
import java.util.Scanner;

public class FileIO {
    public static String readFile(Context context, int resourceId) {
        String content = "";
        StringBuffer buffer = new StringBuffer();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine() + "\n");
            }

            // remove the last "\n"
            if (buffer.length() != 0)
                content = buffer.substring(0, buffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }



}
