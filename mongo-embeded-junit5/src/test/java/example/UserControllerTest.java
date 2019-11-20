package example;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static example.UserRepositoryTest.printBeans;
import static org.hamcrest.CoreMatchers.is;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGet_thenReturnJsonArray() throws Exception {
        /*
            application
            applicationTaskExecutor
            basicErrorController
            beanNameHandlerMapping
            beanNameViewResolver
            defaultServletHandlerMapping
            defaultValidator
            defaultViewResolver
            dispatcherServlet
            dispatcherServletPath
            error
            errorAttributes
            errorPageCustomizer
            formContentFilter
            geoJsonModule
            handlerExceptionResolver
            handlerFunctionAdapter
            httpRequestHandlerAdapter
            jacksonGeoModule
            jacksonObjectMapper
            jacksonObjectMapperBuilder
            jsonComponentModule
            mappingJackson2HttpMessageConverter
            messageConverters
            methodValidationPostProcessor
            mockMvc
            mockMvcBuilder
            mvcContentNegotiationManager
            mvcConversionService
            mvcHandlerMappingIntrospector
            mvcPathMatcher
            mvcResourceUrlProvider
            mvcUriComponentsContributor
            mvcUrlPathHelper
            mvcValidator
            mvcViewResolver
            pageableCustomizer
            pageableResolver
            parameterNamesModule
            preserveErrorControllerTargetClassPostProcessor
            projectingArgumentResolverBeanPostProcessor
            requestContextFilter
            requestMappingHandlerAdapter
            requestMappingHandlerMapping
            resourceHandlerMapping
            routerFunctionMapping
            simpleControllerHandlerAdapter
            sortCustomizer
            sortResolver
            springBootMockMvcBuilderCustomizer
            standardJacksonObjectMapperBuilderCustomizer
            stringHttpMessageConverter
            taskExecutorBuilder
            userController
            viewControllerHandlerMapping
            viewResolver
            welcomePageHandlerMapping
         */
        printBeans(appContext);

        User user1 = new User("name1");
        user1.setId(new ObjectId());
        User user2 = new User("name2");
        user1.setId(new ObjectId());

        List<User> users = Arrays.asList(user1, user2);
        given(userService.findAll()).willReturn(users);

        mvc.perform(get("/user/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("name1")))
                .andExpect(jsonPath("$[1].name", is("name2")));
    }

}
