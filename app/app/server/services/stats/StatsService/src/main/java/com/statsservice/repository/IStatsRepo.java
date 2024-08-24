package com.statsservice.repository;

import com.statsservice.dto.Message;
import com.statsservice.dto.RequestAvg;
import com.statsservice.dto.ServiceAvg;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс репозитория используется для работы с базой данных, отвечающей за библиотеки и книги
 */
public interface IStatsRepo {
    /**
     * Добавление новой записи в базу статистики
     * @param msg сообщение от брокера статистики
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void insert(Message msg) throws SQLException;

    /**
     * Получение всех записей статистики
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public List<Message> selectAll() throws SQLException;

    /**
     * Получение среднего времени обработки запросов сервисами
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public List<ServiceAvg> selectServiceAvgTime() throws SQLException;

    /**
     * Получение среднего времени обработки запросов сервисами в разрезе запросов
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public List<RequestAvg> selectRequestAvgTime() throws SQLException;
}
