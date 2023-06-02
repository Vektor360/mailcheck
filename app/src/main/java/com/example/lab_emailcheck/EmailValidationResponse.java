package com.example.lab_emailcheck;

public class EmailValidationResponse {
    private String email;
    private boolean formatValid;
    private boolean mxFound;
    private boolean smtpCheck;
    private boolean catchAll;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFormatValid() {
        return formatValid;
    }

    public void setFormatValid(boolean formatValid) {
        this.formatValid = formatValid;
    }

    public boolean isMxFound() {
        return mxFound;
    }

    public void setMxFound(boolean mxFound) {
        this.mxFound = mxFound;
    }

    public boolean isSmtpCheck() {
        return smtpCheck;
    }

    public void setSmtpCheck(boolean smtpCheck) {
        this.smtpCheck = smtpCheck;
    }

    public boolean isCatchAll() {
        return catchAll;
    }

    public void setCatchAll(boolean catchAll) {
        this.catchAll = catchAll;
    }

    // Другие геттеры и сеттеры для остальных полей

    // ...

}


