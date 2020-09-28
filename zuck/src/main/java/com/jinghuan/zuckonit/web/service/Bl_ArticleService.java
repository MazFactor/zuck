package com.jinghuan.zuckonit.web.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinghuan.zuckonit.web.mapper.BL_ArticleMapper;
import com.jinghuan.zuckonit.web.entity.BL_Article;
import com.jinghuan.zuckonit.web.util.HtmlUtils;
import com.jinghuan.zuckonit.web.vo.BL_ArticleIndexVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Bl_ArticleService {
    @Autowired
    private BL_ArticleMapper bl_articleMapper;
    /**
     * 根据ID查找文章
     * @param b1_id 文章ID
     * @return 文章
     */
    public BL_Article findBlArticleById(int b1_id) {
        return bl_articleMapper.searchBlArticleById(b1_id);
    }

    @Transactional(readOnly = true)
    public List<BL_ArticleIndexVO> queryList(BL_ArticleIndexVO query) {
        query.setDeleteFlag(false);  //默认未删除
        query.setSortString("create_time desc");
        return bl_articleMapper.queryList(query);
    }

    @Transactional(readOnly = true)
    public PageInfo<BL_ArticleIndexVO> queryPage(BL_ArticleIndexVO query) {
        query.setDeleteFlag(false);  //默认未删除
        query.setSortString("create_time desc");
        PageHelper.startPage(query.getPage(), query.getSize());
        List<BL_ArticleIndexVO> resultList = queryList(query);
        for (BL_ArticleIndexVO bl_articleVo : resultList) {
            if (null != bl_articleVo.getB1_content() && bl_articleVo.getB1_content().length() > 0)
                bl_articleVo.setB1_content(HtmlUtils.getExcerpt(bl_articleVo.getB1_content()));
//                bl_articleVo.setB1_content(StringAnalysis.getStringAnalysed(new IKAnalyzer6x(), HtmlUtils.Html2Text(bl_articleVo.getB1_content())));
//                if(HtmlUtils.Html2Text(bl_articleVo.getB1_content()).length() > 90)
//                    bl_articleVo.setB1_content(HtmlUtils.Html2Text(bl_articleVo.getB1_content()).substring(0,89) + "...");
//                else
//                    bl_articleVo.setB1_content(HtmlUtils.Html2Text(bl_articleVo.getB1_content()));
        }
        return new PageInfo<>(resultList);
    }
    /**
     * 插入文章
     * @param bl_article 文章
     */
    public void insertOneArticle(BL_Article bl_article ) {
        bl_articleMapper.insertOneArticle(bl_article);
    }
}
