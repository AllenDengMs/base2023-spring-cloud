package org.backend.cloud.web.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;

public class FileTool {

  public static String readStringFromResourceFolder(String filePath) {
    InputStream inputStream = readFromResourceFolder(filePath);
    if (inputStream == null) {
      return null;
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    return reader.lines().collect(Collectors.joining(System.lineSeparator()));
  }

  public static String readStringFromTheSameFolderAsJarFile(String filePath) {
    InputStream inputStream = readFromTheSameFolderAsJarFile(filePath);
    if (inputStream == null) {
      return null;
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    return reader.lines().collect(Collectors.joining(System.lineSeparator()));
  }

  /**
   * 读取jar包内部src/resource目录下面的文件
   */
  private static InputStream readFromResourceFolder(String filePath) {
    try {
      ClassPathResource classPathResource = new ClassPathResource(filePath);
      return classPathResource.getInputStream();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * 读取jar包外部，和jar包同级目录的文件
   */
  private static InputStream readFromTheSameFolderAsJarFile(String filePath) {
    try {
      ApplicationHome home = new ApplicationHome(FileTool.class);
      String jarPath = home.getSource().getParentFile().toString();
      return Files.newInputStream(Paths.get(jarPath.concat("/").concat(filePath)));
    } catch (IOException e) {
      return null;
    }
  }

}
