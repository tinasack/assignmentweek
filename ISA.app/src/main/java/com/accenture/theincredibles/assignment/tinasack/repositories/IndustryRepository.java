package com.accenture.theincredibles.assignment.tinasack.repositories;

import com.accenture.theincredibles.assignment.tinasack.models.Industry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndustryRepository {

    /** creating a connection to database for this repo **/
    private Connection connection;

    public IndustryRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Industry> showIndustry() throws SQLException {
        String sql = "select * from industry";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Industry> result = new ArrayList<>();
        while (resultSet.next()) {
            Industry industry = new Industry();
            industry.setId(resultSet.getInt("id"));
            industry.setName(resultSet.getString("name"));
            result.add(industry);
        }
        return result;
    }

    public Integer readIndustryByName(String industry) throws SQLException{
        String sql = "select id from industry where name = ?";
        PreparedStatement getIdStmt = connection.prepareStatement(sql);
        getIdStmt.setString(1, industry);
        ResultSet getIdResult = getIdStmt.executeQuery();
        if (getIdResult.next()) {
            return getIdResult.getInt(1);
        }
        return 0;
    }
}