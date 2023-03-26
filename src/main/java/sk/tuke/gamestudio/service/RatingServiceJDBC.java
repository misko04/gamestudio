package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBC implements RatingService {
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String JDBC_USER = "postgres";
    public static final String JDBC_PASSWORD = "postgres";
    public static final String INSERT_STATEMENT =
            "INSERT INTO rating (player, game, rating_num, rating_date) VALUES (?, ?, ?, ?) ON CONFLICT ON CONSTRAINT rating_player_game_unique DO UPDATE SET rating_num=(excluded.rating_num), rating_date=(excluded.rating_date)";
    public static final String SELECT_STATEMENT = "SELECT player, game, rating_num, rating_date FROM rating WHERE game = ? ORDER BY rating_num DESC ";
    public static final String SELECT_AVERAGE = "SELECT AVG(rating_num) FROM rating WHERE game = ?";
    public static final String SELECT_RATING = "SELECT rating_num FROM rating WHERE game = ? AND player = ? AND rating is not null";
    private static final String DELETE_STATEMENT = "DELETE FROM score";

    @Override
    public void addRating(Rating rating) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)
        ) {
            statement.setString(1, rating.getPlayer());
            statement.setString(2, rating.getGame());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatingDate().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public List<Rating> getRating(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)
        ) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                var rating = new ArrayList<Rating>();
                while (rs.next()) {
                    rating.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return rating;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public int getAverageRating(String game){
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_AVERAGE);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                int result;
                rs.next();
                result = rs.getInt(1);
                return result;
            }
        } catch (SQLException e) {
            throw new GameStudioException("Problem getting rating", e);
        }
    }

    @Override
    public int getYourRating(String game, String player){
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_RATING);
        ) {
            statement.setString(1, game);
            statement.setString(2,player);
            try (ResultSet rs = statement.executeQuery()) {
                int result;
                rs.next();
                result = rs.getInt(1);
                return result;
            }
        } catch (SQLException e) {
            throw new GameStudioException("Problem getting rating", e);
        }
    }

    @Override
    public void resetRating() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }
}