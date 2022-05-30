package integration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Tag("integration")
public class UserApiTest {

    static String BASE_URL= "http://localhost:8080";
    RestTemplate template = new RestTemplate();


    @BeforeAll
    static void setup() {
        MongoDatabaseFactory factory = new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/test");
        MongoTemplate mongoTemplate = new MongoTemplate(factory);
        mongoTemplate.dropCollection("user");
    }

    @Test
    void test1_get_list() {
        String result = template.getForObject(BASE_URL+"/user/", String.class);
        assertEquals("[]", result);
    }

    @Test
    void test2_post() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("name", "name1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = template.postForEntity( BASE_URL+"/user/add", request , String.class );
        //System.out.println(response.getBody());
        JSONObject json = new JSONObject(response.getBody());
        assertEquals("name1", json.getString("name"));
    }

    @Test
    void test3_get_list() {
        String result = template.getForObject(BASE_URL+"/user/", String.class);
        JSONArray arr = new JSONArray(result);

        assertEquals(1, arr.length());
        JSONObject json = arr.getJSONObject(0);
        assertEquals("name1", json.getString("name"));
    }

}
