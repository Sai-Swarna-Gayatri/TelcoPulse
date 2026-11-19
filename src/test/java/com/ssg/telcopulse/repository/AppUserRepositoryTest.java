package com.ssg.telcopulse.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.ssg.telcopulse.config.JpaAuditingConfig;
import com.ssg.telcopulse.model.AppUser;
import com.ssg.telcopulse.model.UserRole;

@DataJpaTest 
@Import(JpaAuditingConfig.class)
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    //public boolean existsByUserName(String userName);
    @Test
    void shouldReturnTrue_whenUsernameExists()
    {
        AppUser user = new AppUser("Test UserA","testPassw0rd",UserRole.USER);
        appUserRepository.save(user);

        assertTrue(appUserRepository.existsByUserName(user.getUserName()));
    }

    @Test
    void shouldReturnFalse_whenUsernameDoesNotExist()
    {
        String userName="Test UserB";

        assertFalse(appUserRepository.existsByUserName(userName));
    }

}
