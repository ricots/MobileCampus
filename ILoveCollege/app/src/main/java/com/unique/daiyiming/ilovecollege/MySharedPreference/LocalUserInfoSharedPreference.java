package com.unique.daiyiming.ilovecollege.MySharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daiyiming on 2015/6/14.
 */
public class LocalUserInfoSharedPreference {

    private final static String USER_INFO_SHARED_PREFERENCE_NAME = "user_info_shared_preference_name";

    private final static String SHARED_PREFERENCE_KEY_USERNAME = "username"; //用户名
    private final static String SHARED_PREFERENCE_KEY_NICKNAME = "nickname"; //昵称
    private final static String SHARED_PREFERENCE_KEY_SIGN = "sign"; //个性签名
    private final static String SHARED_PREFERENCE_KEY_STUDENT_ID = "student_id"; //学号
    private final static String SHARED_PREFERENCE_KEY_GENDER = "gender"; //性别
    private final static String SHARED_PREFERENCE_KEY_AVATAR = "avatar"; //头像地址
    private final static String SHARED_PREFERENCE_KEY_HOMETOWN = "hometown"; //家乡
    private final static String SHARED_PREFERENCE_KEY_BIRTHDAY = "birthday"; //生日
    private final static String SHARED_PREFERENCE_KEY_SEX_ORIENTATION = "sex_orientation"; //性取向
    private final static String SHARED_PREFERENCE_KEY_LOVE_STATUS = "love_status"; //恋爱状态
    private final static String SHARED_PREFERENCE_KEY_HOBBIES = "hobbies"; //兴趣
    private final static String SHARED_PREFERENCE_KEY_SCHOOL = "school"; //学校
    private final static String SHARED_PREFERENCE_KEY_MAJOR = "major"; //专业
    private final static String SHARED_PREFERENCE_KEY_GRADE = "grade"; //年级
    private final static String SHARED_PREFERENCE_KEY_TOKEN = "token"; //token

    //控制变量
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    public LocalUserInfoSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_INFO_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //---------------------------获取-------------------------------------

    public String getUsername() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_USERNAME, "");
    }

    public String getNickname() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_NICKNAME, "");
    }

    public String getSign() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_SIGN, "");
    }

    public String getStudentId() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_STUDENT_ID, "");
    }

    public String getGender() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_GENDER, "");
    }

    public String getAvatar() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_AVATAR, "");
    }

    public String getHometown() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_HOMETOWN, "");
    }

    public String getBirthday() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_BIRTHDAY, "");
    }

    public String getSexOrientation() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_SEX_ORIENTATION, "");
    }

    public String getLoveStatus() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_LOVE_STATUS, "");
    }

    public String getHobbies() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_HOBBIES, "");
    }

    public String getSchool() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_SCHOOL, "");
    }

    public String getMajor() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_MAJOR, "");
    }

    public String getGrade() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_GRADE, "");
    }

    public String getToken() {
        return sharedPreferences.getString(SHARED_PREFERENCE_KEY_TOKEN, "");
    }

    //---------------------------添加-------------------------------------

    public void putAll(String username, String nickname, String sign, String studentId,
                       String gender, String avatar, String hometown, String birthday,
                       String sexOrientation, String loveStatus, String hobbies, String school,
                       String major, String grade, String token) {
        putUsername(username);
        putNickname(nickname);
        putSign(sign);
        putStudentId(studentId);
        putGender(gender);
        putAvatar(avatar);
        putHometown(hometown);
        putBirthday(birthday);
        putSexOrientation(sexOrientation);
        putLoveStatus(loveStatus);
        putHobbies(hobbies);
        putSchool(school);
        putMajor(major);
        putGrade(grade);
        putToken(token);
    }

    public void putUsername(String username) {
        editor.putString(SHARED_PREFERENCE_KEY_USERNAME, username);
    }

    public void putNickname(String nickname) {
        editor.putString(SHARED_PREFERENCE_KEY_NICKNAME, nickname);
    }

    public void putSign(String sign) {
        editor.putString(SHARED_PREFERENCE_KEY_SIGN, sign);
    }

    public void putStudentId(String studentId) {
        editor.putString(SHARED_PREFERENCE_KEY_STUDENT_ID, studentId);
    }

    public void putGender(String gender) {
        editor.putString(SHARED_PREFERENCE_KEY_GENDER, gender);
    }

    public void putAvatar(String avatar) {
        editor.putString(SHARED_PREFERENCE_KEY_AVATAR, avatar);
    }

    public void putHometown(String hometown) {
        editor.putString(SHARED_PREFERENCE_KEY_HOMETOWN, hometown);
    }

    public void putBirthday(String birthday) {
        editor.putString(SHARED_PREFERENCE_KEY_BIRTHDAY, birthday);
    }

    public void putSexOrientation(String sexOrientation) {
        editor.putString(SHARED_PREFERENCE_KEY_SEX_ORIENTATION, sexOrientation);
    }

    public void putLoveStatus(String loveStatus) {
        editor.putString(SHARED_PREFERENCE_KEY_LOVE_STATUS, loveStatus);
    }

    public void putHobbies(String hobbies) {
        editor.putString(SHARED_PREFERENCE_KEY_HOBBIES, hobbies);
    }

    public void putSchool(String school) {
        editor.putString(SHARED_PREFERENCE_KEY_SCHOOL, school);
    }

    public void putMajor(String major) {
        editor.putString(SHARED_PREFERENCE_KEY_MAJOR, major);
    }

    public void putGrade(String grade) {
        editor.putString(SHARED_PREFERENCE_KEY_GRADE, grade);
    }

    public void putToken(String token) {
        editor.putString(SHARED_PREFERENCE_KEY_TOKEN, token);
    }

    public void commit() {
        editor.commit();
    }

    public boolean isEnable() {
        if (getToken().equals("")) {
            return false;
        } else {
            return true;
        }
    }
}






















