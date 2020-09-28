package com.jinghuan.zuckonit.web.service.impl;

import com.jinghuan.common.util.FtpUtil;
import com.jinghuan.zuckonit.web.service.DownloadFileService;
import com.jinghuan.zuckonit.web.util.HtmlUtils;
//import com.jinghuan.zlog.ZLSimpleLogger;
//import com.jinghuan.zlog.ZLoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadFileServiceImpl implements DownloadFileService {
//    private static final ZLSimpleLogger logger = ZLoggerFactory.getSimpleLogger(UploadFileServiceImpl.class);
    /**
     * 下载文章图片到本地
     * @param content 文章内容
     * @return 下载结果：成功-true；失败-false
     */
    @Override
    public boolean downloadArticleImages(String ftpImageParent, String content, String targetLocation){
        boolean downloadResult = true;
        List<String> imageList = HtmlUtils.getImageSrc(content);

        String[] name;
        try {
            for (String imageName : imageList) {
                name = imageName.split("\\/");
                FtpUtil.downSftpFile(name[name.length-1], ftpImageParent, name[name.length-1], targetLocation);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
//            logger.error(e.getMessage(), e);
            downloadResult = false;
        }
        return downloadResult;
    }
}
