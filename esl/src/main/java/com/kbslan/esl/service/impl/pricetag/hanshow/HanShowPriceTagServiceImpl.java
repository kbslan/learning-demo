package com.kbslan.esl.service.impl.pricetag.hanshow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.*;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.enums.PushStatusEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.model.PriceTagListVO;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.domain.service.PriceTagPushRecordDetailService;
import com.kbslan.domain.service.PriceTagPushRecordService;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.service.DPriceTagListService;
import com.kbslan.esl.service.pricetag.PriceTagService;
import com.kbslan.esl.service.pricetag.hanshow.HanShowApiService;
import com.kbslan.esl.service.pricetag.hanshow.HanShowPriceTagUpdateResultHandleService;
import com.kbslan.esl.service.pricetag.model.PriceTagParams;
import com.kbslan.esl.service.pricetag.model.PriceTagRefreshParams;
import com.kbslan.esl.service.pricetag.model.convert.PriceTagHolderConvert;
import com.kbslan.esl.service.pricetag.model.data.PriceTagHolder;
import com.kbslan.esl.service.pricetag.model.data.Ware;
import com.kbslan.esl.service.pricetag.model.hanshow.HanShowResult;
import com.kbslan.esl.service.pricetag.model.hanshow.PassivePriceTagHeartbeat;
import com.kbslan.esl.service.pricetag.model.hanshow.UpdatePriceTagResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 汉朔价签处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 23:06
 */
@Slf4j
@Service
public class HanShowPriceTagServiceImpl implements PriceTagService {
    private final static String SUCCESS = "0";
    @Resource
    private HanShowApiService hanShowApiService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private PriceTagInfoService priceTagInfoService;
    @Resource
    private HanShowPriceTagUpdateResultHandleService hanShowPriceTagUpdateResultHandleService;
    @Resource
    private DPriceTagListService dPriceTagListService;
    @Resource
    private PriceTagHolderConvert priceTagHolderConvert;
    @Resource
    private PriceTagPushRecordService priceTagPushRecordService;
    @Resource
    private PriceTagPushRecordDetailService priceTagPushRecordDetailService;

    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public boolean bind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.bindPriceTag(params, deviceEslApiModel);
    }

    @Override
    public boolean unbind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.unbindPriceTag(params, deviceEslApiModel);
    }

    @Override
    public boolean refresh(PriceTagRefreshParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        //生成推送记录
        PriceTagPushRecordEntity pushRecord = createPushRecord(params);
        boolean save = priceTagPushRecordService.save(pushRecord);

        //TODO 数据组装
        List<DPriceTagListEntity> records = new ArrayList<>();
        TemplateLayoutEntity templateLayout = new TemplateLayoutEntity();
        List<PriceTagListVO> priceTagListVOList = dPriceTagListService.toVOList(records, params.getVendorId(),
                params.getStoreId(), false, true, false, templateLayout, false);
        if (CollectionUtils.isEmpty(priceTagListVOList)) {
            priceTagPushRecordService.changePushRecordStatus(params.getStoreId(), params.getSid(), PushStatusEnum.FAILED, "构建商品信息为空");
            return false;
        }
        //数据转换
        List<PriceTagHolder> priceTagHolderList = priceTagListVOList.stream()
                .map(priceTagHolderConvert)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(priceTagHolderList)) {
            priceTagPushRecordService.changePushRecordStatus(params.getStoreId(), params.getSid(), PushStatusEnum.FAILED, "商品数据转换后为空");
            return false;
        }
        //推送商品明细
        List<PriceTagPushRecordDetailEntity> pushRecordDetailList = priceTagHolderList.stream()
                .map(priceTagHolder -> createPushRecordDetail(params, priceTagHolder))
                .collect(Collectors.toList());

        priceTagPushRecordDetailService.saveBatch(pushRecordDetailList);

        try {
            //调用厂商API数据推送
            boolean pushResult = hanShowApiService.refresh(params, priceTagHolderList, deviceEslApiModel);
            if (pushResult) {
                priceTagPushRecordService.changePushRecordStatus(params.getStoreId(), params.getSid(), PushStatusEnum.PUSHING, "");
            } else {
                priceTagPushRecordService.changePushRecordStatus(params.getStoreId(), params.getSid(), PushStatusEnum.FAILED, "调用厂商推送接口失败");
            }
            return pushResult;
        } catch (Exception e) {
            priceTagPushRecordService.changePushRecordStatus(params.getStoreId(), params.getSid(), PushStatusEnum.FAILED, e.getMessage());
            throw e;
        }
    }

    private PriceTagPushRecordDetailEntity createPushRecordDetail(PriceTagRefreshParams params, PriceTagHolder priceTagHolder) {
        PriceTagPushRecordDetailEntity pushRecordDetail = new PriceTagPushRecordDetailEntity();
        pushRecordDetail.setSid(params.getSid());
        pushRecordDetail.setVendorId(params.getVendorId());
        pushRecordDetail.setStoreId(params.getStoreId());
        Ware ware = priceTagHolder.getWare();
        pushRecordDetail.setSkuId(ware.getSku());
        pushRecordDetail.setMatnr(ware.getMatnr());
        pushRecordDetail.setBarCode(ware.getItemNum());
        pushRecordDetail.setTitle(ware.getTitle());
        pushRecordDetail.setBrand(ware.getBrandName());
        pushRecordDetail.setRetailPrice(ware.getRetailPrice());
        pushRecordDetail.setProPrice(ware.getPromoPrice());
        pushRecordDetail.setProTag(ware.getPromoTag());
//            pushRecordDetail.setProSlogan(ware.getPromoSlogan());
//            pushRecordDetail.setRangInd();
        pushRecordDetail.setSalesFlag(Integer.valueOf(ware.getMstae()));
        pushRecordDetail.setItemStatus(Integer.valueOf(ware.getMmsta()));
        pushRecordDetail.setExtJson(JSON.toJSONString(priceTagHolder));
        pushRecordDetail.setCreatorId(params.getUserId());
        pushRecordDetail.setCreatorName(params.getUserName());
        pushRecordDetail.setCreated(LocalDateTime.now());
        pushRecordDetail.setModifierId(params.getUserId());
        pushRecordDetail.setModifierName(params.getUserName());
        pushRecordDetail.setModified(LocalDateTime.now());
        return pushRecordDetail;
    }

    private PriceTagPushRecordEntity createPushRecord(PriceTagRefreshParams params) {
        PriceTagPushRecordEntity pushRecord = new PriceTagPushRecordEntity();
        pushRecord.setVendorId(params.getVendorId());
        pushRecord.setStoreId(params.getStoreId());
        pushRecord.setSid(params.getSid());
        pushRecord.setPushMode(params.getPushMode().getCode());
        pushRecord.setPushType(params.getPushType().getCode());
        pushRecord.setRefreshType(params.getRefreshType().getCode());
        pushRecord.setStatus(PushStatusEnum.INIT.getCode());
        pushRecord.setCreatorId(params.getUserId());
        pushRecord.setCreatorName(params.getUserName());
        pushRecord.setCreated(LocalDateTime.now());
        pushRecord.setModifierId(params.getUserId());
        pushRecord.setModifierName(params.getUserName());
        pushRecord.setModified(LocalDateTime.now());
        return pushRecord;
    }

    @Override
    public void heartbeat(String json) throws Exception {
        log.info("汉朔价签心跳 json={}", json);
        HanShowResult<List<PassivePriceTagHeartbeat>> passivePriceTagHeartbeatHslResult = JSON.parseObject(json, new TypeReference<HanShowResult<List<PassivePriceTagHeartbeat>>>() {
        });
        List<PassivePriceTagHeartbeat> data = passivePriceTagHeartbeatHslResult.getData();

        for (PassivePriceTagHeartbeat heartbeat : data) {
            String eslId = heartbeat.getEslId();
            try {
                if (StringUtils.isNotEmpty(eslId)) {
                    PriceTagInfoEntity update = new PriceTagInfoEntity();
                    update.setLastHeartbeat(LocalDateTime.now());

                    LambdaQueryWrapper<PriceTagInfoEntity> queryWrapper = Wrappers.<PriceTagInfoEntity>lambdaQuery()
                            .eq(PriceTagInfoEntity::getPriceTagId, eslId.trim())
                            .eq(PriceTagInfoEntity::getStoreId, Long.parseLong(heartbeat.getUser()));

                    priceTagInfoService.update(update, queryWrapper);
                }

            } catch (Exception ex) {
                log.error("{} eslId={}", ex.getMessage(), eslId, ex);
            }
        }

    }

    @Override
    public void callback(String json) throws Exception {
        log.info("汉朔价签回调 json={}", json);
        final HanShowResult<List<UpdatePriceTagResult>> updatePriceTagResultHslResult = JSONObject.parseObject(json, new TypeReference<HanShowResult<List<UpdatePriceTagResult>>>() {
        });
        // 价签更新结果处理
        hanShowPriceTagUpdateResultHandleService.handle(updatePriceTagResultHslResult);

        if (HanShowResult.UNBIND_STATUS.equals(updatePriceTagResultHslResult.getType())) {
            // 价签解绑结果处理
            log.info("价签解绑结果处理");
        }

        //只处理ESL_UPDATE_ACK情况，代表最终结果的
        try {

            List<UpdatePriceTagResult> list = updatePriceTagResultHslResult.getData();
            log.info("价签更新回调：【{}】", JSON.toJSONString(list));
            for (UpdatePriceTagResult updatePriceTagResult : list) {
                String sid = updatePriceTagResult.getSid();
                if (StringUtils.isBlank(sid)) {
                    continue;
                }

                try {
                    if (StringUtils.isNotBlank(updatePriceTagResult.getEslImage())) {
                        byte[] bytes = Base64.getDecoder().decode(updatePriceTagResult.getEslImage());
//                        String resourceUrl = dcgService.uploadWithResourceUrl("image/png", hslSid + ".png", bytes);
                        //图片上传
                        String resourceUrl = "";
                        log.info("上传图片成功:sid={},url={}", sid, resourceUrl);
                        updatePriceTagResult.setEslImage(resourceUrl);
                    }
                } catch (Exception e) {
                    log.error("上传图片异常:sid={}", sid, e);
                }

                String storeStr = sid.split("-")[0];
                Long storeId = Long.valueOf(storeStr);
                String statusNo = updatePriceTagResult.getStatusNo();
                if (SUCCESS.equals(statusNo)) {
                    //成功逻辑
                    priceTagPushRecordService.changePushRecordStatus(storeId, sid, PushStatusEnum.SUCCESS, "");
                } else {
                    //异常逻辑
                    log.warn("in hsl err" + statusNo + "," + JSONObject.toJSONString(updatePriceTagResult));
                    priceTagPushRecordService.changePushRecordStatus(storeId, sid, PushStatusEnum.FAILED, updatePriceTagResult.getErrMsg());
                }
            }
        } catch (Exception e) {
            log.error("价签回调处理异常:{}", e.getMessage(), e);
        }
    }
}
