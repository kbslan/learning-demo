package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分类
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Category implements Serializable {

    private static final long serialVersionUID = -895053445326961623L;
    /**
     * 分类id
     */
    private Integer id;
    /**
     * 整棵树形里边的上级id，跟catType有关
     */
    private Integer parent;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 层级-（指的是各自所属的采购类目、或者营运类目、或者小分类的级次
     */
    private Integer level;
    /**
     * 总层级，品类到小分类的总层级
     */
    private Integer totalLevel;
    /**
     * 是否末级 -（指的是各自所属的采购类目、或者营运类目、或者小分类的是否末级判断）
     */
    private Boolean leaf;
    /**
     * 对应的品类架构类型：1、采购品类架构 2、营运课组架构 3、商家小分类
     */
    private Integer catType;
    /**
     * 门店类型 0没有门店类型;1大卖场;2连锁店;3尚佳会员店;4便利店;5物美参;6虚拟门店;7标超;8卫星站（如果是采购或者小分类，值为0，如果是营运，值为1-8）
     */
    private Integer shopType;

}
