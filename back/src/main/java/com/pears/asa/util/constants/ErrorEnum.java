package com.pears.asa.util.constants;

import com.pears.asa.util.CommonUtil;

/**
 * @author: pears
 * @date: 2017/10/24 10:16
 */
public enum ErrorEnum {
    /*
     * 错误信息
     * */
    E_400("400", CommonUtil.getI18NMessage("validation.requestDisposalException",null)),
    E_500("500", CommonUtil.getI18NMessage("validation.requestMethodException",null)),
    E_501("501", CommonUtil.getI18NMessage("validation.requestPathNotExist",null)),
    E_502("502", CommonUtil.getI18NMessage("validation.noAuth",null)),
    E_10008("10008", CommonUtil.getI18NMessage("validation.failWhenDelRole",null)),
    E_10009("10009", CommonUtil.getI18NMessage("validation.accountHasExist",null)),

    E_20011("20011", CommonUtil.getI18NMessage("validation.sessionInvalid",null)),

    E_90003("90003", CommonUtil.getI18NMessage("validation.missingParams",null)),

    E_00001("00001", CommonUtil.getI18NMessage("validation.emailExist",null)),

    E_00002("00002", CommonUtil.getI18NMessage("validation.userNameExist",null)),

    E_00003("00003", CommonUtil.getI18NMessage("validation.failSendEmail",null)),

    E_10004("00004", CommonUtil.getI18NMessage("validation.selectedDateHasCourse",null)),

    E_10005("00005", CommonUtil.getI18NMessage("validation.hasStudentSelected",null)),

    E_10006("00006", ""),

    E_10007("00007", CommonUtil.getI18NMessage("validation.studentNotSelectSameDate",null)),
    E_1000A("0000A", CommonUtil.getI18NMessage("validation.fileIsBig",null));
    private String errorCode;

    private String errorMsg;

    ErrorEnum() {
    }

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
