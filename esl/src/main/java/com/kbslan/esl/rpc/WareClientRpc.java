package com.kbslan.esl.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WareClientRpc {
	private static final Logger logger = LoggerFactory.getLogger(WareClientRpc.class);

	/**
	 * 查询门店下面的所属sku
	 *
	 * @param vendorId 商家id
	 * @param storeId  门店id
	 * @param skus     sku列表
	 * @return 门店下所属sku
	 */
	public StoreSkuListVO getStoreSkus(Long vendorId, Long storeId, List<Long> skus){
		return new StoreSkuListVO(vendorId, storeId, skus);
	}


}
