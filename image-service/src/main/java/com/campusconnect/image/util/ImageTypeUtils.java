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
        System.out.println("here");
        InputStream is = file.getInputStream();
        System.out.println("here 2");
        String type = FileTypeUtil.getType(is);
        if (type.equalsIgnoreCase("JPG")
                || type.equalsIgnoreCase("JPEG")
                || type.equalsIgnoreCase("PNG")
        ) {
            return "image/"+type;
        } else {
            throw new InvalidFileTypeException();
        }
    }


}
