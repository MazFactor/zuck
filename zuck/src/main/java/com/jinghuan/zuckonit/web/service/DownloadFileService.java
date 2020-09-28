package com.jinghuan.zuckonit.web.service;

public interface DownloadFileService {
    public boolean downloadArticleImages(String ftpImagePath, String content, String targetLocation);
}
