package com.statsservice.service;

import com.statsservice.dto.Message;
import com.statsservice.dto.RequestAvg;
import com.statsservice.dto.ServiceAvg;
import com.statsservice.repository.IStatsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class StatsService {
    @Autowired
    private final IStatsRepo repo;

    public StatsService(IStatsRepo repo) {
        this.repo = repo;
    }

    public void process(Message msg) throws SQLException {
        repo.insert(msg);
    }

    public List<Message> select() throws SQLException {
        return repo.selectAll();
    }

    public List<ServiceAvg> selectServiceAvgTime() throws SQLException {
        return repo.selectServiceAvgTime();
    }

    public List<RequestAvg> selectRequestAvgTime() throws SQLException {
        return repo.selectRequestAvgTime();
    }
}
