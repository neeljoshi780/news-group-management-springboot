package com.newsgroupmanagement.util;

import com.newsgroupmanagement.exception.FileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileOperation {

    String projectRootPath = System.getProperty("user.dir");
    String projectParentPath = new File(projectRootPath).getParent();
    String relativeImagePath = "news-group-management-uploads" + File.separator + "images";

    // Profile picture uploading
    public String profilePhotoUpload(MultipartFile file) {

        // Check if directory exists, else create it
        File dirPath = new File(projectParentPath + File.separator + relativeImagePath + File.separator + "profile");
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        try{
            String profilePhotoName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            File path = new File(projectParentPath + File.separator + relativeImagePath + File.separator + "profile" + File.separator + profilePhotoName);
            file.transferTo(path);
            return profilePhotoName;
        } catch (IOException e) {
            throw new FileException("Could not store profile photo " + file.getOriginalFilename() + " " + e.getMessage());
        }
    }

    // Old profile picture deleting
    public void deleteProfilePhoto(String photoName) {
        File oldProfilePhoto = new File(projectParentPath + File.separator + relativeImagePath + File.separator + "profile" + File.separator + photoName);
        oldProfilePhoto.delete();
    }

    // News Photo uploading
    public String newsPhotoUpload(MultipartFile file) {

        // Check if directory exists, else create it
        File dirPath = new File(projectParentPath + File.separator + relativeImagePath + File.separator + "post");
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        try{
            String newsPhotoName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            File path = new File(projectParentPath + File.separator + relativeImagePath + File.separator + "post" + File.separator + newsPhotoName);
            file.transferTo(path);
            return newsPhotoName;
        } catch (IOException e) {
            throw new FileException("Could not store news photo " + file.getOriginalFilename() + " " + e.getMessage());
        }
    }

    // if user news deleted to news photo deleting
    public void deleteNewsPhoto(String photoName) {
        File deletedNewsPhoto = new File(projectParentPath + File.separator + relativeImagePath + File.separator + "post" + File.separator + photoName);
        deletedNewsPhoto.delete();
    }
}
