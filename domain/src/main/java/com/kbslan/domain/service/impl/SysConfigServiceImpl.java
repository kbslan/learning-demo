package com.kbslan.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kbslan.domain.entity.SysConfigEntity;
import com.kbslan.domain.mapper.SysConfigMapper;
import com.kbslan.domain.service.SysConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统相关配置表 服务实现类
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-22
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements SysConfigService {

}
