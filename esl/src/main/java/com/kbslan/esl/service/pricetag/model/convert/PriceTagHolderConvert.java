package com.kbslan.esl.service.pricetag.model.convert;

import com.kbslan.domain.model.PriceTagListVO;
import com.kbslan.esl.service.pricetag.model.data.PriceTagHolder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 *
 * <a href="https://duodian.feishu.cn/docx/VZ0Hd6t6No5kvQxEJbhc6HBWnZb">
 *     电子价签推送数据源转换
 * </a>
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/11 14:36
 */
@Component
public class PriceTagHolderConvert implements Function<PriceTagListVO, PriceTagHolder> {
    @Override
    public PriceTagHolder apply(PriceTagListVO priceTagListVO) {
        return null;
    }
}
