package com.kbslan.esl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kbslan.domain.entity.DPriceTagListEntity;
import com.kbslan.domain.entity.TemplateLayoutEntity;
import com.kbslan.domain.model.PriceTagListVO;

import java.util.List;

/**
 * <p>
 * 价签记录列表表  服务类
 * </p>
 *
 * @since 2021-03-04
 */
public interface DPriceTagListService extends IService<DPriceTagListEntity> {


	/**
	 * 转换VO （不包含多币种转换）
	 *
	 * @param records        价签信息列表
	 * @param vendorId       商家id
	 * @param storeId        门店id
	 * @param exportXls      是否为导出xls
	 * @param hitTemp        是否查询命中模板
	 * @param printAll       是否为打印全部
	 * @param templateLayout 模板布局信息
	 * @param syncWait       是否同步等待所有返回结果
	 * @return 价签VO列表
	 */
	List<PriceTagListVO> toVOList(List<DPriceTagListEntity> records, long vendorId,
								  long storeId, boolean exportXls, boolean hitTemp,
								  boolean printAll, TemplateLayoutEntity templateLayout,
								  boolean syncWait);

}
