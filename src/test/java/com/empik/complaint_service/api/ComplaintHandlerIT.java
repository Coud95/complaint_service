package com.empik.complaint_service.api;

import com.empik.complaint_service.infrastructure.entity.ComplaintEntity;
import com.empik.complaint_service.infrastructure.entity.SubmitterEntity;
import com.empik.complaint_service.infrastructure.repository.ComplaintRepository;
import com.empik.complaint_service.infrastructure.repository.SubmitterRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComplaintHandlerIT {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL_ADDRESS = "john.doe@empik.pl";
    private static final String DESCRIPTION = "Test complaint";
    private static final String COUNTRY = "US";
    private static final String POST_REQUEST_BODY = "{\n" +
            "  \"productId\": \"123\",\n" +
            "  \"description\": \"Test complaint\",\n" +
            "  \"submitter\": {\n" +
            "    \"firstName\": \"John\",\n" +
            "    \"lastName\": \"Doe\",\n" +
            "    \"emailAddress\": \"john.doe@empik.pl\"\n" +
            "  }\n" +
            "}";
    private static final String PUT_REQUEST_BODY = "{\n" +
            "  \"complaintId\": \"1\",\n" +
            "  \"description\": \"New description\"\n" +
            "}";

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @LocalServerPort
    private Integer port;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        submitterRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    ComplaintRepository complaintRepository;

    @Autowired
    SubmitterRepository submitterRepository;

    @Test
    void shouldSubmitComplaint() {
        with().body(POST_REQUEST_BODY)
                .contentType(ContentType.JSON)
                .when()
                .post("/complaint")
                .then()
                .statusCode(201);

        ComplaintEntity complaintEntity = complaintRepository.findAll().getFirst();

        assertEquals(123L, complaintEntity.getProductId());
        assertEquals(DESCRIPTION, complaintEntity.getDescription());
        assertEquals(FIRST_NAME, complaintEntity.getSubmitterEntity().getFirstName());
        assertEquals(LAST_NAME, complaintEntity.getSubmitterEntity().getLastName());
        assertEquals(EMAIL_ADDRESS, complaintEntity.getSubmitterEntity().getEmailAddress());
        assertEquals(1, complaintEntity.getSubmitCount());
        assertEquals("Unknown", complaintEntity.getCountry());
    }

    @Test
    void shouldIncrementSubmitCount() {
        with().body(POST_REQUEST_BODY)
                .contentType(ContentType.JSON)
                .when()
                .post("/complaint")
                .then()
                .statusCode(201);

        with().body(POST_REQUEST_BODY)
                .contentType(ContentType.JSON)
                .when()
                .post("/complaint")
                .then()
                .statusCode(201);

        ComplaintEntity complaintEntity = complaintRepository.findAll().getFirst();
        assertEquals(2, complaintEntity.getSubmitCount());
    }

    @Test
    void shouldEditComplaint() {
        with().body(POST_REQUEST_BODY)
                .contentType(ContentType.JSON)
                .when()
                .post("/complaint")
                .then()
                .statusCode(201);

        with().body(PUT_REQUEST_BODY)
                .contentType(ContentType.JSON)
                .when()
                .put("/complaint")
                .then()
                .statusCode(200);

        ComplaintEntity complaintEntity = complaintRepository.findAll().getFirst();
        assertEquals("New description", complaintEntity.getDescription());
    }

    @Test
    void shouldGetComplaintById() {
        ComplaintEntity complaintEntity = createComplaint();

        submitterRepository.save(complaintEntity.getSubmitterEntity());
        complaintRepository.save(complaintEntity);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/complaint/{id}", complaintEntity.getComplaintId())
                .then()
                .statusCode(200)
                .body("submitter.firstName", equalTo(FIRST_NAME))
                .body("submitter.lastName", equalTo(LAST_NAME))
                .body("submitter.emailAddress", equalTo(EMAIL_ADDRESS))
                .body("description", equalTo(DESCRIPTION))
                .body("productId", equalTo(1))
                .body("submitCount", equalTo(1))
                .body("country", equalTo(COUNTRY))
                .body("creationDate", equalTo(complaintEntity.getCreationDate()
                        .truncatedTo(ChronoUnit.MICROS).toString()));
    }

    @Test
    void shouldGetAllComplaints() {
        ComplaintEntity complaintEntity = createComplaint();

        submitterRepository.save(complaintEntity.getSubmitterEntity());
        complaintRepository.save(complaintEntity);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/complaint")
                .then()
                .statusCode(200)
                .body("[0].submitter.firstName", equalTo(FIRST_NAME))
                .body("[0].submitter.lastName", equalTo(LAST_NAME))
                .body("[0].submitter.emailAddress", equalTo(EMAIL_ADDRESS))
                .body("[0].description", equalTo(DESCRIPTION))
                .body("[0].productId", equalTo(1))
                .body("[0].submitCount", equalTo(1))
                .body("[0].country", equalTo(COUNTRY))
                .body("[0].creationDate", equalTo(complaintEntity.getCreationDate()
                        .truncatedTo(ChronoUnit.MICROS).toString()));
    }

    private ComplaintEntity createComplaint() {
        SubmitterEntity submitter = new SubmitterEntity();
        submitter.setFirstName(FIRST_NAME);
        submitter.setLastName(LAST_NAME);
        submitter.setEmailAddress(EMAIL_ADDRESS);
        ComplaintEntity complaint = new ComplaintEntity();
        complaint.setDescription(DESCRIPTION);
        complaint.setProductId(1L);
        complaint.setSubmitterEntity(submitter);
        complaint.setSubmitCount(1);
        complaint.setCountry(COUNTRY);
        LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        complaint.setCreationDate(creationDate);
        return complaint;
    }

}
