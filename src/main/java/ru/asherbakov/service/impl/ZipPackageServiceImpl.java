package ru.asherbakov.service.impl;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.asherbakov.service.PackageService;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipPackageServiceImpl implements PackageService {

    @Override
    public File createZipFile(MultipartFile[] files, String name) {
        File resultFile = null;
        try {
            // Создаём архив
            String resultFileName = name + ".zip";
            resultFile = new File(resultFileName);
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(resultFile));
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + '.' + StringUtils.getFilenameExtension(file.getOriginalFilename());

                    // Создаём заглушку файла в архиве
                    ZipEntry entry = new ZipEntry(fileName); //file.getOriginalFilename()
                    zout.putNextEntry(entry);

                    // Получаем байт-код файла
                    byte[] buffer = file.getBytes();

                    // Записываем данные в файл-заглушку
                    zout.write(buffer);
                }
            }
            zout.closeEntry();
            zout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultFile;
    }


    @Override
    public byte[] createZipFile(Map<Path, String> files, String name) {
        // Используем baos для формирования файла в памяти
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // Создаём архив
            ZipOutputStream zout = new ZipOutputStream(baos);
            for (Map.Entry<Path, String> entry : files.entrySet()) {
                String fileName = "";
                if (entry.getValue() == null) {
                    fileName = UUID.randomUUID().toString() + '.' + StringUtils.getFilenameExtension(entry.getKey().getFileName().toString());
                } else {
                    fileName = entry.getValue();
                }

                // Создаём заглушку файла в архиве
                ZipEntry zipEntry = new ZipEntry(fileName);
                zout.putNextEntry(zipEntry);

                // Получаем байт-код файла
                File f = entry.getKey().toFile();
                FileInputStream fis = new FileInputStream(f);
                byte[] buffer = new byte[fis.available()];
                fis.close();

                // Записываем данные в файл-заглушку
                zout.write(buffer);

            }
            zout.closeEntry();
            zout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}
