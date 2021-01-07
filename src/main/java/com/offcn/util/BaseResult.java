package com.offcn.util;

import com.offcn.bean.Employee;

public class BaseResult {
    private boolean success;
    private String message;

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
