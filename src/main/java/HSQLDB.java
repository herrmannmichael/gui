import configuration.Configuration;

import java.sql.*;

public enum HSQLDB {
    instance;

    private Connection connection;

    public void setupConnection() {
        System.out.println("--- setupConnection");

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = Configuration.instance.driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, Configuration.instance.username, Configuration.instance.password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlStatement);
            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    /*
       [algorithms]
       id TINYINT       NOT NULL PK
       name VARCHAR(10) NOT NULL unique
    */
    public void createTableAlgorithms() {
        System.out.println("--- createTableAlgorithms");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE IF NOT EXISTS algorithms (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX IF NOT EXISTS idx_algorithms ON algorithms (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    /*
       [types]
       id TINYINT       NOT NULL PK
       name VARCHAR(10) NOT NULL unique
    */
    public void createTableTypes() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE IF NOT EXISTS types (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX IF NOT EXISTS idx_types ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        String query = "INSERT INTO types (id,name) VALUES (1,'normal');";
        update(query);
        query = "INSERT INTO types (id,name) VALUES (2,'intruder');";
        update(query);

    }

    /*
       [participants]
       id TINYINT       NOT NULL PK
       name VARCHAR(50) NOT NULL unique
       type_id TINYINT  NOT NULL FK
    */
    public void createTableParticipants() {
        System.out.println("--- createTableParticipants");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE IF NOT EXISTS participants (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("type_id TINYINT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX IF NOT EXISTS idx_participants ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE participants ADD CONSTRAINT IF NOT EXISTS fk_participants ");
        sqlStringBuilder03.append("FOREIGN KEY (type_id) ");
        sqlStringBuilder03.append("REFERENCES types (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());

        update(sqlStringBuilder03.toString());
    }

    /*
       [channel]
       name           VARCHAR(25) NOT NULL PK
       participant_01 TINYINT NOT NULL FK
       participant_02 TINYINT NOT NULL FK
    */
    public void createTableChannel() {
        System.out.println("--- createTableChannel");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE IF NOT EXISTS channel (");
        sqlStringBuilder01.append("name VARCHAR(25) NOT NULL").append(",");
        sqlStringBuilder01.append("participant_01 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_02 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (name)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE channel ADD CONSTRAINT IF NOT EXISTS fk_channel_01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_01) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE channel ADD CONSTRAINT IF NOT EXISTS fk_channel_02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_02) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());
    }

    /*
      [messages]
      id                  TINYINT NOT NULL
      participant_from_id TINYINT NOT NULL
      participant_to_id   TINYINT NOT NULL
      plain_message       VARCHAR(50) NOT NULL
      algorithm_id        TINYINT NOT NULL
      encrypted_message   VARCHAR(50) NOT NULL
      keyfile             VARCHAR(20) NOT NULL
      timestamp           INT
    */
    public void createTableMessages() {
        System.out.println("--- createTableMessages");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE IF NOT EXISTS messages (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_to_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("plain_message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("algorithm_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("encrypted_message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("keyfile VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder01.append("timestamp INT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE messages ADD CONSTRAINT IF NOT EXISTS fk_messages_01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE messages ADD CONSTRAINT IF NOT EXISTS fk_messages_02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_to_id) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());

        StringBuilder sqlStringBuilder04 = new StringBuilder();
        sqlStringBuilder04.append("ALTER TABLE messages ADD CONSTRAINT IF NOT EXISTS fk_messages_03 ");
        sqlStringBuilder04.append("FOREIGN KEY (algorithm_id) ");
        sqlStringBuilder04.append("REFERENCES algorithms (id) ");
        sqlStringBuilder04.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder04.toString());
        update(sqlStringBuilder04.toString());
    }

    /*
       [postbox_[participant_name]]
       id                  TINYINT NOT NULL
       participant_from_id TINYINT NOT NULL
       message             VARCHAR(50) NOT NULL
       timestamp           INT
     */
    public void createTablePostbox(String participantName) {
        String table = "postbox_" + participantName;
        System.out.println("--- createTablePostbox_" + participantName);

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE IF NOT EXISTS ").append(table).append(" (");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("timestamp BIGINT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE ").append(table).append(" ADD CONSTRAINT IF NOT EXISTS fk_postbox_").append(participantName);
        sqlStringBuilder02.append(" FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void shutdown() {
        System.out.println("--- shutdown");

        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    public void setupDatabase() {
        setupConnection();
        createTableAlgorithms();
        createTableTypes();
        createTableParticipants();
        createTableChannel();
        createTableMessages();
        createTablePostbox("msa");
    }

    public String registerParticipant(String participantName, String type) throws SQLException {
        String query = "SELECT COUNT(name) AS count FROM participants WHERE name = '" + participantName+"'";
        ResultSet result = select(query);
        result.next();
        int count = result.getInt("count");
        int typeAsInt = 0;

        query = "SELECT COUNT(name) AS count FROM participants";
        ResultSet resultSet1 = select(query);
        resultSet1.next();
        int maxCount = resultSet1.getInt("count")+1;


        if(count==0){
            if(type.equals("normal")){
                typeAsInt = 1;
            }
            else if(type.equals("intruder")){
                typeAsInt = 2;
            }
            query = "INSERT INTO participants (id,name,type_id) VALUES ("+ maxCount +",'"+participantName+"',"+typeAsInt+");";
            update(query);
            createTablePostbox(participantName);
            return "participant ["+participantName+"] with type["+type+"] registered and postbox_["+participantName+"] created";
        }
        else {
            return "participant ["+participantName+"] already exists, using existing postbox_["+participantName+"]";
        }
    }

    public String showChannel () throws SQLException {
        String query = "SELECT name,participant_01, participant_02 FROM channel";
        ResultSet resultSet = select(query);

        StringBuilder sqlStringBuilder01 = new StringBuilder();

        while (resultSet.next()){

            sqlStringBuilder01.append(resultSet.getString("name")).append(" | ");
            int par1 = resultSet.getInt("participant_01");
            query = "SELECT name FROM participants WHERE id="+par1;
            ResultSet resultSet1 = select(query);
            resultSet1.next();
            String parName1 = resultSet1.getString("name");
            sqlStringBuilder01.append(parName1).append(" and ");

            int par2 = resultSet.getInt("participant_02");
            query = "SELECT name FROM participants WHERE id="+par2;
            resultSet1 = select(query);
            resultSet1.next();
            String parName2 = resultSet1.getString("name");
            sqlStringBuilder01.append(parName2).append("\n\r");
        }
        return sqlStringBuilder01.toString();
    }

    public String createChannel(String channelName, String participant01, String participant02) throws SQLException {
        if(doesChannelNameExist(channelName)){
            return "channel [" +  channelName + "] already exists";
        }
        else if(doesChannelWithParticipantsExist(participant01, participant02)){
            return "communication channel between participant01 [" + participant01 + "] and participant02 [" + participant02 + "] already exists";
        }
        else if(participant01.equals(participant02)){
            return "participant01 [" + participant01 + "] and participant02 [" + participant02 + "] are identical – cannot create channel on itself";
        }
        else {
            String participant01Query = "SELECT COUNT(name) AS count FROM participants WHERE name = " + participant01;
            ResultSet resultPar01 = select(participant01Query);
            resultPar01.next();
            int countParticipant01 = resultPar01.getInt("count");
            int participant01ID = 0;
            if(countParticipant01 > 0){
                participant01ID = resultPar01.getInt("id");
            }
            else
                return "participant01 [" + participant01 + "] not found";

            String participant02Query = "SELECT COUNT(name) AS count FROM participants WHERE name = " + participant02;
            ResultSet resultPar02 = select(participant01Query);
            resultPar02.next();
            int countParticipant02 = resultPar02.getInt("count");
            int participant02ID = 0;
            if(countParticipant02 > 0){
                participant02ID = resultPar02.getInt("id");
            }
            else
                return "participant02 [" + participant02 + "] not found";

            String query = "INSERT INTO channel VALUES('" + channelName + "'," + participant01ID + "," + participant02ID + ")";
            update(query);

            return "channel [" + channelName + "] from [" + participant01 + "] to [" + participant02 +"] successfullycreated";
        }
    }

    private boolean doesChannelWithParticipantsExist(String participant01, String participant02) throws SQLException {
        String participant01Query = "SELECT COUNT(name) AS count FROM participants WHERE name = " + participant01;
        ResultSet resultPar01 = select(participant01Query);
        resultPar01.next();
        int countParticipant01 = resultPar01.getInt("count");
        int participant01ID = 0;
        if(countParticipant01 > 0){
            participant01ID = resultPar01.getInt("id");
        }

        String participant02Query = "SELECT COUNT(name) AS count FROM participants WHERE name = " + participant02;
        ResultSet resultPar02 = select(participant01Query);
        resultPar02.next();
        int countParticipant02 = resultPar02.getInt("count");
        int participant02ID = 0;
        if(countParticipant02 > 0){
            participant02ID = resultPar02.getInt("id");
        }


        String query = "SELECT COUNT(name) AS count FROM channel WHERE participant_01 = '" + participant01ID + "' AND participant_02 = '" + participant02ID + "'" +
                " OR participant_01 = '" + participant02ID + "' AND participant_02 = '" + participant01ID + "'";

        System.out.println(query);
        ResultSet result = select(query);
        result.next();
        int count = result.getInt("count");
        if(count > 0){
            return true;
        }
        return false;
    }

    private boolean doesChannelNameExist(String channelName) throws SQLException {
        String query = "SELECT COUNT(name) AS count FROM channel WHERE name = '" + channelName+"'";
        ResultSet result = select(query);
        result.next();
        int count = result.getInt("count");
        if(count > 0){
            return true;
        }
        return false;
    }


    public String dropChannel(String channelName) throws SQLException {
        String query = "SELECT COUNT(name) AS count FROM channel WHERE name = '" + channelName+"'";
        ResultSet result = select(query);
        result.next();
        int count = result.getInt("count");

        if(count!=0){
            query = "DELETE FROM channel WHERE name = '" + channelName+"'";
            update(query);
            return "channel ["+channelName+"] deleted";
        }
        else {
            return "unknown channel ["+channelName+"]";
        }
    }





    private synchronized ResultSet select(String sqlStatement){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            statement.close();
            return resultSet;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}