package com.kbslan.esl.vo.pricetag.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 更新价签异步返回结果
 * </p>
 * <p>
 *     {
 *     "status_no": 0,
 *     "type": "ESL_UPDATE_ACK",
 *     "user": "14259",
 *     "data": [
 *         {
 *             "sid": "14259-8937887",
 *             "sales_no": "",
 *             "status_no": 0,
 *             "esl_id": "44-E7-81-05",
 *             "ap_id": "256",
 *             "ap_mac": "98:6D:35:75:69:63",
 *             "retry": 0,
 *             "rf_power": 24,
 *             "last_hb_time": "1693892176563",
 *             "last_esl_ack": 64,
 *             "refresh_time": 1693892432304,
 *             "last_payload_type": "QUERY",
 *             "esl_image": "iVBORw0KGgoAAAANSUhEUgAAAPoAAAB6CAYAAACWXE7lAAAKiElEQVR42u2d646muA5Fv/d/6T7nz0ijHki27e0kwLKE1E1VBQhZvsUJvz+/3x8ODo53Hz86gYMD0Dk4OAB9wfGPuNv7p81K+3+35X7Ov+WUfu66n6tn7ejjzvEE6MXB5+rIGVAOOCptXQ3yv3/POTivZDfoXdfquO8HQH426Mqgd76YaPuqVKxCZuBHZNXgriiQU0CPjJsDrfzvUe66o/OUNiIwVV05pa1I+47nyygNh3I53aIr93eoK/97XEweGTBdA9MF+ex5MgqvArpTSZzmuncp+C7P89WgRyDPdmiX21qBsgrtrI+cltcBY+Ta0XPdnscI7IPj9d8xcEcHZzYrXXkx7sEzcvuybTs8oh0hVRbg3dnxzv5+BeiVuM01LaTEv+p0XPa8e5C8FfQd02Ar8xPMozclY7KDygF6xk3MKsZOl71rJuMU0JleeyHo3ZnoVTmJ6nOtCltWgt4ZRwP6gVNtTtBXZMi7QHcDdbrr7gR9FMqRjGuOg6oFOI7yTzfoLkX0ddC7n5XptcNcoxnoO6fXngK6s4LviaBnpvoAvQCDM/ar1KR3xugng35CMq46Y+MqbHIWT326YMZZoOEoVNlV+ZY9pyqMjqx7NiyqTGc6ypjdz/7pefRVJYaz6aadmVQ36NnfcSYNHWXLK8qAIwq/+9mpdTdVskXb6ly37HRVo2vru5RedrB3rCtYXTHH9Nqm+N25+KV7BVfEW5lV7bks045S4mpOxJVkcy9KAvTGVWaHLhO0wBKtoV8xcFcsLuqEvGvzEUDn4OAAdA4OjgbQEQR5vQA6gnwOdEdBP4IgDwBdnV5SQXftJDKbQ16xnDLSRocSPKG/Vm8miTSCnrHmo2mdu2uoSsXZTltPDvrEpYDe1F9dSs91vvNe7vq9UWn+woPh722YsoMvswf5qQM3UtAC6H194TrfPS6V/RLN7yVv0WeFGQrolSo198Dt/MqJos1PB33XPmtvBD3z/+UWfWSZ1Zg++2B3Wx27OyjjeTihjjz/DsW4OxFbfbaI1Y0q7ceBnt1CpxP0Va5oZCDf7Si74osngD4GNHrvn7DoEWu0A/SVMWfm+aJ9+OQYPds/uy26sqCmS+kcCfosa+wAfaZ9I7/vnl6rWveOGP2U/jqhniKbAKt6SRUFfATooz3UFEWw041zx79RwFZZ9Df0zynJuE+77og/WUYhyZq+np13Fm1V7yXr4QI6giCAjiAIoB/paldq7AkZEEAXY2rHlkzV/c8qW0kBO3IJ+l3FWWaa6CmDq3vTQFf5LKAjVoueLTa4+v/pA6wC507Qs/eDAHoadIdiOB3ylaA79kJXziOAPrTOmaqjEwtqur7Y4QQ9kmRT7xMB9OGqsCvQN6ynbQPdoQh2gR65TwTQyxb9KaBnE24dCqUbdNx3BNB/+c0znNeJQAnoyGNA3z3gVrmz1flzQEdaQb9bsRaZWvs66I5CmYzrnlEeyMdAz+z0Osu6P8F1P+kagI4ssegz66ysT38q6M4iE3edemWJJaAjQ9Bf+aQ/vdTX9XEDxz0COgLoDRBVYAV0BNAB3eZ5ADoC6EnQR7+zCvJMnoTpNQTQBdBVi7oadAVaQEcAPQl6FtpO0Kl1RwD9ANDdGfvI/QA6AuhJGF1ryR1WO6IEcNuRz4MeAXIH6COvgx1mEED/EOjuv0MAHdBfAjqCAPqGeD4LuzMHgQD6p0B3ZN2BCwF0QEcQQD8R9l3xOYIAejPolVp3QEcA/UGgu5JgCALoD4S9A3QUAwLoB8GeaQfQEUB/EOzZvwf03vd19X/6EdCPjLG/BHrms9Sj81QBAjryQqv9b7hVpYAEQFemlSK/G93s0LWHW3WAIfshVy09MgE9u01RRikoLy36d7Prqss97wYPg8jrso+KlTJKu8F1//2/vX+OjvP//llHO/egX4GhAKfAoALnBF29f0XRIT5LnYV5oad1BZnz/B2grnY00JXvoKsfXpwpg1EWdfTN9uigURTVzBohe0Dv2po7ALpLAcx+x9XO3HW/A1xRADPlsNKiRz+5VFEoSB70SPgX9dJMbnvF4t61MwP06rqZduaueySWVl5W5tPLsw87upNrXd9iA/L62JnlVRri807XfaQ8OtqZx+iK5Y2CXmm3GqNHMv1YdE8yLgpvNC5veDcrQL+y0q525q67mm2uflU1a/mrMToW/dlKYxZ6PSwZl43RZ+3MLfos+VEFfaZInAmX2UzCyAJh0dEtC6bX7iB2tDMH3WF5Ffc5k5DJfG9ccRtn9wToyNMVF12AIF8D3VWQ8uZYMfKz7KeZ3NdY+H7UBNKdSzqKR2dtzK6nTE9V57dH9za6l6sEm9JHsem1O/c6UjKaWaqputUnJYEieQt3bX5limlRPwL66aBHpr3UAR+Jn09ea6wk59TpvKgyURVx9wIhQH+pRZ/BFpniUP9+BvrOL4bO+uJuk8mKJVa8pkPqwwH9jaBfDWoF+Aro7pVtLvf9ztpmYFb7SJ212LBJA6CfDPpsEUnHJ3mjC1wy7nR3Ak5x3RWYK667qgwX5T0A/S0W/e5cNRn3VtCVGD1r0SN9BOiAPgW4ai0cMbqyhHE36IqCzMboEUsO6IAugx51C1dMrzk2Lei25hFll8kBjHIkmcpEQP9YjK5AvNlaLHXdOz5/XHXdZ/2+WhEC+kNj9KcJq8sOfCWA/gzXvWJtuqznk8ptd3s6h4LOfe+973FlXLZKKwNGtXBkVYighjN3fYQgOxTCMGmjxsOu7Z7Ugo9VVjID8w5l9HIPoXMb5ZkLPtq7beReO6+bfS7Noqs11IpVzuwyoyiAE5J+mY03kJBbqsb/ozh5FPcr7Y3c48x9Rq6bea570Ktxs2rBRpZPmctfvSlEFnT2iLeDroKrJtOi/6+CHnkOx3Ndu+7ZJZndVms3JJW96Nl7zua2Ryz0yBoqLnR2++bINs1TMBM7v+Ysuho3X1ndKMDKQpFTknHVj1YgZdf9DqI7CKIWXGlfcdEz11UVmapMahZ9NKBdO8FG3eNTQFeUFVIGXbG4VeAi13WBriidzAaRv/DAnlnbjEvevZuNE/S7cIJdZI9Jxq2K0d2gK3P4Xtd9VBd9l22P1lI/xaJX8gqzc0goTldj7mqMnrmuY3pNsdz16TUEQd6rMOkCBAF0BEHeAvpVbMDBwfHsA4uOILjuCIJY4DrIqgM6gnwF9Fkt8dX5zBwgUh84o/4fvb/RDip/n8/MOY8G+uxZru5fLX99Guh3P4+cjyiV22Tc3ct2rqSZtROt9f0q5HfbD0U1/mzxiGQxBlshzRRAZvupJ1r0u3dVPZ+y6OqFVPekYmVmv/cVzyGysCKzCEP9meI5qAo5uooM0A2gK506syjKy3aDjkUfbygYdd1Vi3/380gI53Ddvwy6amwvQZ/tYBkFL1qkn7H8gP5Hgq4jRKuEVNWFKU949yroszGvKoBwMk6FqzIQlNgM0DXQI8kqdamnI0aPbuP8ZdArQMugK4mPaPIkMnije20DuqZgs6BGwrFZEk5ZGTazcqoV/GqMngJ9lgRSNtRXX4aSXHvDNIsTdNV7yizJHP17FA8qFv2L0jW9FkmI/wd0BEH6Qd9VMPM/xBFmZirG5I8AAAAASUVORK5CYII=",
 *             "errmsg": ""
 *         }
 *     ]
 * }
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
@Getter
@Setter
@ToString
public class UpdatePriceTagResult {
    /**
     * 会话ID
     */
    private String sid;

    /**
     * 请求价签的id
     */
    @JSONField(name = "esl_id")
    private String eslId;
    /**
     * 能量值
     */
    @JSONField(name = "rf_power")
    private Integer rfPower;

    /**
     * 重试次数
     */
    private Integer retry;

    /**
     * 使用基站ID
     */
    @JSONField(name = "ap_id")
    private Integer apId;

    /**
     * 基站mac
     */
    @JSONField(name = "ap_mac")
    private String apMac;

    /**
     * base64 后 的 价 签 渲 染 图 ， 仅 在 ESL_UPDATE_ACK 中
     * errno=0时返回此值
     */
    @JSONField(name = "esl_image",serialize = false)
    private String eslImage;

    /**
     * 此价签在此基站上的最后一次心跳时间
     */
    @JSONField(name = "last_hb_time")
    private String lastHbTime;


    /**
     * 通信ack
     */
    @JSONField(name = "last_esl_ack")
    private Integer lastEslAck;

    /**
     * 最后一次通信任务
     */
    @JSONField(name = "last_payload_type")
    private String lastPayloadType;

    /**
     * 此价签绑定的salesNo, 没有时返回空串
     */
    @JSONField(name = "sales_no")
    private String salesNo;


    /**
     * 错误代码
     */
    @JSONField(name = "status_no")
    private String statusNo;


    /**
     * 错误信息。没有错误时返回空字符串
     */
    @JSONField(name = "errmsg")
    private String errMsg;

    /**
     * 刷新时间
     */
    @JSONField(name = "refresh_time")
    private String refreshTime;

}
