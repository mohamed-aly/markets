package com.markets.demo.business.controller;

import com.markets.demo.DemoApplication;
import com.markets.demo.business.entity.Market;
import com.markets.demo.business.repo.MarketRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
@EnableJpaRepositories(basePackages = "com.markets.demo.business.repo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarketControllerTest {

    public final String CONTEXT_PATH = "/markets/";

    @Autowired
    private MarketRepository marketRepository;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }


    @Test
    @Order(1)
    void AddValidMarket() {
        Market market = Market.builder().address("address").arabicName("منتج").englishName("product").image(imageMock()).build();
        Response response = given().contentType("application/json").accept("application/json")
                .body(market).when()
                .post(CONTEXT_PATH).then().statusCode(201).
                        contentType("application/json").extract().response();
        long id = Long.parseLong(response.jsonPath().get("id").toString());
        assertNotNull(marketRepository.findById(id));
        assert (id == 1);

    }


    @Test
    @Order(2)
    void AddMarketWithEmptyData() {
        Market market = Market.builder().address("").arabicName("منتج2").englishName("product2").image(imageMock()).build();
        given().contentType("application/json").accept("application/json")
                .body(market).when()
                .post(CONTEXT_PATH).then().statusCode(400);
    }


    @Test
    @Order(3)
    void AddWithMarketAlreadySavedDetails() {
        Market market = Market.builder().address("address").arabicName("منتج").englishName("product").image(imageMock()).build();

        given().contentType("application/json").accept("application/json")
                .body(market).when()
                .post(CONTEXT_PATH).then().statusCode(400);
    }

    @Test
    @Order(4)
    void validMarketQuery() {

        Response response = given().contentType("application/json").accept("application/json")
                .when()
                .get(CONTEXT_PATH + "?query=product").then().statusCode(200).
                        contentType("application/json").extract().response();

        int totalElements = Integer.parseInt(response.jsonPath().get("totalElements").toString());
        assert totalElements == 1;

        List<Market> markets = response.jsonPath().getList("content", Market.class);
        assertNotNull(markets);
        assert markets.get(0).getId() == 1;
        assert markets.get(0).getEnglishName().equals("product");

    }

    @Test
    @Order(5)
    void invalidMarketQuery() {
        given().contentType("application/json").accept("application/json")
                .when()
                .get(CONTEXT_PATH + "?query=id>0").then().statusCode(400);
    }

    @Test
    @Order(6)
    void ListMarkets() {

        Response response = given().contentType("application/json").accept("application/json")
                .when()
                .get(CONTEXT_PATH).then().statusCode(200).
                        contentType("application/json").extract().response();

        int totalElements = Integer.parseInt(response.jsonPath().get("totalElements").toString());
        assert totalElements == 1;

        List<Market> markets = response.jsonPath().getList("content", Market.class);
        assertNotNull(markets);
        assert markets.get(0).getEnglishName().equals("product");

    }

    @Test
    @Order(7)
    void activateMarket() {

        Response response = given().contentType("application/json").accept("application/json")
                .when()
                .post(CONTEXT_PATH + "activate/1?active=true").then().statusCode(200).
                        contentType("application/json").extract().response();

        boolean isActive = response.jsonPath().getBoolean("active");

        assert isActive;

    }

    @Test
    @Order(8)
    void activateNotFoundMarket() {

        given().contentType("application/json").accept("application/json")
                .when().post(CONTEXT_PATH + "activate/2?active=true")
                .then().statusCode(404);


    }

    @Test
    @Order(9)
    void editMarket() {
        Market market = Market.builder().id(1).address("address").arabicName("منتج جديد").englishName("product").image(imageMock()).build();

        Response response = given().contentType("application/json").accept("application/json")
                .body(market).when()
                .patch(CONTEXT_PATH).then().statusCode(200).
                        contentType("application/json").extract().response();

        String arabicName = response.jsonPath().getString("arabicName");
        assert arabicName.equals("منتج جديد");

    }

    @Test
    @Order(10)
    void editOnlyEnglishNameMarket() {
        Market market = Market.builder().id(1).englishName("new product").build();

        Response response = given().contentType("application/json").accept("application/json")
                .body(market).when()
                .patch(CONTEXT_PATH).then().statusCode(200).
                        contentType("application/json").extract().response();

        String name = response.jsonPath().getString("englishName");
        assert name.equals("new product");

        String arabicName = response.jsonPath().getString("arabicName");
        assertNotNull(arabicName);

    }

    @Test
    @Order(11)
    void deleteMarket() {

        given().contentType("application/json").accept("application/json")
                .when().delete(CONTEXT_PATH + "1")
                .then().statusCode(200);


    }

    @Test
    @Order(12)
    void deleteNotFoundMarket() {

        given().contentType("application/json").accept("application/json")
                .when().delete(CONTEXT_PATH + "1")
                .then().statusCode(404);


    }


    private byte[] imageMock() {
        return Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAM1BMVEUKME7///+El6bw8vQZPVlHZHpmfpHCy9Ojsbzg5ekpSmTR2N44V29XcYayvsd2i5yTpLFbvRYnAAAJcklEQVR4nO2d17arOgxFs+kkofz/154Qmg0uKsuQccddT/vhnOCJLclFMo+//4gedzcApf9B4srrusk+GsqPpj+ypq7zVE9LAdLWWVU+Hx69y2FMwAMGyfusLHwIpooyw9IAQfK+8naDp3OGHvZ0FMhrfPMgVnVjC2kABOQ1MLvi0DEIFj1ILu0LU2WjNRgtSF3pKb4qqtd9IHmjGlJHlc09IHlGcrQcPeUjTAySAGNSkQlRhCCJMGaUC0HSYUx6SmxFAtJDTdylsr4ApC1TY0yquKbCBkk7qnYVzPHFBHkBojhVJWviwgPJrsP4qBgTgbQXdsesjm4pDJDmIuswVZDdFx0ENTtkihoeqSDXD6tVxOFFBHndMKxWvUnzexpIcx/Gg2goJJDhVo6PCMGRAnKTmZuKm3wcJO/upphUqUHy29yVrRhJDORXOKIkEZDf4YiRhEF+iSNCEgb5KY4wSRDkB/yurUEG8nMcocgYABnvbrVL3nMIP0h/d5udKnwzSC/InfPdkJ6eWb0PJE++dyVVyQP5iQmWW27X5QG5druEKafBu0Hqu9saVOHa8HKC/K6BzHKZiRMEZCDF0Nd1/ZfXI/fcOibHOssFgokg9uFA20BhztHEAZIjIohrD/o1wljeFBDEwBo8YUt5Ir/rNLjOIACPFdy/AbEcPdcJBOCxytjeYAM4Kzp6rhOIPhRGNzwmFP3rOoTFI0irtnQKx6fj1Zt+h9njEUS9mKJxfFRrX5lt7wcQtaWTOfTHeIXVJQcQrRW+OYex2j0a66XZINoO8a7fPH2iHF2mC7ZBtB3Czb5QvjizSx7A3308mRzqAwujSywQbYfwc0iU8zqjS0yQ6ztEHX9332KCaGNIYB/Qq1z3yN0oDZBWyeFYJBCkm2sXLhDtpKFwNDMu5TnrZpYGiHbK4Nlwikg5DrYV1g6iPoJmzE5MKd/fOp53EPUaQZaLqH3u+vo2ELWp3wSyWuYGoj9EEIJoV3L9AUS/ZLsJpLNBXmqOu0CW6P5A/dx9IL0FAji/FYKot9EqE0Tvs6QBUe/2CxMEkZAlBNGPhdoAQWyTSmbxUwvUygwQyMmniAPgLt87CODXHuftWJIQgzrfQDC5AfwSgz9MmmG/gWCOqDgZ4JsQeTvZBoJJDhAFEsSDyxUEEUUekk0UEMhjBcEcGsoWVpBU3NcCgkkPkJWrKbdRZvULCMTWhYEdMrayBQRyqHcnSLmAIH7LcWJ8Hch7BsHEdWFpJsZjziCgFBpZ9TPm4e0XBJTTJKt9xjy8RoLI4gimPLP5goCSgWTrEcyzsy8IqmZVMo0H5bJiQToBCOjZ5RcElhjLN3dU7uQMAvoxwQkJZKI1CQzCthJYEigahHuDDi4rFwzCPQ7F1fiDQZgTR5iJwEGYRgIsiECD8BwwMAEfDcIaW8CRBQdhjS1kJQEchDEFhiRKr4KDFPS9FGQNVwEHoW83QjsEHdkfnuIOl6C1NjMItiaCaCWgbdpFJXQ9soh2uoB9aJcCxFdgZwlcrTmvENGlrITBBdpK25Qhd1F2RScq8CKu/gsCL8qN5THjy+Rr5E6joYgPxpdl518QrCf8Kpgjn6C8HLkbb+vt7ZM8wdVvy258khsRfHaS5DalDnlidZT7Erk+SXV5Bj1D3LS29XyhVJuoKHs9Q8S6reK11oUc7vPcr9uswP3SLiDINefXOF5rwCuGzVT6zVkVPfh2wWmHcz4wAwba2cgN1/Tsvleu7//i69CgVyt1GwjOs2+XK3rtbl151Tg3vOeioG40Mz2V+6pQ4xbJHOZj6g0EMxk93tV7fuedvVZpQSPhbwNBGInrymGrwNh1GXmL8F+lAaJ+NU/fzcmvJqvKj7177+1v1GY/GiBKI1Fdy/2XK6upXwaIJpI8B/399W0mH9zzafKaeCF9J0WF+jyCuFusTGzZKhFH8dVLZql2brxgcdVBKb7KG/7UZTmB3XJ6uL/QYT5ScRI74FcHEJ7feopyfGkaeaGlPoCw/BbjZmSBWIvINQNmTxdjWJqwUI8sztR4nYPuIPSTSUnOCZOE3ierqRoJfNSQxDjLEYs8i91eqgFCDSWiFHiuqAN9CwEGCPEISVjvwhS7Mfx6dtX8kC5aqvneGBOEFN2v6RBiYwr3DQOkLhEW6fHFbIwFQnkLiWYmZxE220z/aedPx99C+hiyKR4OzNFhg8S75CJTnxQ1dyugHTLaY10iu9dBpmhQtMz1ABLrkgtHVnRsPUO3OcU25i8cWdGxZbflCBKJqBdMs3aF/dYhNexU9RFcYEmLXYQKghyWdufyldBSU3KpjkKhZclxTXQGCTkL/HZDUIH5+Gkt4SgoCtj7pSYSNJLTK3VVRnmXZxebSMBIzmHABeIdXBebiN9eHYtUZ62ab3BdGkUm+SKJw1bdRXeewaX7qqdAnljg2sVxg3guAk3baofcg9yZ2eZpnHNvSFrEqhB9YPjesmt0pt6Xc8hl7W5L9Q4Xx09ctsrd5VhWeF6nF8SRrZdw49qns//0xTK/AZ8vGr3caTliuzeFNeCJTgafpKlhHd2WP1sy1LqDF798gjKJPLqDr9keoTd43+NyNzC1CI8Xy2lcPtOaVBI5IiAWyQ3e125AcKoXs2Djhy5eVc3KiBxREIPkhjBiLhIjU++4T91IbggjRiCJLSEIwWGddkEaxlVN5KCArPHk8mXVpHk8FHH7JL3n5dPA7C90q7XkeFJucacNmGXeRfswLE71HA79efaGiCN/Ofjmfmtcp8X10tIsqCacV5xfRWjNUiXGYbovWgyFYHcQLak15K9oM5zqmgaeKsHJetbSHfSPzXOiw/rxE9YH4CXaUpsZ0ztemFurP95Jpyvrd29YTpIZr7cEJHqfc7Wl0PFm2+yJR70udaokKFtGPTdm8WdQe24+HmVLlueboWQquBcYYVH2vEzfh8kCks1p90eWsLCyZ8qK7E86Oe+3XYFnBuiWdth20UqZR5SvMoyPg3WNauJipi0LMTQgVq5xUUlZcrPsopPHJ926z8pm7xyFLrH/PxpHSoXKdWgXsLn1scZn1ZDd/2vszN3lt254qkE+qu3yoqLM+ghN3Qz2qcVzUC/ZMFsK/alU6l0OWV/bQz6v6yYbyuN5BaZ4A7Y30vs/PPksS2+qzlvfF7OQmzzcL7W+xa7OIfRuVdtn/tdvdFLnL4OTKcm2W16PmWc4FWWXNSlWM2n3D+uPxuyrcfo74aP+Ac30a82+oLmfAAAAAElFTkSuQmCC");
    }


}