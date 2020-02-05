package ae.dsg.happiness;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by dsg216 on 3/21/16.
 */
public class Utils {

    public static String generateHash(String text) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(text.getBytes());
            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String generate16DigitRandom() {
        Random rand = new Random();
        long accumulator = 1 + rand.nextInt(9); // ensures that the 16th digit isn't 0
        for(int i = 0; i < 15; i++) {
            accumulator *= 10L;
            accumulator += rand.nextInt(10);
        }
        String val = String.valueOf(accumulator);
        return val;
    }


    public static String getUTCDate() {
        Date date =  new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String stDate = formatter.format(date);
        return stDate;
    }

    /**
     * Encode String to UTF-8 Encoding
     * @param string
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8");
    }
}
