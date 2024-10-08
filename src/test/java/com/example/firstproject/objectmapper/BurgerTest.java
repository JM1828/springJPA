package com.example.firstproject.objectmapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.lang.reflect.Array.set;
import static org.junit.jupiter.api.Assertions.*;

class BurgerTest {

    @Test
    public void 자바_객체를_JSON으로_변환() throws JsonProcessingException {
        // 준비
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ingredients = Arrays.asList("순쇠고기 패티", "바삭한 치킨패티", "토마토", "스파이시 어니언 화이트 소스");
        Burger burger = new Burger("맥도날드 핫 크리스피버거", 8500, ingredients);

        // 수행
        String json = objectMapper.writeValueAsString(burger);

        // 예상
        String expected = "{\"name\":\"맥도날드 핫 크리스피버거\",\"price\":8500,\"ingredients\":[\"순쇠고기 패티\",\"바삭한 치킨패티\",\"토마토\",\"스파이시 어니언 화이트 소스\"]}";

        // 검증
        assertEquals(expected, json);
        JsonNode jsonNode = objectMapper.readTree(json);
        System.out.println(jsonNode.toPrettyString());
    }

    @Test
    public void JSON을_자바_객체로_변환() throws JsonProcessingException {
        // 준비
        ObjectMapper objectMapper = new ObjectMapper();
        /*
        {
            "name" : "맥도날드 핫 크리스피버거"
            "price" : 8600
            "ingredients" : [ "순 쇠고기 패티", "핫 치킨 패티", "토마토", "스파이시 어니언 화이트 소스" ]
        }
         */
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", "맥도날드 핫 크리스피버거");
        objectNode.put("price", 8600);

        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add("순 쇠고기 패티");
        arrayNode.add("핫 치킨 패티");
        arrayNode.add("토마토");
        arrayNode.add("스파이시 어니언 화이트 소스");
        objectNode,set("ingredients", arrayNode);
        String json = objectNode.toString();

        // 수행
        Burger burger = objectMapper.readValue(json, Burger.class);

        // 예상
        List<String> ingredients = Arrays.asList("순쇠고기 패티", "바삭한 치킨패티", "토마토", "스파이시 어니언 화이트 소스");
        Burger expected = new Burger("맥도날드 핫 크리스피버거", 8500, ingredients);

        // 검증
        assertEquals(expected.toString(), burger.toString());
        JsonNode jsonNode = objectMapper.readTree(json);
        System.out.println(jsonNode.toPrettyString());
        System.out.println(burger);
    }
}