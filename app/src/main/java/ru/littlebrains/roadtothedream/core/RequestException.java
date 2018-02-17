package ru.littlebrains.roadtothedream.core;

public class RequestException extends Exception {

    //public MetaDataModel metaDataModel;
    public boolean isTimeoutException;
    public boolean isUnknownHostException;
    public int code;
    public int errorCode;


    public RequestException(String message) {
        super(message);
    }

    /*public RequestException(MetaDataModel metaDataModel, String message) {
        super(message);
        this.metaDataModel = metaDataModel;
    }
*/
    public RequestException(String message, int code, int errorCode) {
        super(message);
        this.code = code;
        this.errorCode = errorCode;
    }

    public RequestException(String message, boolean isUnknownHostException, boolean isTimeoutException) {
        super(message);
        this.isUnknownHostException = isUnknownHostException;
        this.isTimeoutException = isTimeoutException;
    }

}