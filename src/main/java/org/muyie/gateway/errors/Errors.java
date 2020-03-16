package org.muyie.gateway.errors;

public enum Errors {

  SUCCESS("000000", "成功"),

  BAD_REQUEST("000400", "请求数据错误"),

  UNAUTHORIZED("000401", "未授权"),

  FORBIDDEN("000403", "没有访问权限"),

  NOT_FOUND("000404", "请求资源不存在"),

  REQUEST_TIMEOUT("000408", "请求超时"),

  INTERNAL_SERVER_ERROR("000500", "服务出错，请重试！")

  ;

  private String code;

  private String messsage;

  private Errors(String code, String messsage) {
    this.code = code;
    this.messsage = messsage;
  }

  public String getCode() {
    return code;
  }

  public String getMesssage() {
    return messsage;
  }

}
