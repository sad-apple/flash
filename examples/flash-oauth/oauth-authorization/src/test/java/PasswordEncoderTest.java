import com.authorization.AuthApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zsp
 * @date 2023/7/19 16:39
 */
@SpringBootTest(classes = AuthApplication.class)
@Slf4j
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testBCryptEncoder() {
        String encode = passwordEncoder.encode("123");
        System.out.println(encode);
    }

}
