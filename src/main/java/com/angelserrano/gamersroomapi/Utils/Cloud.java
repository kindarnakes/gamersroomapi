package com.angelserrano.gamersroomapi.Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Cloud {
    private static final Cloud INSTANCE = new Cloud();
    private final Cloudinary cloudinary;

    public Cloud() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dqaoqcwvf",
                "api_key", "252531278735351",
                "api_secret", "vBPuw5ppiMXubAGcagfhC8VLreE"));
    }

    public static Cloud getINSTANCE() {
        return INSTANCE;
    }

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public String uploadImg(String base64) {
        try {
            Map upload = cloudinary.uploader().upload(base64, ObjectUtils.asMap("public_id", "Images/" + UUID.randomUUID().toString()));
            return (String) upload.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    public String uploadPortrait(String base64) {
        try {
            Map upload = cloudinary.uploader().upload(base64, ObjectUtils.asMap("public_id", "Portraits/" + UUID.randomUUID().toString()));
            return (String) upload.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    public String uploadVideo(String base64) {
        try {
            Map upload = cloudinary.uploader().upload(base64, ObjectUtils.asMap("public_id", "Video/" + UUID.randomUUID().toString()));
            return (String) upload.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String uploadAudio(String base64) {
        try {
            Map upload = cloudinary.uploader().upload(base64, ObjectUtils.asMap("public_id", "Video/" + UUID.randomUUID().toString()));
            return (String) upload.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean destroy(String url){
        boolean destroy = false;
        try {
            cloudinary.uploader().destroy(url.split("/")[7]+"/"+url.split("/")[8].split("\\.")[0], ObjectUtils.emptyMap());
            destroy = true;
        } catch (IOException|NullPointerException e) {
            e.printStackTrace();
        }
        return destroy;
    }
}
