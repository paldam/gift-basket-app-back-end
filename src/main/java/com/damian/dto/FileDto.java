package com.damian.dto;

import com.damian.domain.order_file.DbFile;

public class FileDto {

    private Long fileId;
    private Long orderId;

    public FileDto() {
    }

    public FileDto(Long fileId,Long orderId) {
        this.fileId = fileId;
        this.orderId = orderId;
    }

    public FileDto(DbFile dbFile) {
        this.fileId = dbFile.getFileId();
        this.orderId = dbFile.getOrderId();
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
