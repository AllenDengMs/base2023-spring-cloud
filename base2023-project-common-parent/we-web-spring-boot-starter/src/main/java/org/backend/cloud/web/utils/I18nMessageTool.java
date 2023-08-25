package org.backend.cloud.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.MessageSource;

public class I18nMessageTool {

  private static MessageSource messageSource;

  private static final Pattern pattern = Pattern.compile("\\$\\{(.*?):(.*?)}");

  public static final Object[] EMPTY_ARGS = new Object[0];

  public static void setMessageSource(MessageSource messageSource) {
    I18nMessageTool.messageSource = messageSource;
  }

  public static String getKey(String message) {
    Matcher matcher = pattern.matcher(message);
    return matcher.find() ? matcher.group(1) : null;
  }

  public static String getValue(String message) {
    // 提取默认国际化翻译：加速message的值是“{error.msg:错误消息}”，那么提出出来的结果是：“错误消息”
    if (message == null || !message.startsWith("${")) {
      return message;
    }
    Matcher matcher = pattern.matcher(message);
    return matcher.find() ? matcher.group(2) : message;
  }

  public static String translate(String message) {
    if (I18nMessageTool.messageSource == null) {
      // 不进行国际化翻译，直接提取默认值
      return getValue(message);
    }

    String i18nKey = getKey(message);
    if (i18nKey == null) {
      return message;
    }

    try {
      // 如果异常信息的格式是: {i18nKey:默认错误信息}，则会进来这里，去查src/i18n/message.properties
      return messageSource.getMessage(i18nKey, EMPTY_ARGS, LanguageTool.getRequestLanguage());
    } catch (Exception e) {
      // 找不到国际化翻译，提取默认值
      return getValue(message);
    }
  }
}
