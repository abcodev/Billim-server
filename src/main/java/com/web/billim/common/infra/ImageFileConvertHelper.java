package com.web.billim.common.infra;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ImageFileConvertHelper {

    public static File convertBase64EncodedStringToImageFile(String encodedString, String fileName) {
        String[] strings = encodedString.split(",");
        String extension;
        switch (strings[0]) { // check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default: // should write cases for more images types
                extension = "jpg";
                break;
        }
        // convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        File file = new File(fileName + extension);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
        } catch (IOException e) {
//            e.printStackTrace();
            log.error(e.getMessage());
        }
        return file;
    }

}
