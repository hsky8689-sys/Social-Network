package org.example.repository;
/*
import org.example.domain.Movie;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MovieDBRepository implements Repository<Long, Movie> {

    private String url;
    private String username;
    private String password;

    public MovieDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Movie> findOne(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from movies where id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                int year = resultSet.getInt("year");
                Movie movie = new Movie(title, director, year);
                movie.setId(id);
                return Optional.of(movie);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Movie> findAll() {
        Set<Movie> movies = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from movies");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                String director = resultSet.getString("director");
                int year = resultSet.getInt("year");
                Movie movie = new Movie(title, director, year);
                movie.setId(id);
                movies.add(movie);
            }
            return movies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> save(Movie entity) {
        String insertSQL = "insert into movies (title, director, year) values (?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDirector());
            statement.setInt(3, entity.getYear());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        String deleteSQL = "delete from movies where id = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setLong(1, id);
            Optional<Movie> foundUser = findOne(id);
            int response = 0;
            if (foundUser.isPresent()) {
                response = statement.executeUpdate();
            }
            return response == 0 ? Optional.empty() : foundUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> update(Movie entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String updateSQL = "update movies set title = ?, director = ?, year = ? where id = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateSQL);) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDirector());
            statement.setInt(3, entity.getYear());
            statement.setLong(4, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
*/