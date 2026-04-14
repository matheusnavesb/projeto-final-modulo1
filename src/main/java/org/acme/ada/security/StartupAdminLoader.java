package org.acme.ada.security;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.ada.model.User;
import org.acme.ada.repository.UserRepository;

@Startup
@ApplicationScoped
public class StartupAdminLoader {

    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (userRepository.findByEmail("admin").isEmpty()) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin");
            admin.setPassword("admin");
            admin.setRole("ADMIN");
            userRepository.persist(admin);
        }
    }

//    @PostConstruct
//    @Transactional
//    void init() {
//        if (userRepository.findByEmail("admin").isEmpty()) {
//            User admin = new User();
//            admin.setName("admin");
//            admin.setEmail("admin");
//            admin.setPassword("admin");
//            admin.setRole("ADMIN");
//
//            userRepository.persist(admin);
//        }
//    }
}
