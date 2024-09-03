package com.libraryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.libraryservice.config.ActionType;
import com.libraryservice.dto.BookInfo;
import com.libraryservice.dto.LibraryBookResponse;
import com.libraryservice.dto.LibraryResponse;
import com.libraryservice.dto.StatMessage;
import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;
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
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "LIBRARIES")
public class LibraryController {
    /**
     * Сервис, работающий с пользователями
     */
    private final LibraryService libraryService;

    private final BookMapper bookMapper;
    private final LibraryMapper libraryMapper;
    private final StatsProducer producer;

    public LibraryController(LibraryService libraryService, BookMapper bookMapper,
                             LibraryMapper libraryMapper, StatsProducer producer) {
        this.libraryService = libraryService;
        this.bookMapper = bookMapper;
        this.libraryMapper = libraryMapper;
        this.producer = producer;
    }

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить список библиотек в городе")
    @GetMapping("/libraries")
    public ResponseEntity<ArrayList<LibraryResponse>> getLibrariesByCity(@RequestParam("city") String city) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        ArrayList<Library> libraries = libraryService.getLibrariesByCity(city);

        ArrayList<LibraryResponse> allLibs = new ArrayList<>();
        for (Library lib : libraries) {
            allLibs.add(libraryMapper.toLibraryResponse(lib));
        }

        ResponseEntity<ArrayList<LibraryResponse>> res = ResponseEntity.status(HttpStatus.OK).body(allLibs);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.LIBRARIES_BY_CITY,
                            new HashMap<String, String>() {{
                                put("city", city);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить список книг в библиотеке")
    @GetMapping("/libraries/{libraryUid}/books")
    public ResponseEntity<ArrayList<LibraryBookResponse>> getBooksByLibrary(@PathVariable UUID libraryUid,
                                                                            @RequestParam boolean showAll) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        ArrayList<Book> books = libraryService.getBooksByLibrary(libraryUid, showAll);

        ArrayList<LibraryBookResponse> allBooks = new ArrayList<>();
        for (Book b : books) {
            allBooks.add(bookMapper.toLibraryBookResponse(b));
        }

        ResponseEntity<ArrayList<LibraryBookResponse>> res = ResponseEntity.status(HttpStatus.OK).body(allBooks);

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.BOOKS_BY_LIBRARY,
                            new HashMap<String, UUID>() {{
                                put("library_uuid", libraryUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Взятие и возврат книги в библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую хотим получить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Взять или вернуть книгу")
    @PutMapping("/libraries/{libraryUid}/books/{bookUid}")
    public ResponseEntity<String> bookOperation(@PathVariable UUID libraryUid, @PathVariable UUID bookUid,
                                                @RequestParam("rent") boolean rent) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        if (!rent)
            libraryService.returnBook(libraryUid, bookUid);

        else {
            try {
                libraryService.takeBook(libraryUid, bookUid);
            } catch (BookIsNotAvailable e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.OK).build();

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.BOOK_OPERATION,
                            new HashMap<String, String>() {{
                                put("library_uuid", libraryUid.toString());
                                put("book_uuid", bookUid.toString());
                                put("rent", String.valueOf(rent));
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Получение информации о книге в конкретной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую хотим получить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о книге в конкретной библиотеке")
    @GetMapping("/libraries/{libraryUid}/books/{bookUid}")
    public ResponseEntity<LibraryBookResponse> getLibraryBookInfo(@PathVariable UUID libraryUid,
                                                                  @PathVariable UUID bookUid) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        Book book = libraryService.getLibraryBookInfo(libraryUid, bookUid);
        ResponseEntity<LibraryBookResponse> res = ResponseEntity.status(HttpStatus.OK).body(bookMapper.toLibraryBookResponse(book));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.LIBRARY_BOOK,
                            new HashMap<String, UUID>() {{
                                put("library_uuid", libraryUid);
                                put("book_uuid", bookUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Получение информации о библиотеке
     * @param libraryUid UUID библиотеки, о которой нужна информация
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о библиотеке")
    @GetMapping("/libraries/{libraryUid}")
    public ResponseEntity<LibraryResponse> getLibraryInfo(@PathVariable UUID libraryUid) throws SQLException {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getUsername();
        Library lib = libraryService.getLibraryInfo(libraryUid);
        ResponseEntity<LibraryResponse> res = ResponseEntity.status(HttpStatus.OK).body(libraryMapper.toLibraryResponse(lib));

        try {
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.LIBRARY,
                            new HashMap<String, UUID>() {{
                                put("library_uuid", libraryUid);
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[LIBRARY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
