package com.kbslan.domain.model;


import com.kbslan.domain.entity.DPriceTagListEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;


@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class PriceTagListVO extends DPriceTagListEntity {

	private static final long serialVersionUID = 1L;

	public PriceTagListVO(DPriceTagListEntity e) {
        BeanUtils.copyProperties(e, this);
    }

}
