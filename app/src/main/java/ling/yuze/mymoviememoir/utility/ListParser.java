package ling.yuze.mymoviememoir.utility;

import java.util.List;

public class ListParser {
    public static String listToString(List<String> list) {
        StringBuffer buffer = new StringBuffer();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            buffer.append(list.get(i));
            if (i != length - 1)
                buffer.append(", ");
        }

        return buffer.toString();
    }

}
