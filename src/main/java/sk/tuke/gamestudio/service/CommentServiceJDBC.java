package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String JDBC_USER = "postgres";
    public static final String JDBC_PASSWORD = "postgres";
    public static final String INSERT_STATEMENT = "INSERT INTO comment (player, comment_text, comment_date, game) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "SELECT player, comment_text, comment_date, game FROM comment WHERE game = ? ORDER BY comment_date DESC";
    private static final String DELETE_STATEMENT = "DELETE FROM score";

    @Override
    public void addComment(Comment comment) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)
        ) {
            statement.setString(1, comment.getPlayer());
            statement.setString(2, comment.getComment());
            statement.setTimestamp(3, new Timestamp(comment.getCommentDate().getTime()));
            statement.setString(4, comment.getGame());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)
        ) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                var comment = new ArrayList<Comment>();
                while (rs.next()) {
                    comment.add(new Comment(rs.getString(1), rs.getString(4), rs.getString(2), rs.getTimestamp(3)));
                }
                return comment;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void resetComment() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }
}
