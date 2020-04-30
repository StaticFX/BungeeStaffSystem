package de.ziemlich.bungeeStaffSystem.logsystem.db;

import de.ziemlich.bungeeStaffSystem.logsystem.util.ChatLog;
import de.ziemlich.bungeeStaffSystem.logsystem.util.MessageLog;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LogDAO {

    public static final LogDAO INSTANCE = new LogDAO();

    public static LogDAO getInstance() {
        return INSTANCE;
    }

    public void createLog(String id, ProxiedPlayer p, ChatLog log) throws SQLException {
        System.out.println(log.getMessages().size());
        for(int i = log.getMessages().size(); i > 0; i--) {
            System.out.print(1);
            StaffSystemManager.ssm.getMainSQL().executeUpdate("INSERT INTO logs(UUID, timeStamp, message, logID) VALUES(?, ?, ?, ?)",p.getUniqueId().toString(), log.getTimes().get(i - 1), log.getMessages().get(i - 1), id);
        }
    }

    public MessageLog getMessageLog(String id) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM logs WHERE logID = ?", Arrays.asList(id));
        List<Long> times = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        while(rs.next()) {
            long time = rs.getLong("timeStamp");
            String message = rs.getString("message");
            times.add(time);
            messages.add(message);
        }
        rs.close();
        return new MessageLog(times,messages);
    }

    public void removeLog(String id) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("DELETE FROM logs WHERE logID = ?", id);
    }

    public void storeLog(String id, int newID) {
        StaffSystemManager.ssm.getMainSQL().executeUpdate("UPDATE logs SET logID = ? WHERE logID = ?",newID,Integer.toString(newID));
    }

    public List<Integer> getStoredLogsIDs() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT * FROM logs", null);
        while(rs.next()) {
            int id = rs.getInt("logID");
            if(!ids.contains(id)) ids.add(id);
        }
        rs.close();
        return ids;
    }

    public UUID getUUIDForID(String id) throws SQLException {
        ResultSet rs = StaffSystemManager.ssm.getMainSQL().getResult("SELECT UUID FROM logs WHERE logID = ?", Arrays.asList(id));
        if(rs.next()) {
            UUID player = UUID.fromString(rs.getString("UUID"));
            rs.close();
            return player;
        }
        return null;
    }


}