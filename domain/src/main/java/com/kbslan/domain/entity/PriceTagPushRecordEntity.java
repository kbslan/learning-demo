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
 * 电子价签推送记录
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("price_tag_push_record")
public class PriceTagPushRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家ID
     */
    private Long vendorId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 批次号
     */
    private String sid;

    /**
     * 推送模式
     * @see com.kbslan.domain.enums.PushModeEnum
     */
    private Integer pushMode;

    /**
     * 推送类型
     * @see com.kbslan.domain.enums.PushTypeEnum
     */
    private Integer pushType;

    /**
     * 触发类型 1：自动， 2：手动，3：补偿
     * @see com.kbslan.domain.enums.RefreshTypeEnum
     */
    private Integer refreshType;

    /**
     * 状态 0：待下发，1：下发中，2：下发成功，3：下发失败，4：补发成功
     * @see com.kbslan.domain.enums.PushStatusEnum
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String errorMsg;

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
     * 更新人ID
     */
    private Long modifierId;

    /**
     * 更新人名称
     */
    private String modifierName;

    /**
     * 更新时间
     */
    private LocalDateTime modified;


}
