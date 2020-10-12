package de.unibremen.swp;

import de.unibremen.swp.data.CSVUser;
import de.unibremen.swp.data.JSONUser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    private final Parser parser = new Parser();

    @Nested
    class CSV {
        @Test
        void readFromExampleFile() throws IOException, ParseException {
            byte[] bytes = getClass()
                    .getResourceAsStream("/users.csv")
                    .readAllBytes();
            String csv = new String(bytes, StandardCharsets.UTF_8);

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

        @Test
        void writeAndCompareWithRead() throws ParseException {
            CSVUser stanly = new CSVUser();
            stanly.setId(1);
            stanly.setFirstName("Stanly");
            stanly.setLastName("Hudson");
            stanly.setMotto("Either you run the day or the day runs you.");

            CSVUser kelly = new CSVUser();
            kelly.setId(2);
            kelly.setFirstName("Kelly");
            kelly.setLastName("Kapoor");
            kelly.setFriends(List.of(3));

            CSVUser phyllis = new CSVUser();
            phyllis.setId(3);
            phyllis.setFirstName("Phyllis");
            phyllis.setLastName("Vance");
            phyllis.setFriends(List.of(1, 2));

            String csv = parser.writeCSVUsers(List.of(stanly, kelly, phyllis));
            List<CSVUser> users = parser.readCSVUsers(csv);
            assertEquals(
                    3,
                    users.size(),
                    "Number of user objects");

            stanly = users.get(0);
            assertEquals(
                    1,
                    stanly.getId(),
                    "Stanly's id");
            assertEquals(
                    "Stanly",
                    stanly.getFirstName(),
                    "Stanly's first name");
            assertEquals(
                    "Hudson",
                    stanly.getLastName(),
                    "Stanly's last name");
            assertEquals(
                    "Either you run the day or the day runs you.",
                    stanly.getMotto(),
                    "Stanly's motto");
            assertEquals(
                    List.of(),
                    stanly.getFriends(),
                    "Stanly's friends");

            kelly = users.get(1);
            assertEquals(
                    2,
                    kelly.getId(),
                    "Kelly's id");
            assertEquals(
                    "Kelly",
                    kelly.getFirstName(),
                    "Kelly's first name");
            assertEquals(
                    "Kapoor",
                    kelly.getLastName(),
                    "Kelly's last name");
            assertEquals(
                    "",
                    kelly.getMotto(),
                    "Kelly's motto");
            assertEquals(
                    List.of(3),
                    kelly.getFriends(),
                    "Kelly's friends");

            phyllis = users.get(2);
            assertEquals(
                    3,
                    phyllis.getId(),
                    "Phyllis' id");
            assertEquals(
                    "Phyllis",
                    phyllis.getFirstName(),
                    "Phyllis' first name");
            assertEquals(
                    "Vance",
                    phyllis.getLastName(),
                    "Phyllis' last name");
            assertEquals(
                    "",
                    phyllis.getMotto(),
                    "Phyllis' motto");
            assertEquals(
                    List.of(1, 2),
                    phyllis.getFriends(),
                    "Phyllis' friends");
        }

        @Test
        void readWithInvalidId() {
            assertThrows(IllegalArgumentException.class,
                    () -> parser.readCSVUsers(
                            "ID‽First Name‽Last Name‽Motto‽Friends\n0‽‽‽‽"));

            assertThrows(IllegalArgumentException.class,
                    () -> parser.readCSVUsers(
                            "ID‽First Name‽Last Name‽Motto‽Friends\n2‽‽‽‽"));
        }

        @Test
        void writeWithInvalidId() {
            CSVUser user = new CSVUser();
            Executable executable = () ->
                    parser.writeCSVUsers(List.of(user));

            user.setId(0);
            assertThrows(IllegalArgumentException.class, executable);

            user.setId(2);
            assertThrows(IllegalArgumentException.class, executable);
        }

        @Test
        void readDifferentWithSameId() {
            assertThrows(IllegalArgumentException.class,
                    () -> parser.readCSVUsers(
                            "ID‽First Name‽Last Name‽Motto‽Friends\n"
                                    + "1‽a‽‽‽\n"
                                    + "1‽b‽‽‽"));
        }

        @Test
        void writeDifferentWithSameId() {
            CSVUser u1 = new CSVUser();
            u1.setId(1);
            CSVUser u2 = new CSVUser();
            u2.setId(1);

            assertThrows(IllegalArgumentException.class, () ->
                    parser.writeCSVUsers(List.of(u1, u2)));
        }

        @Test
        void writeSameTwice() {
            CSVUser u = new CSVUser();
            u.setId(1);

            String csv = parser.writeCSVUsers(List.of(u, u));
            int count = (int) csv.chars().filter(c -> c == '1').count();
            assertEquals(
                    1,
                    count,
                    "Occurrence of id");
        }

        @Test
        void readWithInvalidFriends() {
            assertThrows(IllegalArgumentException.class,
                    () -> parser.readCSVUsers(
                            "ID‽First Name‽Last Name‽Motto‽Friends\n1‽‽‽‽2,3"));
        }

        @Test
        void writeWithInvalidFriends() {
            CSVUser u = new CSVUser();
            u.setId(1);
            u.setFriends(List.of(2, 3));

            assertThrows(IllegalArgumentException.class, () ->
                    parser.writeCSVUsers(List.of(u)));
        }

        @Test
        void readNullParameter() {
            assertThrows(NullPointerException.class,
                    () -> parser.readCSVUsers(null));
        }

        @Test
        void writeNullParameter() {
            assertThrows(NullPointerException.class,
                    () -> parser.writeCSVUsers(null));
        }

        @Test
        void writeNullInListAndCompareWithRead() throws ParseException {
            CSVUser u = new CSVUser();
            u.setId(1);
            // ArrayList permits null.
            ArrayList<CSVUser> list = new ArrayList<>();
            list.add(u);
            list.add(null);

            String csv = parser.writeCSVUsers(list);
            List<CSVUser> users = parser.readCSVUsers(csv);
            assertEquals(
                    1,
                    users.size(),
                    "Number of user objects");
        }
    }

    @Nested
    class JSON {
        @Test
        void readFromExampleFile() throws IOException, ParseException {
            byte[] bytes = getClass()
                    .getResourceAsStream("/users.json")
                    .readAllBytes();
            String json = new String(bytes, StandardCharsets.UTF_8);

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
        void writeAndCompareWithRead() throws ParseException {
            JSONUser stanly = new JSONUser();
            stanly.setFirstName("Stanly");
            stanly.setLastName("Hudson");
            stanly.setMotto("Either you run the day or the day runs you.");

            JSONUser kelly = new JSONUser();
            kelly.setFirstName("Kelly");
            kelly.setLastName("Kapoor");

            JSONUser phyllis = new JSONUser();
            phyllis.setFirstName("Phyllis");
            phyllis.setLastName("Vance");

            kelly.setFriends(List.of(phyllis));
            phyllis.setFriends(List.of(stanly, kelly));

            String json = parser.writeJSONUsers(
                    List.of(stanly, kelly, phyllis));
            List<JSONUser> users = parser.readJSONUsers(json);
            assertEquals(
                    3,
                    users.size(),
                    "Number of user objects");

            stanly = users.get(0);
            kelly = users.get(1);
            phyllis = users.get(2);

            assertEquals(
                    "Stanly",
                    stanly.getFirstName(),
                    "Stanly's first name");
            assertEquals(
                    "Hudson",
                    stanly.getLastName(),
                    "Stanly's last name");
            assertEquals(
                    "Either you run the day or the day runs you.",
                    stanly.getMotto(),
                    "Stanly's motto");
            assertEquals(
                    List.of(),
                    stanly.getFriends(),
                    "Stanly's friends");

            assertEquals(
                    "Kelly",
                    kelly.getFirstName(),
                    "Kelly's first name");
            assertEquals(
                    "Kapoor",
                    kelly.getLastName(),
                    "Kelly's last name");
            assertEquals(
                    "",
                    kelly.getMotto(),
                    "Kelly's motto");
            assertEquals(
                    List.of(phyllis),
                    kelly.getFriends(),
                    "Kelly's friends");

            assertEquals(
                    "Phyllis",
                    phyllis.getFirstName(),
                    "Phyllis' first name");
            assertEquals(
                    "Vance",
                    phyllis.getLastName(),
                    "Phyllis' last name");
            assertEquals(
                    "",
                    phyllis.getMotto(),
                    "Phyllis' motto");
            assertEquals(
                    List.of(stanly, kelly),
                    phyllis.getFriends(),
                    "Phyllis' friends");
        }

        @Test
        void writeSameTwice() {
            JSONUser u = new JSONUser();

            String json = parser.writeJSONUsers(List.of(u, u));
            int count = (int) json.chars().filter(c -> c == '@').count();
            assertEquals(
                    1,
                    count,
                    "Occurrence of @ (from @id)");
        }

        @Test
        void readNullParameter() {
            assertThrows(NullPointerException.class,
                    () -> parser.readJSONUsers(null));
        }

        @Test
        void writeNullParameter() {
            assertThrows(NullPointerException.class,
                    () -> parser.writeJSONUsers(null));
        }

        @Test
        void writeNullInListAndCompareWithRead() throws ParseException {
            JSONUser u = new JSONUser();
            // ArrayList permits null.
            ArrayList<JSONUser> list = new ArrayList<>();
            list.add(u);
            list.add(null);

            String json = parser.writeJSONUsers(list);
            List<JSONUser> users = parser.readJSONUsers(json);
            assertEquals(
                    1,
                    users.size(),
                    "Number of user objects");
        }
    }
}
