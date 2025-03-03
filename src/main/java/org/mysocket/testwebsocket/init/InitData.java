package org.mysocket.testwebsocket.init;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.mysocket.testwebsocket.domain.user.entity.User;
import org.mysocket.testwebsocket.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final InitMemberService initMemberService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initMemberService.init();
        }
    }

    @Component
    @RequiredArgsConstructor
    static class InitMemberService {

        @PersistenceContext
        EntityManager em;

        private final BCryptPasswordEncoder bCryptPasswordEncoder;


        @Transactional
        public void init() {
            // 회원 한명
            User testUser = User.builder()
                    .email("test")
                    .password(bCryptPasswordEncoder.encode("1234"))
                    .role("ROLE_USER")
                    .build();

            em.persist(testUser);

            // 관리자
            User admin = User.builder()
                    .email("admin")
                    .password(bCryptPasswordEncoder.encode("1234"))
                    .role("ROLE_ADMIN")
                    .build();

            em.persist(admin);

        }
    }
}
