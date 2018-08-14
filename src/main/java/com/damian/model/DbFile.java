package com.damian.model;
import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.sql.JDBCType.BLOB;

@Entity
@Table(name = "files")
public class DbFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Basic
    @Column(name = "file_name", length = 300)
    private String fileName;
    @Basic
    @Column(name = "file_type", length = 100)
    private String fileType;
    

    @Basic
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;

    @Basic
    @Column(name = "order_id")
    private Long  orderId;

    public DbFile() {
    }

    public DbFile(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }



    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}