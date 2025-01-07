package org.example.musk.plugin.web.upload.limit.member;


public interface TenantMemberUploadFileLimit {
    boolean fileExceedLimit(int fileCount, long fileBytes, int memberId);
}
