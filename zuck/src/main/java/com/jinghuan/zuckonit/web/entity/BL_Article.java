package com.jinghuan.zuckonit.web.entity;

import com.jinghuan.zuckonit.web.base.Entity.AutoIncrementKeyBaseDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
//@EqualsAndHashCode(callSuper = true)
@Table(name = "`bl_article`")
public class BL_Article extends AutoIncrementKeyBaseDomain<Integer> {

    @Column(name = "`B1_ID`")
    private Integer b1_id;

    private String b1_subject;

    private String b1_content;

//    private String b2_id;

    @Column(name = "`create_time`")
    //@DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT_PATTERN_DATETIME)
    //@JsonFormat(pattern = DateUtil.DEFAULT_FORMAT_PATTERN_DATETIME, timezone = DateUtil.DEFAULT_TIME_ZONE_TYPE)
    private Date create_time;

    @Column(name = "`create_time`")
    //@DateTimeFormat(pattern = DateUtil.DEFAULT_FORMAT_PATTERN_DATETIME)
    //@JsonFormat(pattern = DateUtil.DEFAULT_FORMAT_PATTERN_DATETIME, timezone = DateUtil.DEFAULT_TIME_ZONE_TYPE)
    private Date modify_time;

    private Integer b1_expose;

    private String b1_tag_01;
    private String b1_tag_02;
    private String b1_tag_03;
    private String b1_tag_04;
    private String b1_tag_05;

//    private Integer b3_id;

    private Integer b1_isMarkDown;

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object var1) {
        return false;
    }
}
