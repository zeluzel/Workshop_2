package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private final static String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private final static String READ_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private final static String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private final static String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private final static String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private final static String DELETE_ALL_USERS_QUERY = "DELETE FROM users";

    //    tworzy w bazie danych rekord na podstawie danych obiektu user
    //    wypełnia pole id obiektu kluczem pobranym z bazy danych
    public User create(User user) {
        try (Connection connection = DBUtil.connectToDB()) {
//            tworzymy szablon zapytania tworzącego nowy rekord w bazie danych
//            używamy metody prepareStatement z dodatkowym parametrem, który sprawia,
//            że potem możemy z niego wyciągnąć wygenerowany klucz
            PreparedStatement prepStatement =
                    connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
//            na odpowiednie miejsca w szablonie zapytania wstawiamy pola obiektu user
            prepStatement.setString(1, user.getUsername());
            prepStatement.setString(2, user.getEmail());
            prepStatement.setString(3, hashPassword(user.getPassword()));
//            wywołujemy gotowe, wypełnione zapytanie
            prepStatement.executeUpdate();  // execute update, bo coś się zmienia
//            pobieramy odpowiedź bazy na nasze zapytanie i zapisujemy ją do obiektu klasy ResultSet
//            metoda getGeneratedKeys() jest metodą klasy Statement, po której dziedziczy klasa PreparedStatement
//            i zwraca obiekt klasy ResultSet, zawierający automatycznie wygenerowany klucz nowego rekordu
            ResultSet createResult = prepStatement.getGeneratedKeys();
            if (createResult.next()) {  //jeśli w obiekcie jest zawarta jest informacja
//                z pierwszej kolumny obiektu ResultSet pobierz klucz i zapisz go w obiekcie user
                user.setId(createResult.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    //    tworzy obiekt klasy User na podstawie rekordu w bazie danych
    public User read(int userId) {
        try (Connection connection = DBUtil.connectToDB()) {
            PreparedStatement prepStatement = connection.prepareStatement(READ_USER_QUERY);
            prepStatement.setInt(1, userId);
            ResultSet readResult = prepStatement.executeQuery();  // executeQuery, bo nic nie zmieniam
            if (readResult.next()) {  // czy potrzebne jest to if?
                User user = new User();
                user.setUsername(readResult.getString("username"));
                user.setEmail(readResult.getString("email"));
                user.setPassword(readResult.getString("password"));  // tu chyba trzeba odhaszować hasło??
                user.setId(userId);
                return user;
            } else {
                return null;  // nie lepiej byłoby wyrzucić tu wyjątek?
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }


    //    zmienia dane w bazie na podstawie przekazanego obiektu user
    public void update(User user) {
        try (Connection connection = DBUtil.connectToDB()) {
            PreparedStatement prepStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            prepStatement.setString(1, user.getUsername());
            prepStatement.setString(2, user.getEmail());
            prepStatement.setString(3, hashPassword(user.getPassword()));
//            po odczytaniu użytkownika i zupdateowaniu
//            zapisze się podówjnie szyfrowane hasło
            prepStatement.setInt(4, user.getId());
//            jeśli nie executeUpdate nie zwróciło, że zmieniło jeden wiersz, to coś jest nie tak
            if (prepStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //    usuwa z bazy danych użytkownika o o id przekazanym jako parametr
    public void delete(int userId) {
        try (Connection connection = DBUtil.connectToDB()) {
            PreparedStatement prepStatement = connection.prepareStatement(DELETE_USER_QUERY);
            prepStatement.setInt(1, userId);
            if (prepStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private User[] addToArray(User user, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = user; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }


    public User[] findAll() {
        try (Connection connection = DBUtil.connectToDB()) {
//            tutaj nie potrzebujemy preparedStatement, bo zapytanie jest niesparametryzowane

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_USERS_QUERY);
            User[] allUsers = new User[0];
//            User user = new User();
            while (resultSet.next()) {
//                poniższa deklaracja musi być w pętli, bo inaczej będziemy modyfikować jednego usera
//                i dodawać go do tablicy - na końcu będziemy mieli tablicę takich samych userów
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                allUsers = addToArray(user, allUsers);
            }
            return allUsers;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new User[0];
    }

    public void deleteAll() {
        try (Connection conn = DBUtil.connectToDB()) {
            Statement query = conn.createStatement();
            query.executeQuery(DELETE_ALL_USERS_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}