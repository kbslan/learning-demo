package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统相关配置表
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_config")
public class SysConfigEntity implements Serializable {

    private static final long serialVersionUID = 8569642647966182017L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家ID
     */
    private Long vendorId;

    /**
     * 组ID
     */
    private Long groupId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 作用域:10-系统, 20-商家, 30-组, 40-门店
     */
    private Integer configScope;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置编码
     */
    private String configKey;

    /**
     * 配置内容
     */
    private String configValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态:1:有效 0：无效
     */
    private Integer yn;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 修改人ID
     */
    private Long modifierId;

    /**
     * 修改人名称
     */
    private String modifierName;

    /**
     * 最近一次修改时间
     */
    private LocalDateTime lastModified;


}
