package ru.asherbakov.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public interface PackageService {
    /**
     *
     * @param files
     * @param name
     * @return
     */
    File createZipFile(MultipartFile[] files, String name);

    /**
     * Package files to archive/
     * @param files
     * @param name
     * @return
     */
    byte[] createZipFile(Map<Path, String> files, String name);
}
