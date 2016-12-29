package com.noddy.androidframework.errorHandle;
/**
 * Created by NoddyLaw on 2016/12/6.
 */

public enum CatchedRequestErrorType {

    /*server side response, reference by:"List of HTTP status codes" wiki */
    //1xx Informational
//    CONTINUE(100,10100),
//    SWITCHING_PROPOCOLS(101,1101),
//    PROCESSING(102,10102),
//    //300 Redirection
//    MULTIPLE_CHOICES(300,10300),
//    MOVED_PREMANENTLY(301,10301),
//    FOUND(302,10302),
//    SEE_OTHER(303,10303),
//    NOT_MODIFIED(304,10304),
//    USE_PROXY(305,10305),
//    SWITCH_PROXY(306,10306),
//    TEMPORARY_REDIRECT(307,10307),
//    PERMANENT_REDIRECT(308,10308),
//    //400 Client Error
//    BAD_REQUEST(400,10400),
//    UNAUTHORIZED(400,10401),
//    PQYMENT_REQUIRED(400,10402),
//    FORBIDDEN(400,10403),
//    NOT_FOUND(400,10404),
//    METHOD_NOT_ALLOWED(400,10405),
//    NOT_ACCEPTABLE(400,10406),
//    PROXY_AUTHENTICATION_REQUIRED(400,10407),
//    REQUEST_TIMEOUT(400,10408),
//    CONFLICT(400,10409),
//    GONE(400,10410),
//    LENGTH_REQUIRED(400,10411),
//    PRECONDITION_FAILED(400,10412),
//    PAYLOAD_TOO_LARGE(400,10413),
//    URL_TOO_LONG(400,10414),
//    UNSUPPORTED_MEDIA_TYPE(400,10415),
//    RANGE_NOT_SATISFIABLE(400,10416),
//    EXXPECTION_FAILED(400,10417),
//    IM_A_TEAPOT(400,10418),

    //500 Server Error
    SERVER_SIDE_ERROR_INFORMATIONAL(100),
    SERVER_SIDE_ERROR_REDIRECTION(300),
    SERVER_SIDE_ERROR_CLIENT_ERROR(400),
    SERVER_SIDE_ERROR_SERVER_ERROR(500),
    /*client side*/
    //airwatch
    NETWORK_NOT_CONNECT(10001),
    USER_NAME_NOT_FIND(10002),
    TOKEN_NOT_FIND(10003),
    UPLOAD_BLOB_TO_AZURE_FAIL(10004),
    UPLOAD_MESSAGE_FAIL(10005),
    ADLOGIN_FAIL(10006),
    GET_ICS_FAIL(10007),
    DOWNLOAD_ATTACHMENT_FAIL(10008),
    DOWNLOAD_SPECIFIC_ICS_FAIL(10009),
    CAN_NOT_GET_MORE(10010),
    ENTITY_HOLDER_CONVERTION_FAIL(10011),
    CODING_ERROR(99998),
    UNKNOW(99999);

    int errorCode;
    CatchedRequestErrorType(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode;
    }

    public static CatchedRequestErrorType getTypeByCode(int code){
        for(CatchedRequestErrorType errorType:CatchedRequestErrorType.values()){
            if(code>=errorType.getCode()&&code<(errorType.getCode()+99))
                return errorType;
        }
        return UNKNOW;
    }

    public String getDisplayErrorMsg() {
        String errorMsg ="";
        switch (this) {
            case UPLOAD_BLOB_TO_AZURE_FAIL:
                errorMsg = "因不明原因無法上載文件... \n請聯絡總行IT部 ";
                break;
            //client
            case DOWNLOAD_SPECIFIC_ICS_FAIL:
            case DOWNLOAD_ATTACHMENT_FAIL:
            case GET_ICS_FAIL:
            case UPLOAD_MESSAGE_FAIL:
            case ADLOGIN_FAIL:
            case TOKEN_NOT_FIND:
            case CODING_ERROR:
                errorMsg = "因不明原因無法獲取數據... \n請聯絡總行IT部 ";
                break;
            case NETWORK_NOT_CONNECT:
                errorMsg = "無法連線至網絡... \n請檢查網絡是否連線正常";
                break;

            case USER_NAME_NOT_FIND:
                errorMsg = "無法辨別使用者身份... \n請聯絡總行IT部";
                break;
            case  CAN_NOT_GET_MORE:
                errorMsg = "沒有更多資料";
                break;
            //server/api side
            case SERVER_SIDE_ERROR_INFORMATIONAL:
            case SERVER_SIDE_ERROR_REDIRECTION:
            case SERVER_SIDE_ERROR_CLIENT_ERROR:
            case SERVER_SIDE_ERROR_SERVER_ERROR:
                errorMsg = "伺服器維護或繁忙中... \n請稍後再試。";
                break;
            case UNKNOW:
            default:
                errorMsg = "伺服器維護或繁忙中... \n請稍後再試。";
                break;
        }

        return errorMsg +="\n("+getCode()+")";
    }
}
