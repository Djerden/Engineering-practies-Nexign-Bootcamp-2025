package com.djeno.ipnexignbootcamp2025.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ImportSubscribersServiceTest {

    private ImportSubscribersService importSubscribersService;

    @BeforeEach
    void setUp() {
        importSubscribersService = new ImportSubscribersService();
    }

    @Test
    void parseJsonFile_ShouldReturnListOfStrings() throws IOException {
        MultipartFile mockFile = createMockFile("[\"user1\", \"user2\"]", "application/json");

        List<String> result = importSubscribersService.parseJsonFile(mockFile);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0));
        assertEquals("user2", result.get(1));
    }

    @Test
    void parseXmlFile_ShouldReturnListOfStrings() throws IOException {
        String xmlContent = "<List><item>user1</item><item>user2</item></List>";
        MultipartFile mockFile = createMockFile(xmlContent, "application/xml");

        List<String> result = importSubscribersService.parseXmlFile(mockFile);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0));
        assertEquals("user2", result.get(1));
    }

    @Test
    void parseYamlFile_ShouldReturnListOfStrings() throws IOException {
        String yamlContent = "- user1\n- user2";
        MultipartFile mockFile = createMockFile(yamlContent, "application/x-yaml");

        List<String> result = importSubscribersService.parseYamlFile(mockFile);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0));
        assertEquals("user2", result.get(1));
    }

    private MultipartFile createMockFile(String content, String contentType) {
        return new MockMultipartFile("file", "test-file", contentType, content.getBytes(StandardCharsets.UTF_8));
    }
}