package com.sharmachait.wazir.Repository;

import com.sharmachait.wazir.Model.Entity.USER_ROLE;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
    @Autowired
    private IUserRepository userRepository;
    public void UserRepository_SaveAll_ReturnsSavedUser() {
        //arrange
        WazirUser user = WazirUser.builder()
                .role(USER_ROLE.ROLE_CUSTOMER)
                .email("Chait8126@gmail.com")
                .mobile("8126056659")
                .fullName("Chaitanya Sharma")
                .build();
        //act
        WazirUser savedUser = userRepository.save(user);
        //assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0L);
    }
}
