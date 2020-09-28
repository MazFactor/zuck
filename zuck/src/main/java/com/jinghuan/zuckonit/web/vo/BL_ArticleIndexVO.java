package com.jinghuan.zuckonit.web.vo;

import com.jinghuan.zuckonit.web.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BL_ArticleIndexVO extends BaseEntity {
    private Integer b1_id;
    private String b1_subject;
    private String b1_content;
//    private Integer b2_id;
    private Date create_time;
    private Date modify_time;
    private Integer b1_expose;
    private String b1_tag_01;
    private String b1_tag_02;
    private String b1_tag_03;
    private String b1_tag_04;
    private String b1_tag_05;
//    private Integer b3_id;
    private Integer b1_isMarkdown;
    /**排序字段*/
    private String sortString;
}
