package com.psq.backend.member.util;

import com.psq.backend.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PasswordUtilTest {
    
    @Test
    @DisplayName("유니크한 salt 값을 생성한다")
    public void generateSaltTest() throws Exception {
        // when
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();
        
        // then
        assertThat(salt1).isNotEqualTo(salt2);
    }
    
    @Test
    @DisplayName("비밀번호를 암호화한다")
    public void encodedPasswordTest() throws Exception {
        // given
        String password = "password";
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();

        // when
        String actual1 = PasswordUtil.encodePassword(password, salt1);
        String actual2 = PasswordUtil.encodePassword(password, salt1);

        // then
        assertThat(actual1).isEqualTo(actual2);
    }

    @Test
    @DisplayName("비밀번호를 암호화 시 salt 값이 다르면 비밀번호가 달라진다")
    public void encodedPasswordOtherSaltTest() throws Exception {
        // given
        String password = "password";
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();

        // when
        String actual1 = PasswordUtil.encodePassword(password, salt1);
        String actual2 = PasswordUtil.encodePassword(password, salt2);

        // then
        assertThat(actual1).isNotEqualTo(actual2);
    }
}
