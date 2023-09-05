package com.kbslan.esl.service.impl.pricetag.hanshow;

import com.kbslan.esl.service.pricetag.hanshow.HanShowPriceTagUpdateResultHandleService;
import com.kbslan.esl.service.pricetag.model.hanshow.HanShowResult;
import com.kbslan.esl.service.pricetag.model.hanshow.UpdatePriceTagResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 汉朔价签回调处理
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
@Service
public class HanShowPriceTagUpdateResultHandleServiceImpl implements HanShowPriceTagUpdateResultHandleService {
    public static final Logger logger = LoggerFactory.getLogger(HanShowPriceTagUpdateResultHandleServiceImpl.class);

    private final List<PriceTagUpdateResultHandleListener> priceTagUpdateResultHandleListeners;

    public HanShowPriceTagUpdateResultHandleServiceImpl(ObjectProvider<List<PriceTagUpdateResultHandleListener>> provider) {
        this.priceTagUpdateResultHandleListeners = provider.getIfAvailable();
    }

    @Override
    public void handle(HanShowResult<List<UpdatePriceTagResult>> result) {

        try {
            for (PriceTagUpdateResultHandleListener listener : priceTagUpdateResultHandleListeners) {
                try {
                    if (listener.matches(result)) {
                        listener.onHandle(result);
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            //
        }


    }
}

