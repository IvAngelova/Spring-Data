package orm;

import annotations.Column;
import annotations.Entity;
import annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EntityManager<E> implements DbContext<E> {
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    public void doCreate(Class<E> entityClass) throws SQLException {
        String tableName = getTableName(entityClass);
        String fieldsWithTypes = getSQLFieldsWithTypes(entityClass);

        String createQuery = String.format("CREATE TABLE %s (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT, %s)",
                tableName, fieldsWithTypes);

        PreparedStatement statement = connection.prepareStatement(createQuery);
        statement.execute();
    }

    private String getSQLFieldsWithTypes(Class<E> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .map(field -> {
                    String fieldName = field.getAnnotationsByType(Column.class)[0].name();

                    String sqlType = getSQLType(field.getType());

                    return fieldName + " " + sqlType;
                })
                .collect(Collectors.joining(","));
    }

    private String getSQLType(Class<?> type) {
        String sqlType = "";
        if (type == Integer.class || type == int.class) {
            sqlType = "INT";
        } else if (type == String.class) {
            sqlType = "VARCHAR(200)";
        } else if (type == LocalDate.class) {
            sqlType = "DATE";
        }

        return sqlType;
    }

    public void doAlter(Class<E> entityClass) throws SQLException {
        String tableName = getTableName(entityClass);
        String addColumnStatements = getAddColumnStatementsForNewFields(entityClass);

        String alterQuery = String.format("ALTER TABLE %s %s", tableName, addColumnStatements);

        PreparedStatement statement = connection.prepareStatement(alterQuery);
        statement.execute();
    }

    private String getAddColumnStatementsForNewFields(Class<E> entityClass) throws SQLException {
        Set<String> sqlColumns = getSQLColumnNames(entityClass);

        List<Field> fields = Arrays.stream(entityClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());

        List<String> allAddStatements = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getAnnotationsByType(Column.class)[0].name();

            if (sqlColumns.contains(fieldName)) {
                continue;
            }

            String sqlType = getSQLType(field.getType());

            String addStatement = String.format("ADD COLUMN %s %s", fieldName, sqlType);
            allAddStatements.add(addStatement);
        }

        return String.join(",", allAddStatements);
    }

    private Set<String> getSQLColumnNames(Class<E> entityClass) throws SQLException {
        String schemaQuery = "SELECT COLUMN_NAME FROM information_schema.`COLUMNS` c" +
                " WHERE c.TABLE_SCHEMA = 'custom-orm'" +
                " AND COLUMN_NAME != 'id'" +
                " AND TABLE_NAME = 'users';";

        PreparedStatement statement = connection.prepareStatement(schemaQuery);

        ResultSet resultSet = statement.executeQuery();

        Set<String> result = new HashSet<>();
        while(resultSet.next()) {
            String columnName = resultSet.getString("COLUMN_NAME");

            result.add(columnName);
        }

        return result;
    }

    @Override
    public boolean delete(E toDelete) throws IllegalAccessException, SQLException {
        String tableName = getTableName(toDelete.getClass());
        Field idColumn = getIdColumn(toDelete.getClass());

        String idColumnName = idColumn.getAnnotationsByType(Column.class)[0].name();

        idColumn.setAccessible(true);
        Object idColumnValue = idColumn.get(toDelete);

        String query = String.format("DELETE FROM %s WHERE %s = %s",
                tableName, idColumnName, idColumnValue);

        PreparedStatement statement = connection.prepareStatement(query);

        return statement.execute();
    }

    @Override
    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field idColumn = getIdColumn(entity.getClass());
        idColumn.setAccessible(true);
        Object idValue = idColumn.get(entity);

        if (idValue == null || (long) idValue <= 0) {
            return doInsert(entity, idColumn);
        }

        return doUpdate(entity, (long) idValue);
    }

    private boolean doUpdate(E entity, long idValue) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity.getClass());
        List<String> tableFields = getColumnsWithoutId(entity.getClass());
        List<String> tableValues = getColumnsValuesWithoutId(entity);

        List<String> setStatements = new ArrayList<>();

        for (int i = 0; i < tableFields.size(); i++) {
            setStatements.add(tableFields.get(i) + " = " + tableValues.get(i));
        }

        String updateQuery = String.format("UPDATE %s SET %s WHERE id = %d",
                tableName,
                String.join(", ", setStatements),
                idValue);

        PreparedStatement statement = connection.prepareStatement(updateQuery);
        return statement.execute();

    }

    private boolean doInsert(E entity, Field idColumn) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity.getClass());
        List<String> tableFields = getColumnsWithoutId(entity.getClass()); // username, age, registration_date
        List<String> tableValues = getColumnsValuesWithoutId(entity);

        String insertQuery = String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, String.join(", ", tableFields), String.join(", ", tableValues));

        return connection.prepareStatement(insertQuery).execute();
    }

    private List<String> getColumnsValuesWithoutId(E entity) throws IllegalAccessException {
        Class<?> clazz = entity.getClass();
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());

        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(entity);

            if (o instanceof String || o instanceof LocalDate) {
                values.add("'" + o + "'");
            } else {
                values.add(o.toString());
            }
        }

        return values;
    }

    private List<String> getColumnsWithoutId(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .filter(f -> f.isAnnotationPresent(Column.class))
                .map(f -> f.getAnnotationsByType(Column.class))
                .map(a -> a[0].name())
                .collect(Collectors.toList());

    }

    private String getTableName(Class<?> clazz) {
        Entity[] annotationsByType = clazz.getAnnotationsByType(Entity.class);

        if (annotationsByType.length == 0) {
            throw new UnsupportedOperationException("Class must be Entity");
        }

        return annotationsByType[0].name();

    }

    private Field getIdColumn(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() ->
                        new UnsupportedOperationException("Entity missing an Id column"));
    }

    @Override
    public Iterable<E> find(Class<E> table) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return find(table, null);
    }

    @Override
    public Iterable<E> find(Class<E> table, String where) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String tableName = getTableName(table);

        String selectQuery = String.format("SELECT * FROM %s %s",
                tableName, where != null ? "WHERE " + where : "");

        PreparedStatement statement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = statement.executeQuery();

        List<E> result = new ArrayList<>();
        while (resultSet.next()) {
            E entity = table.getDeclaredConstructor().newInstance();
            fillEntity(table, resultSet, entity);

            result.add(entity);
        }

        return result;
    }

    @Override
    public E findFirst(Class<E> table) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return findFirst(table, null);
    }

    @Override
    public E findFirst(Class<E> table, String where) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Statement statement = connection.createStatement();
        String tableName = getTableName(table);

        String query = String.format("SELECT * FROM %s %s LIMIT 1",
                tableName, where != null ? " WHERE " + where : "");

        ResultSet resultSet = statement.executeQuery(query);
        E entity = table.getDeclaredConstructor().newInstance();

        resultSet.next();
        fillEntity(table, resultSet, entity);

        return entity;
    }

    private void fillEntity(Class<E> table, ResultSet resultSet, E entity) throws SQLException, IllegalAccessException {
        Field[] declaredFields = table.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            fillField(field, resultSet, entity);
        }
    }

    private void fillField(Field field, ResultSet resultSet,  E entity) throws SQLException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        String fieldName = field.getAnnotationsByType(Column.class)[0].name();

        if (fieldType == int.class || fieldType == Integer.class) {
            int value = resultSet.getInt(fieldName);

            field.set(entity, value);
        } else if (fieldType == long.class || fieldType == Long.class) {
            long value = resultSet.getLong(fieldName);

            field.set(entity, value);
        } else if (fieldType == LocalDate.class) {
            LocalDate value = LocalDate.parse(resultSet.getString(fieldName));

            field.set(entity, value);
        } else {
            String value = resultSet.getString(fieldName);

            field.set(entity, value);
        }


    }
}