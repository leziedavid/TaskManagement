package com.mobisoft.taskmanagement.pagination;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private long totalElements;
    private int currentPage;
    private int pageSize;

    public Page(List<T> content, long totalElements, int currentPage, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalPages() {
        return (long) Math.ceil((double) totalElements / pageSize);
    }
}
