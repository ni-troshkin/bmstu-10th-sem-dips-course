package com.libraryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.libraryservice.config.ActionType;
import com.libraryservice.dto.BookInfo;
import com.libraryservice.dto.StatMessage;
import com.libraryservice.entity.Book;
import com.libraryservice.jwt.JwtAuthentication;
import com.libraryservice.mapper.BookMapper;
import com.libraryservice.mapper.LibraryMapper;
import com.libraryservice.producer.StatsProducer;
import com.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "BOOKS")
public class BookController {
    private final LibraryService libraryService;

    private final BookMapper bookMapper;

    private final StatsProducer producer;

    public BookController(LibraryService libraryService, BookMapper bookMapper,
                          StatsProducer producer) {
        this.libraryService = libraryService;
        this.bookMapper = bookMapper;
        this.producer = producer;
    }

    /**
     * Получение информации о книге
     * @param bookUid UUID книги, о которую хотим получить информацию
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о книге")
    @GetMapping("/books/{bookUid}")
    public ResponseEntity<BookInfo> getBookInfo(@PathVariable UUID bookUid) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();

        Book book = libraryService.getBookInfo(bookUid);
        System.out.println(book.toString());
        ResponseEntity<BookInfo> res = ResponseEntity.status(HttpStatus.OK).body(bookMapper.toBookInfo(book));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.BOOK,
                            new HashMap<String, UUID>() {{
                                put("book_uuid", bookUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
