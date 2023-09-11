package com.kbslan.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbslan.domain.entity.PriceTagPushRecordEntity;
import com.kbslan.domain.enums.PushStatusEnum;
import com.kbslan.domain.mapper.PriceTagPushRecordMapper;
import com.kbslan.domain.service.PriceTagPushRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 电子价签推送记录 服务实现类
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-22
 */
@Service
public class PriceTagPushRecordServiceImpl extends ServiceImpl<PriceTagPushRecordMapper, PriceTagPushRecordEntity> implements PriceTagPushRecordService {


    @Override
    public void changePushRecordStatus(Long storeId, String sid, PushStatusEnum status, String errMsg) {
        PriceTagPushRecordEntity update = new PriceTagPushRecordEntity();
        update.setStatus(status.getCode());
        update.setModified(LocalDateTime.now());
        update.setErrorMsg(errMsg);
        LambdaUpdateWrapper<PriceTagPushRecordEntity> updateWrapper = Wrappers.<PriceTagPushRecordEntity>lambdaUpdate()
                .eq(PriceTagPushRecordEntity::getStoreId, storeId)
                .eq(PriceTagPushRecordEntity::getSid, sid);
        this.update(update, updateWrapper);
    }

}
