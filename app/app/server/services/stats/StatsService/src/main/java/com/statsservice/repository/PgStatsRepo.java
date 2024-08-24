package com.statsservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.statsservice.dto.Message;
import com.statsservice.dto.RequestAvg;
import com.statsservice.dto.ServiceAvg;
import com.statsservice.utils.ConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий используется для работы с таблицей places базы данных PostgreSQL
 * Для подключения используется драйвер JDBC
 */
@Repository
@Slf4j
public class PgStatsRepo implements IStatsRepo {
    /**
     * Объект подключения к БД
     */
    private final Connection conn = ConnectionManager.open();

    /**
     * Добавление новой записи в базу статистики
     * @param msg сообщение от брокера статистики
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void insert(Message msg) throws SQLException {
        String insertMsg = "INSERT INTO events " +
                "(event_uuid, username, action, event_start, event_end, " +
                "params, service) " +
                "VALUES (?, ?, ?, ?, ?, to_json(?::json), ?)";
        PreparedStatement statInsertion = conn.prepareStatement(insertMsg);

        statInsertion.setObject(1, msg.getEventUuid());
        statInsertion.setString(2, msg.getUsername());
        statInsertion.setString(3, msg.getAction());
        statInsertion.setTimestamp(4, Timestamp.valueOf(msg.getEventStart()));
        statInsertion.setTimestamp(5, Timestamp.valueOf(msg.getEventEnd()));
        if (msg.getParams() != null) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = "{}";
            try {
                json = ow.writeValueAsString(msg.getParams());
            } catch (JsonProcessingException e) {
                log.error("Json Processing error {}, {}", e.getMessage(), msg.getParams().toString());
            }
            statInsertion.setString(6, json);
        }
        else
            statInsertion.setString(6, "{}");
        statInsertion.setString(7, msg.getService());
        statInsertion.executeUpdate();
    }

    /**
     * Получение всех записей статистики
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public List<Message> selectAll() throws SQLException {
        ArrayList<Message> events = new ArrayList<>();

        String getEvents = "SELECT * FROM events";

        PreparedStatement eventsQuery = conn.prepareStatement(getEvents);
        ResultSet rs = eventsQuery.executeQuery();
//        ZoneId zoneId = ZoneId.systemDefault();

        while (rs.next())
        {
            Message msg = new Message(rs.getObject("event_uuid", UUID.class),
                    rs.getString("username"),
                    rs.getString("action"),
                    rs.getTimestamp("event_start").toLocalDateTime(),
                    rs.getTimestamp("event_end").toLocalDateTime(),
                    rs.getString("service"));
            events.add(msg);
        }

        return events;
    }

    /**
     * Получение среднего времени обработки запросов сервисами
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public List<ServiceAvg> selectServiceAvgTime() throws SQLException {
        ArrayList<ServiceAvg> services = new ArrayList<>();

        String getAvg = "SELECT service, COUNT(*) as num, " +
                "EXTRACT(EPOCH FROM AVG(event_end - event_start)) * 1000 as avg_time " +
                "FROM events " +
                "GROUP BY service " +
                "ORDER BY avg_time";

        PreparedStatement avgServiceQuery = conn.prepareStatement(getAvg);
        ResultSet rs = avgServiceQuery.executeQuery();

        while (rs.next())
        {
            ServiceAvg service = new ServiceAvg(rs.getString("service"),
                    rs.getLong("num"),
                    rs.getDouble("avg_time"));
            services.add(service);
        }

        return services;
    }

    /**
     * Получение среднего времени обработки запросов сервисами в разрезе запросов
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public List<RequestAvg> selectRequestAvgTime() throws SQLException {
        ArrayList<RequestAvg> requests = new ArrayList<>();

        String getAvg = "SELECT service, action, COUNT(*) as num, " +
                "EXTRACT(EPOCH FROM AVG(event_end - event_start)) * 1000 as avg_time " +
                "FROM events " +
                "GROUP BY service, action " +
                "ORDER BY avg_time";

        PreparedStatement avgRequestQuery = conn.prepareStatement(getAvg);
        ResultSet rs = avgRequestQuery.executeQuery();

        while (rs.next())
        {
            RequestAvg request = new RequestAvg(rs.getString("service"),
                    rs.getString("action"),
                    rs.getLong("num"),
                    rs.getDouble("avg_time"));
            requests.add(request);
        }

        return requests;
    }
}
