package com.WorkDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    public List<Email> getEmails() throws SQLException {
        List<Email> workList = new ArrayList<>();
        PreparedStatement pr = Connect.SQLConnection("SELECT * FROM emails WHERE sent_at IS NULL");
        ResultSet rs = pr.executeQuery();
        while(rs.next()){
            Email email = new Email();
            email.setId(rs.getLong("id"));
            email.setContent(rs.getString("content"));
            email.setSendTo(rs.getString("send_to"));
            workList.add(email);
        }
        return workList;

    }

    public void setDate(long id) throws SQLException {
        String now = String.valueOf(LocalDateTime.now());
        PreparedStatement pr = Connect.SQLConnection("UPDATE emails SET sent_at = ? WHERE id = ?");
        pr.setString(1, now);
        pr.setLong(2, id);
        pr.execute();


    }

}
