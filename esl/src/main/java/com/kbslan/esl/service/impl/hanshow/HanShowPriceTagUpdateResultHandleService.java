package com.kbslan.esl.service.impl.hanshow;


import com.kbslan.esl.vo.hanshow.HanShowResult;
import com.kbslan.esl.vo.hanshow.UpdatePriceTagResult;

import java.util.List;

/**
 * <p>
 * 汉朔基站状态变更处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
public interface HanShowPriceTagUpdateResultHandleService {
    void handle(HanShowResult<List<UpdatePriceTagResult>> result);


    interface PriceTagUpdateResultHandleListener {

        boolean matches(HanShowResult<List<UpdatePriceTagResult>> result);

        void onHandle(HanShowResult<List<UpdatePriceTagResult>> result);
    }

}
