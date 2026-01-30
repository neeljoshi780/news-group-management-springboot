package com.newsgroupmanagement.seeder;

import com.newsgroupmanagement.enums.AuthProvider;
import com.newsgroupmanagement.model.Category;
import com.newsgroupmanagement.model.Role;
import com.newsgroupmanagement.model.User;
import com.newsgroupmanagement.repository.RoleRepository;
import com.newsgroupmanagement.repository.UserRepository;
import com.newsgroupmanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    @Autowired
    public DataSeeder(UserRepository userRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Create default roles if not exist
        createRoleIfNotExists("ROLE_MASTER_ADMIN");
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");

        // 2. Create master admin if not exists
        if (!userRepository.existsByEmail("neeljoshi780@gmail.com")){
            User masterAdmin = new User();
            masterAdmin.setFirstName("Neel");
            masterAdmin.setLastName("Joshi");
            masterAdmin.setEmail("neeljoshi780@gmail.com");
            masterAdmin.setPassword(passwordEncoder.encode("n@123456"));
            masterAdmin.setTermsAcceptedAt(LocalDateTime.now());
            masterAdmin.setAuthProvider(AuthProvider.LOCAL);

            Optional<Role> role = roleRepository.findByName("ROLE_MASTER_ADMIN");
            if(role.isPresent()){
                masterAdmin.getRoles().add(role.get());
            }

            userRepository.save(masterAdmin);

            System.out.println("Master admin user created.");
        }

        // 2. Create master admin if not exists
        if (!userRepository.existsByEmail("gautamjoshi780@gmail.com")){
            User admin = new User();
            admin.setFirstName("Gautam");
            admin.setLastName("Joshi");
            admin.setEmail("gautamjoshi780@gmail.com");
            admin.setPassword(passwordEncoder.encode("g@123456"));
            admin.setTermsAcceptedAt(LocalDateTime.now());
            admin.setAuthProvider(AuthProvider.LOCAL);

            Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
            if(role.isPresent()){
                admin.getRoles().add(role.get());
            }

            userRepository.save(admin);

            System.out.println("Admin user created.");
        }

        // 3. Create default news category if not exist
        createCategoryIfNotExists("India");
        createCategoryIfNotExists("World");
        createCategoryIfNotExists("Business");
        createCategoryIfNotExists("Politics");
        createCategoryIfNotExists("Education");
        createCategoryIfNotExists("Sports");
        createCategoryIfNotExists("Tech");
        createCategoryIfNotExists("Science");
        createCategoryIfNotExists("Health");
        createCategoryIfNotExists("Travel");
        createCategoryIfNotExists("Culture");
        createCategoryIfNotExists("Stories");
        createCategoryIfNotExists("Opinion");
        createCategoryIfNotExists("Day");
        createCategoryIfNotExists("Other");
    }

    public void createRoleIfNotExists(String roleName){
        if(!roleRepository.existsByRole(roleName)){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            System.out.println("Role created: " + roleName);
        }
    }

    public void createCategoryIfNotExists(String categoryName){
        if(!categoryRepository.existsByCategory(categoryName)){
            Category category = new Category();
            category.setName(categoryName);
            categoryRepository.save(category);
            System.out.println("Category created: " + categoryName);
        }
    }
}
