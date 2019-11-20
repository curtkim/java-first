package example;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private ApplicationContext appContext;

    public static void printBeans(ApplicationContext appContext) {
        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            if( bean.split("\\.").length == 1)
                System.out.println(bean);
        }
    }

    @Test
    void test(@Autowired UserRepository userRepository){
        /**
         * 몽고에 관련된 것들만 생성된다.
         *
         * application
         * embeddedMongoConfiguration
         * embeddedMongoRuntimeConfig
         * embeddedMongoServer
         * gridFsTemplate
         * mappingMongoConverter
         * mongo
         * mongoCustomConversions
         * mongoDbFactory
         * mongoMappingContext
         * mongoTemplate
         * userRepository
         */
        printBeans(appContext);

        User user = new User();
        user.setName("test");
        userRepository.save(user);

        User user2 = userRepository.findAll().get(0);
        assertEquals("test", user2.getName());
    }
}
