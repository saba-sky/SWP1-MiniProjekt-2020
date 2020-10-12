package de.unibremen.swp;

import de.unibremen.swp.data.CSVUser;
import de.unibremen.swp.data.Converter;
import de.unibremen.swp.data.JSONUser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest {

    private final Converter converter = new Converter();

    private final Parser parser = new Parser();

    @Test
    void CSVToJSON() throws IOException, ParseException {
        byte[] bytes = getClass()
                .getResourceAsStream("/users.csv")
                .readAllBytes();
        String csv = new String(bytes, StandardCharsets.UTF_8);
        String json = converter.csvToJSON(csv);

        List<JSONUser> users = parser.readJSONUsers(json);
        assertEquals(
                5,
                users.size(),
                "Number of user objects");

        JSONUser michael = users.get(0);
        JSONUser dwight = users.get(1);
        JSONUser pam = users.get(2);
        JSONUser jim = users.get(3);
        JSONUser ryan = users.get(4);

        assertEquals(
                "Michael",
                michael.getFirstName(),
                "Michael's first name");
        assertEquals(
                "Scott",
                michael.getLastName(),
                "Michael's last name");
        assertEquals(
                "Progress, not perfection.",
                michael.getMotto(),
                "Michael's motto");
        assertEquals(
                List.of(pam, jim, ryan),
                michael.getFriends(),
                "Michael's friends");

        assertEquals(
                "Dwight",
                dwight.getFirstName(),
                "Dwight's first name");
        assertEquals(
                "Schrute",
                dwight.getLastName(),
                "Dwight's last name");
        assertEquals(
                "If you’re going through hell, keep going.",
                dwight.getMotto(),
                "Dwight's motto");
        assertEquals(
                List.of(),
                dwight.getFriends(),
                "Dwight's friends");

        assertEquals(
                "Pam",
                pam.getFirstName(),
                "Pam's first name");
        assertEquals(
                "Beesly",
                pam.getLastName(),
                "Pam's last name");
        assertEquals(
                "Your pain today will be your strength tomorrow.",
                pam.getMotto(),
                "Pam's motto");
        assertEquals(
                List.of(michael, jim),
                pam.getFriends(),
                "Pam's friends");

        assertEquals(
                "Jim",
                jim.getFirstName(),
                "Jim's first name");
        assertEquals(
                "Halpert",
                jim.getLastName(),
                "Jim's last name");
        assertEquals(
                "Act or accept.",
                jim.getMotto(),
                "Jim's motto");
        assertEquals(
                List.of(michael, pam),
                jim.getFriends(),
                "Jim's friends");

        assertEquals(
                "Ryan",
                ryan.getFirstName(),
                "Ryan's first name");
        assertEquals(
                "Howard",
                ryan.getLastName(),
                "Ryan's last name");
        assertEquals(
                "I am strong,\nI am confident,\nand I am unstoppable.",
                ryan.getMotto(),
                "Ryan's motto");
        assertEquals(
                List.of(ryan),
                ryan.getFriends(),
                "Ryan's friends");
    }

    @Test
    void JSONToCSV() throws IOException, ParseException {
        byte[] bytes = getClass()
                .getResourceAsStream("/users.json")
                .readAllBytes();
        String json = new String(bytes, StandardCharsets.UTF_8);
        String csv = converter.jsonToCSV(json);

        List<CSVUser> users = parser.readCSVUsers(csv);
        assertEquals(
                5,
                users.size(),
                "Number of user objects");

        CSVUser michael = users.get(0);
        assertEquals(
                1,
                michael.getId(),
                "Michael's id");
        assertEquals(
                "Michael",
                michael.getFirstName(),
                "Michael's first name");
        assertEquals(
                "Scott",
                michael.getLastName(),
                "Michael's last name");
        assertEquals(
                "Progress, not perfection.",
                michael.getMotto(),
                "Michael's motto");
        assertEquals(
                List.of(3, 4, 5),
                michael.getFriends(),
                "Michael's friends");

        CSVUser dwight = users.get(1);
        assertEquals(
                2,
                dwight.getId(),
                "Dwight's id");
        assertEquals(
                "Dwight",
                dwight.getFirstName(),
                "Dwight's first name");
        assertEquals(
                "Schrute",
                dwight.getLastName(),
                "Dwight's last name");
        assertEquals(
                "If you’re going through hell, keep going.",
                dwight.getMotto(),
                "Dwight's motto");
        assertEquals(
                List.of(),
                dwight.getFriends(),
                "Dwight's friends");

        CSVUser pam = users.get(2);
        assertEquals(
                3,
                pam.getId(),
                "Pam's id");
        assertEquals(
                "Pam",
                pam.getFirstName(),
                "Pam's first name");
        assertEquals(
                "Beesly",
                pam.getLastName(),
                "Pam's last name");
        assertEquals(
                "Your pain today will be your strength tomorrow.",
                pam.getMotto(),
                "Pam's motto");
        assertEquals(
                List.of(1, 4),
                pam.getFriends(),
                "Pam's friends");

        CSVUser jim = users.get(3);
        assertEquals(
                4,
                jim.getId(),
                "Jim's id");
        assertEquals(
                "Jim",
                jim.getFirstName(),
                "Jim's first name");
        assertEquals(
                "Halpert",
                jim.getLastName(),
                "Jim's last name");
        assertEquals(
                "Act or accept.",
                jim.getMotto(),
                "Jim's motto");
        assertEquals(
                List.of(1, 3),
                jim.getFriends(),
                "Jim's friends");

        CSVUser ryan = users.get(4);
        assertEquals(
                5,
                ryan.getId(),
                "Ryan's id");
        assertEquals(
                "Ryan",
                ryan.getFirstName(),
                "Ryan's first name");
        assertEquals(
                "Howard",
                ryan.getLastName(),
                "Ryan's last name");
        assertEquals(
                "I am strong,\nI am confident,\nand I am unstoppable.",
                ryan.getMotto(),
                "Ryan's motto");
        assertEquals(
                List.of(5),
                ryan.getFriends(),
                "Ryan's friends");
    }
}
