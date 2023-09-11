package com.kbslan.esl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbslan.domain.entity.DPriceTagListEntity;
import com.kbslan.domain.entity.TemplateLayoutEntity;
import com.kbslan.domain.mapper.DPriceTagListMapper;
import com.kbslan.domain.model.PriceTagListVO;
import com.kbslan.esl.service.DPriceTagListService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/11 13:43
 */
public class DPriceTagListServiceImpl extends ServiceImpl<DPriceTagListMapper, DPriceTagListEntity> implements DPriceTagListService {
    @Override
    public List<PriceTagListVO> toVOList(List<DPriceTagListEntity> records, long vendorId, long storeId, boolean exportXls, boolean hitTemp, boolean printAll, TemplateLayoutEntity templateLayout, boolean syncWait) {
        return Collections.emptyList();
    }
}
