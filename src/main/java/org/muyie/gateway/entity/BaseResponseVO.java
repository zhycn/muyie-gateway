package org.muyie.gateway.entity;

import java.io.Serializable;
import java.util.HashMap;

import org.muyie.gateway.errors.Errors;

public class BaseResponseVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String retCode;

  private String retMsg;

  private Object retData;

  public static BaseResponseVO ok() {
    return ok(Errors.SUCCESS);
  }

  public static BaseResponseVO ok(Object retData) {
    return ok(Errors.SUCCESS, retData);
  }

  public static BaseResponseVO ok(Errors errors) {
    return ok(errors.getCode(), errors.getMesssage(), new HashMap<>());
  }

  public static BaseResponseVO ok(Errors errors, Object retData) {
    return ok(errors.getCode(), errors.getMesssage(), retData);
  }

  public static BaseResponseVO ok(String retCode, String retMsg, Object retData) {
    BaseResponseVO vo = new BaseResponseVO();
    vo.setRetCode(retCode);
    vo.setRetMsg(retMsg);
    vo.setRetData(retData);
    return vo;
  }

  public String getRetCode() {
    return retCode;
  }

  public String getRetMsg() {
    return retMsg;
  }

  public Object getRetData() {
    return retData;
  }

  public void setRetCode(String retCode) {
    this.retCode = retCode;
  }

  public void setRetMsg(String retMsg) {
    this.retMsg = retMsg;
  }

  public void setRetData(Object retData) {
    this.retData = retData;
  }

}
