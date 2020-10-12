package de.unibremen.swp.data;

import de.unibremen.swp.Parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Allows to convert between {@link CSVUser} stored in CSV strings and
 * {@link JSONUser} stored in JSON strings. Uses {@link Parser} for
 * serialization and deserialization.
 */

public class Converter {

    /**
     * The parser that is used to serialize and deserialize the corresponding
     * {@link CSVUser} and {@link JSONUser} objects.
     */
    private final Parser parser = new Parser();

    /**
     * Converts the given CSV string with {@link CSVUser} to a JSON string with
     * {@link JSONUser}.
     *
     * @param csv The CSV string to convert.
     * @return The converted JSON string.
     * @throws NullPointerException     If {@code csv} is {@code null}.
     * @throws IllegalArgumentException If {@link Parser#readCSVUsers(String)} or
     *                                  {@link Parser#writeJSONUsers(List)} throws an
     *                                  {@link IllegalArgumentException}.
     * @throws ParseException           If {@link Parser#readCSVUsers(String)} throws a
     *                                  {@link ParseException}.
     */
    public String csvToJSON(final String csv) throws NullPointerException,
            IllegalArgumentException, ParseException {
        if (csv == null) {
            throw new NullPointerException();
        }
        Parser parser = new Parser();
        List<CSVUser> csvUsers = parser.readCSVUsers(csv);
        List<JSONUser> jsonUsers = new ArrayList<>();

        for (CSVUser csvUser : csvUsers) {
            JSONUser jsonUser = new JSONUser();
            jsonUser.setFirstName(csvUser.getFirstName());
            jsonUser.setLastName(csvUser.getLastName());
            jsonUser.setMotto(csvUser.getMotto());
            jsonUsers.add(jsonUser);
        }

        for (int i = 0; i < jsonUsers.size(); i++) {
            List<JSONUser> friends = new ArrayList<>();
            for (int j = 0; j < csvUsers.get(i).getFriends().size(); j++) {
                friends.add(jsonUsers.get(csvUsers.get(i).getFriends().get(j) - 1));
            }
            jsonUsers.get(i).setFriends(friends);
            friends.clear();
        }
        return parser.writeJSONUsers(jsonUsers);

    }


    /**
     * Converts the given JSON string with {@link JSONUser} to a CSV string
     * with {@link CSVUser}.
     *
     * @param json The JSON string to convert.
     * @return The converted CSV string.
     * @throws NullPointerException     If {@code json} is {@code null}.
     * @throws IllegalArgumentException If {@link Parser#readJSONUsers(String)} or
     *                                  {@link Parser#writeCSVUsers(List)} throws an
     *                                  {@link IllegalArgumentException}.
     * @throws ParseException           If {@link Parser#readJSONUsers(String)} throws a
     *                                  {@link ParseException}.
     */
    public String jsonToCSV(final String json) throws NullPointerException,
            IllegalArgumentException, ParseException {
        Parser parser = new Parser();
        List<JSONUser> jsonUsers = parser.readJSONUsers(json);
        List<CSVUser> csvUsers = new ArrayList<>();
        Map<String, Integer> namesAndIds = new TreeMap<>();

        for (int i = 0; i < jsonUsers.size(); i++) {
            CSVUser csvUser = new CSVUser();
            csvUser.setId(i + 1);
            csvUser.setFirstName(jsonUsers.get(i).getFirstName());
            csvUser.setLastName(jsonUsers.get(i).getLastName());
            csvUser.setMotto(jsonUsers.get(i).getMotto());
            namesAndIds.put(jsonUsers.get(i).getFirstName(), i + 1);
            csvUsers.add(csvUser);
        }

        for (int i = 0; i < csvUsers.size(); i++) {
            List<Integer> friendsIds = new ArrayList<>();
            for (int j = 0; j < jsonUsers.get(i).getFriends().size(); j++) {
                friendsIds.add(namesAndIds.get(jsonUsers.get(i).getFriends().get(j).getFirstName()));
            }
            csvUsers.get(i).setFriends(friendsIds);
            friendsIds.clear();
        }
        return parser.writeCSVUsers(csvUsers);
    }
}
