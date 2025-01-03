package com.dev.library.core;

public class AppConstant {
    public static class UserAccountRegex {
        public static final String USERNAME = "^[a-zA-Z0-9._-]{3,}$";
        public static final String PHONE_NUMBER = "([\\+84|84|0]+(3|5|7|8|9|1[2|6|8|9]))+([0-9]{8})";
        public static final String EMAIL = "^[a-zA-Z][a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]*(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+\\.(com|net|org|edu|gov|info|io|xyz|vn)$";
    }
}
