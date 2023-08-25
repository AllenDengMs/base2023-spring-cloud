package org.backend.cloud.common.builder;

public class PageQueryBuilder {

  private int currentPage = 0;
  private int size = 10;

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
}
