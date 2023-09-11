package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kbslan.domain.annotation.I18NField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author panbo.guo
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_template_layout")
public class TemplateLayoutEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @I18NField(code = "traffic.template.layout.id")
    private Long id;

    @I18NField(code = "traffic.template.layout.vendorId")
    private Long vendorId;

    /**
     * 布局作用范围。同配置表scope含义
     */
    @I18NField(code = "traffic.template.layout.scope")
    private Integer scope;

    /**
     * 模板名称
     */
    @I18NField(code = "traffic.template.layout.name")
    private String name;

    /**
     * 布局类型
     */
    @I18NField(code = "traffic.template.layout.type")
    private Integer layoutType;

    /**
     * 价签类型
     */
    @I18NField(code = "traffic.template.layout.priceTagType")
    private Integer priceTagType;

    /**
     * 布局关联的所有模板类型
     */
    @I18NField(code = "traffic.template.layout.templateTypes")
    private String templateTypes;

    /**
     * 排版配置
     */
    @I18NField(code = "traffic.template.layout.config")
    private String config;

    /**
     * 布局排序值
     */
    @I18NField(code = "traffic.template.layout.orderIndex")
    private Integer orderIndex;

    private LocalDateTime created;

    private LocalDateTime modified;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    /**
     * 是否展示货架分割签
     * {@link com.dmall.pricetag.enums.YNEnum}
     */
    private Integer segmentLabel;
    /**
     * 分隔价签内容
     */
    private String segmentLabelContext;

    /**
     * 页眉页脚SVG【JSON格式】，其中有两个固定占位符：
     * 页码：{pageIndex}
     * 页数：{pageCount}
     *
     * 除了固定站位符外，可以使用数据源中的字段
     */
    private String headContent;


}
