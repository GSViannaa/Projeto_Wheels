package com.ProjetoWheels.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacaoService
{
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean isValidEmail(String email)
    {
        if (email == null) return false;

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
