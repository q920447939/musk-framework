package org.example.musk.plugin.web.upload.limit.tenant;


public interface TenantUploadFileLimit {
    boolean fileExceedLimit(int fileCount, long fileBytes);
}
