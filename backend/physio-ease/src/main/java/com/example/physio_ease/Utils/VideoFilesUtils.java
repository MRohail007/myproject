package com.example.physio_ease.Utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.physio_ease.Constant.Constants.VIDEO_UPLOAD_DIR;

@Component
public class VideoFilesUtils {

    /*
    =============
    Method for extracting video file extension
    =============
    */
    public static String getVideoFileExtension(MultipartFile videoFile) {
        String originalFilename = videoFile.getOriginalFilename();
        if (originalFilename != null) {
            int lastIndexOfDot = originalFilename.lastIndexOf('.');
            if (lastIndexOfDot > 0 && lastIndexOfDot < originalFilename.length() - 1) {
                return originalFilename.substring(lastIndexOfDot + 1).toLowerCase();
            }
        }
        return "";
    }

    /*
    =============
    Method for getting video file size
    =============
     */
    public static Long getFileSize(MultipartFile videoFile) {
        File videoFileSize = new File(VIDEO_UPLOAD_DIR, videoFile.getOriginalFilename());
        if (videoFileSize.exists()) {
            return videoFile.getSize();
        } else {
            return null;
        }
    }

    /*
    =============
    Method for getting video file duration
    =============
     */
    public static Long getVideoDuration(MultipartFile videoFile) {
        // Create a temp file to hold the values of the multipart file
        File tempFile = null;
        try {
            // Create a temporary file in the system's temp directory
            tempFile = File.createTempFile("tempVideo", ".tmp");
            tempFile.deleteOnExit();
            // Transfer the contents of the MultipartFile to the temporary file
            videoFile.transferTo(tempFile);
            // Get the duration using FFmpeg
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(tempFile);
            grabber.start();
            long durationInMicroseconds = grabber.getLengthInTime();
            grabber.stop();
            return durationInMicroseconds / 1000000;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Clean the temp file if exists
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /*
    ===============
    Method for saving video files to system
    ===============
     */
    public static String saveVideoFile(MultipartFile videoFile) throws IOException {
        File videoFileDirectory = new File(VIDEO_UPLOAD_DIR);

        if (!videoFileDirectory.exists()) {
            videoFileDirectory.mkdirs();
        }
        // Save file to Directory
        Path filePath = Paths.get(VIDEO_UPLOAD_DIR, videoFile.getOriginalFilename());
        Files.copy(videoFile.getInputStream(), filePath);
        return filePath.toString();
    }
}
