package com.kbslan.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kbslan.domain.entity.PriceTagPushRecordEntity;
import com.kbslan.domain.enums.PushStatusEnum;

/**
 * <p>
 * 电子价签推送记录 服务类
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-22
 */
public interface PriceTagPushRecordService extends IService<PriceTagPushRecordEntity> {

    /**
     * 推送记录状态变更
     *
     * @param storeId 门店ID
     * @param sid     推送批次号
     * @param status  状态
     * @param errMsg  错误信息
     */
    void changePushRecordStatus(Long storeId, String sid, PushStatusEnum status, String errMsg);
}
