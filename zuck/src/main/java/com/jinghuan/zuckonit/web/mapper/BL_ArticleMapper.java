package com.jinghuan.zuckonit.web.mapper;

import com.jinghuan.zuckonit.web.entity.BL_Article;
import com.jinghuan.zuckonit.web.vo.BL_ArticleIndexVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface BL_ArticleMapper extends Mapper{
    /**
     * @Description 根据主键查询文章
     * @param b1_id 主键
     * @return 文章
     */
    BL_Article searchBlArticleById(Integer b1_id);
    List<HashMap> searchArticleList();
    List<BL_ArticleIndexVO> queryList(BL_ArticleIndexVO query);
    Integer insertOneArticle(BL_Article bl_article);
}
