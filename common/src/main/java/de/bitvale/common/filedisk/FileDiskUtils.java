package de.bitvale.common.filedisk;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDiskUtils {

    public static File workingFile(UUID id) throws IOException {
        String home = System.getProperty("user.home");
        File meld = new File(home + File.separator + ".meld");
        FileUtils.forceMkdir(meld);
        File file = new File(meld.getCanonicalPath() + File.separator + id.toString());
        if (file.isFile()) {
            return file;
        } else {
            file.createNewFile();
        }
        return file;
    }

    public static Base64Resource extractBase64(String value) {
        Pattern pattern = Pattern.compile("data:(\\w+)/(\\w+);base64,.*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(value);
        boolean matches = matcher.matches();
        String type = matcher.group(1);
        String subType = matcher.group(2);

        String result = value.replaceFirst("data:(\\w+)/(\\w+);base64,", "");

        Base64.Decoder decoder = Base64.getMimeDecoder();
        byte[] decode = decoder.decode(result);


        return new Base64Resource(type, subType, decode);
    }

    public static String buildBase64(String type, String subType, byte[] data) {
        Base64.Encoder encoder = Base64.getMimeEncoder();
        byte[] encode = encoder.encode(data);

        return "data:" + type + "/" + subType+ ";base64," + new String(encode);
    }

}
