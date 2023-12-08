package com.campusconnect.image.util;

import cn.hutool.core.io.FileTypeUtil;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ImageTypeUtils {

    public String getImageType(MultipartFile file)
        throws IOException, InvalidFileTypeException
    {
        InputStream is = file.getInputStream();
        String type = FileTypeUtil.getType(is);
        System.out.println("file type is: " + type);
        if (type.equalsIgnoreCase("JPG")
                || type.equalsIgnoreCase("JPEG")
                || type.equalsIgnoreCase("PNG")
        ) {
            return "image/"+type;
        } else {
            System.out.println("throwing exception");
            throw new InvalidFileTypeException();
        }
    }


}
