package com.djeno.ipnexignbootcamp2025.controllers;

import com.djeno.ipnexignbootcamp2025.services.ImportSubscribersService;
import com.djeno.ipnexignbootcamp2025.services.SubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Импорт данных", description = "API для импорта данных абонентов")
@RequiredArgsConstructor
@RequestMapping("/import")
@RestController
public class ImportSubscribersController {

    private final ImportSubscribersService importSubscribersService;
    private final SubscriberService subscriberService;

    @Operation(
            summary = "Импорт JSON файла",
            description = "Файл должен содержать список номеров в JSON формате.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"),
                            examples = @ExampleObject(
                                    name = "Пример JSON",
                                    value = "[\"79001234567\", \"79009876543\", \"79111234567\"]"
                            )
                    )
            )
    )
    @PostMapping(value = "/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importJson(@RequestPart("file") MultipartFile file) {
        try {
            List<String> msisdns = importSubscribersService.parseJsonFile(file);
            subscriberService.saveSubscribers(msisdns);
            return ResponseEntity.ok("Данные успешно импортированы");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Импорт XML файла",
            description = "Файл должен содержать список номеров в XML формате.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"),
                            examples = @ExampleObject(
                                    name = "Пример XML",
                                    value = "<subscribers>\n    <msisdn>79001234567</msisdn>\n    <msisdn>79009876543</msisdn>\n    <msisdn>79111234567</msisdn>\n</subscribers>"
                            )
                    )
            )
    )
    @PostMapping(value = "/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importXml(@RequestPart("file") MultipartFile file) {
        try {
            List<String> msisdns = importSubscribersService.parseXmlFile(file);
            subscriberService.saveSubscribers(msisdns);
            return ResponseEntity.ok("Данные успешно импортированы");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Импорт YAML файла",
            description = "Файл должен содержать список номеров в YAML формате.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"),
                            examples = @ExampleObject(
                                    name = "Пример YAML",
                                    value = "subscribers:\n  - \"79001234567\"\n  - \"79009876543\"\n  - \"79111234567\""
                            )
                    )
            )
    )
    @PostMapping(value = "/yaml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importYaml(@RequestPart("file") MultipartFile file) {
        try {
            List<String> msisdns = importSubscribersService.parseYamlFile(file);
            subscriberService.saveSubscribers(msisdns);
            return ResponseEntity.ok("Данные успешно импортированы");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
        }
    }
}
