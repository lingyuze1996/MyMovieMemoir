package ling.yuze.mymoviememoir.utility;

public class Validation {
    public static boolean checkLength(String s, int length) {
        if (s.length() >= length || s.length() == 0)
            return false;
        return true;
    }
}
