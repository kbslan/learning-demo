package com.kbslan.esl.rpc;

import java.io.Serializable;
import java.util.List;

/**
 * 门店sku信息表
 *
 */
public class StoreSkuListVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long vendorId;

	private Long storeId;
	
	private List<Long> skus;

	public StoreSkuListVO(Long vendorId, Long storeId, List<Long> skus) {
		this.vendorId = vendorId;
		this.storeId = storeId;
		this.skus = skus;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public List<Long> getSkus() {
		return skus;
	}

	public void setSkus(List<Long> skus) {
		this.skus = skus;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
}
