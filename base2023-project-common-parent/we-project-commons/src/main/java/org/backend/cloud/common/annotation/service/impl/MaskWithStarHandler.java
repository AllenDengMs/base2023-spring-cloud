package org.backend.cloud.common.annotation.service.impl;

import org.backend.cloud.common.annotation.service.MaskDataHandler;

public class MaskWithStarHandler implements MaskDataHandler {

  @Override
  public Object maskData(Object data) {
    return "******";
  }
}
