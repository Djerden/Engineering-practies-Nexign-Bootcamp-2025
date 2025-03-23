package com.djeno.ipnexignbootcamp2025.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImportSubscribersService {

    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper;
    private final YAMLMapper yamlMapper;

    public ImportSubscribersService() {
        this.objectMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
        this.yamlMapper = new YAMLMapper();
    }


    public List<String> parseJsonFile(MultipartFile file) throws IOException {
        return objectMapper.readValue(file.getInputStream(), new TypeReference<List<String>>() {});
    }

    public List<String> parseXmlFile(MultipartFile file) throws IOException {
        return xmlMapper.readValue(file.getInputStream(), new TypeReference<List<String>>() {});
    }

    public List<String> parseYamlFile(MultipartFile file) throws IOException {
        return yamlMapper.readValue(file.getInputStream(), new TypeReference<List<String>>() {});
    }
}
