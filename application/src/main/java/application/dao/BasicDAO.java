package application.dao;

import application.model.BaseModel;
import application.model.Repjegy_foglalas;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class BasicDAO<T extends BaseModel> {

    public static Connection databaseConnection;

    public static void connectToDatabase(){
        Connection conn2 = null;
        try {
            // registers Oracle JDBC driver - though this is no longer required
            // since JDBC 4.0, but added here for backward compatibility
            Class.forName("oracle.jdbc.OracleDriver");


            // METHOD #2
            String dbURL2 = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "adatbazishoz_felhasznalonev";
            String password = "adatbazishoz_jelszo";
            conn2 = DriverManager.getConnection(dbURL2, username, password);
            if (conn2 != null) {
                System.out.println("Connected with connection #2");
            }

        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        databaseConnection = conn2;
    }

    public static void unconnectFromDatabase(){
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param obj
     * @param column UPPERCASE!!!
     * @param value
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T extends BaseModel> List<T> select(T obj, String column, String value) throws Exception {
        connectToDatabase();

        String tableName = obj.getClass().getName().toUpperCase().substring(obj.getClass().getPackage().getName().length() + 1);
        String command = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, column, value);

        Statement stmt = databaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(command);
        List<T> result = new ArrayList<T>();

        while(rs.next()) {
            T newObj = (T)obj.clone();

            for(Field field : newObj.getClass().getDeclaredFields()) {
                if(field.getType() == int.class) {
                    field.set(newObj, rs.getInt(field.getName().toUpperCase()));

                    if(rs.wasNull())
                        field.set(newObj, null);
                }

                if(field.getType() == double.class) {
                    field.set(newObj, rs.getDouble(field.getName().toUpperCase()));

                    if(rs.wasNull())
                        field.set(newObj, null);
                }

                if(field.getType() == String.class)
                    field.set(newObj, rs.getString(field.getName().toUpperCase()));
            }

            result.add(newObj);
        }
        stmt.close();
        unconnectFromDatabase();

        return result;
    }

    public <T extends BaseModel> List<T> selectAll(T obj) throws Exception{
        connectToDatabase();

        String tableName = obj.getClass().getName().toUpperCase().substring(obj.getClass().getPackage().getName().length() + 1);
        String command = String.format("SELECT * FROM %s ", tableName);

        Statement stmt = databaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(command);
        List<T> result = new ArrayList<T>();

        while(rs.next()) {
            T newObj = (T)obj.clone();

            for(Field field : newObj.getClass().getDeclaredFields()) {

                System.out.println(field.getName().toUpperCase());
                if(field.getType() == int.class) {
                    field.set(newObj, rs.getInt(field.getName().toUpperCase()));

                    if(rs.wasNull())
                        field.set(newObj, null);
                }

                if(field.getType() == double.class) {
                    field.set(newObj, rs.getDouble(field.getName().toUpperCase()));

                    if(rs.wasNull())
                        field.set(newObj, null);
                }

                if(field.getType() == String.class)
                    field.set(newObj, rs.getString(field.getName().toUpperCase()));
            }

            result.add(newObj);
        }
        stmt.close();
        unconnectFromDatabase();

        return result;
    }
    public <T extends BaseModel> List<T> SelectSpecificObject(T obj, String sql, int felhaszn_id) throws Exception{
        connectToDatabase();


        CallableStatement cs = databaseConnection.prepareCall(sql);
        cs.registerOutParameter(1, OracleTypes.CURSOR);
        cs.setInt(2,felhaszn_id);
        cs.execute();
        ResultSet rs = (ResultSet)cs.getObject(1);
        List<T> result = new ArrayList<T>();

        while(rs.next()) {
            T newObj = (T)obj.clone();

            for(Field field : newObj.getClass().getDeclaredFields()) {

                System.out.println(field.getName().toUpperCase());
                if(field.getType() == int.class) {
                    field.set(newObj, rs.getInt(field.getName().toUpperCase()));

                    if(rs.wasNull())
                        field.set(newObj, null);
                }

                if(field.getType() == double.class) {
                    field.set(newObj, rs.getDouble(field.getName().toUpperCase()));

                    if(rs.wasNull())
                        field.set(newObj, null);
                }

                if(field.getType() == String.class)
                    field.set(newObj, rs.getString(field.getName().toUpperCase()));
            }

            result.add(newObj);
        }
        cs.close();
        unconnectFromDatabase();

        return result;
    }

    /**
     * @param obj
     * @param skipColumns UPPERCASE!!!
     * @return
     * @throws Exception
     */
    public boolean insert(T obj, List<String> skipColumns) throws Exception {
        connectToDatabase();
        Field[] fields = obj.getClass().getDeclaredFields();

        if(skipColumns.size() > 0) {
            for(int i = 0; i < fields.length; i++) {
                if(skipColumns.contains(fields[i].getName().toUpperCase())) {
                    Field[] newArr = new Field[fields.length - 1];

                    int c = 0;

                    for(int j = 0; j < fields.length; j++) {
                        if(i == j) {
                            continue;
                        }

                        newArr[c++] = fields[j];
                    }

                    fields = newArr;
                }
            }
        }

        String tableName = obj.getClass().getName().toUpperCase().substring(obj.getClass().getPackage().getName().length() + 1);
        String columnList = String.join(", ", Arrays.stream(fields).map(f -> f.getName().toUpperCase()).collect(Collectors.toList()));
        String valueList  = String.join(", ", Arrays.stream(fields).map(f -> {
            try {
                return f.get(obj) == null ? "null" : String.format("'%s'", f.get(obj).toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));

        String command = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnList, valueList);
        Statement stmt = databaseConnection.createStatement();

        try {
            stmt.execute(command);
        }
        catch(Exception ex) {
            return false;
        }
        finally {
            stmt.close();
            unconnectFromDatabase();
        }

        return true;
    }

    /**
     * @param obj
     * @param skipColumns UPPERCASE!!!
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     */
    public boolean update(T obj, List<String> skipColumns) throws SQLException, IllegalAccessException {
        connectToDatabase();

        Field[] fields = obj.getClass().getDeclaredFields();

        if(fields.length == 0)
            return false;

        if(skipColumns.size() > 0) {
            for(int i = 0; i < fields.length; i++) {
                if(skipColumns.contains(fields[i].getName().toUpperCase())) {
                    Field[] newArr = new Field[fields.length - 1];

                    int c = 0;

                    for(int j = 0; j < fields.length; j++) {
                        if(i == j) {
                            continue;
                        }

                        newArr[c++] = fields[j];
                    }

                    fields = newArr;
                }
            }
        }

        Field[] fieldsWithoutPK = Arrays.copyOfRange(fields, 1, fields.length);
        Field primaryKey = fields[0];

        String tableName = obj.getClass().getName().toUpperCase().substring(obj.getClass().getPackage().getName().length() + 1);
        String valueUpdateSubstr = String.join(", ", Arrays.stream(fieldsWithoutPK).map(f -> {
            try {
                return String.format("%s = %s", f.getName(), f.get(obj) == null ? "null" : String.format("'%s'", f.get(obj).toString()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));

        String command = String.format("UPDATE %s SET %s WHERE %s = '%s'", tableName, valueUpdateSubstr, primaryKey.getName(), primaryKey.get(obj).toString());
        Statement stmt = databaseConnection.createStatement();

        try {
            stmt.execute(command);
        }
        catch(Exception ex) {
            return false;
        }
        finally {
            stmt.close();
            unconnectFromDatabase();
        }

        return true;
    }

    public boolean delete(T obj) throws SQLException, IllegalAccessException {
        connectToDatabase();

        Field primaryKey = obj.getClass().getDeclaredFields()[0];
        String tableName = obj.getClass().getName().toUpperCase().substring(obj.getClass().getPackage().getName().length() + 1);

        String command = String.format("DELETE FROM %s WHERE %s = '%s'", tableName, primaryKey.getName(), primaryKey.get(obj).toString());
        Statement stmt = databaseConnection.createStatement();

        try {
            stmt.execute(command);
        }
        catch(Exception ex) {
            return false;
        }
        finally {
            stmt.close();
            unconnectFromDatabase();
        }

        return true;
    }

    public <T extends BaseModel> List<T> runSql(T obj, String sql) throws SQLException, CloneNotSupportedException, IllegalAccessException {
        connectToDatabase();

        Statement stmt = databaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<T> result = new ArrayList<T>();

        while (rs.next()) {
            T newObj = (T) obj.clone();

            for (Field field : newObj.getClass().getFields()) {
                if (field.getType() == int.class) {
                    field.set(newObj, rs.getInt(field.getName()));

                    if (rs.wasNull())
                        field.set(newObj, null);
                }

                if (field.getType() == double.class) {
                    field.set(newObj, rs.getDouble(field.getName()));

                    if (rs.wasNull())
                        field.set(newObj, null);
                }

                if (field.getType() == String.class)
                    field.set(newObj, rs.getString(field.getName()));
            }

            result.add(newObj);
        }

        stmt.close();
        unconnectFromDatabase();

        return result;
    }
    public static ResultSet SelectSmth(String sql) throws SQLException {
        connectToDatabase();

        Statement stmt = databaseConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<String> result = new ArrayList<String>();

        return rs;
    }

    public static boolean UpdateSmth(String sql) throws SQLException, IllegalAccessException {
        connectToDatabase();

        Statement stmt = databaseConnection.createStatement();

        try {
            stmt.execute(sql);
        }
        catch(Exception ex) {
            return false;
        }
        finally {
            stmt.close();
            unconnectFromDatabase();
        }

        return true;
    }

    public static List<Repjegy_foglalas> getRepjegyfoglalasuser(int felhaszn_id) throws SQLException {
        connectToDatabase();

        CallableStatement cs = databaseConnection.prepareCall("{call lekerdezes_eljaras(?)}");
        cs.setInt(1, felhaszn_id);
        try {
            ResultSet rs =cs.executeQuery();
        }
        catch(Exception ex) {
            return null;
        }
        finally {
            cs.close();
            unconnectFromDatabase();
        }
        return null;
    }
}

