package com.github.tombentley.krpccodegen.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class KrpcGeneratorTest {

    private static final String MESSAGE_SCHEMAS_PATH = "message-schemas/common/message";
    private static final String OUTPUT_DIR = "generated-test-sources/krpc";
    private static final String TEST_CLASSES_DIR = "test-classes";

    @Test
    public void testHelloWorld() throws IOException {
        KrpcGenerator gen = new KrpcGenerator();

        File outputDir = getOutputDir();
        outputDir.mkdirs();
        gen.setOutputDir(outputDir);
        gen.setOutputFilePattern("${schemaName}.txt");

        gen.setSchemaDir(getSchemaDir());
        gen.setSchemaFilter("*.json");

        gen.setTemplateDir(getTemplateDir());
        gen.setTemplateNames(List.of("hello-world/example.ftl"));

        gen.generate();
    }

    @Test
    public void testKrpcData() throws IOException {
        KrpcGenerator gen = new KrpcGenerator();

        File outputDir = getOutputDir();
        outputDir.mkdirs();
        gen.setOutputDir(outputDir);
        gen.setOutputFilePattern("${schemaName}.java");

        gen.setSchemaDir(getSchemaDir());
        gen.setSchemaFilter("*.json");

        gen.setTemplateDir(getTemplateDir());
        gen.setTemplateNames(List.of("Data/example.ftl"));

        gen.generate();
    }

    @Test
    public void testKproxyFilter() throws IOException {
        KrpcGenerator gen = new KrpcGenerator();

        File outputDir = getOutputDir();
        outputDir.mkdirs();
        gen.setOutputDir(outputDir);
        gen.setOutputFilePattern("${schemaName}Filter.java");

        gen.setSchemaDir(getSchemaDir());
        gen.setSchemaFilter("*.json");

        gen.setTemplateDir(getTemplateDir());
        gen.setTemplateNames(List.of("Kproxy/Filter.ftl"));

        gen.generate();
    }

    private static File getSchemaDir() {
        return getBuildDir().resolve(MESSAGE_SCHEMAS_PATH).toFile();
    }

    private static File getOutputDir() {
        return getBuildDir().resolve(OUTPUT_DIR).toFile();
    }

    private static File getTemplateDir() {
        return getBuildDir().resolve(TEST_CLASSES_DIR).toFile();
    }

    private static Path getBuildDir() {
        try {
            return Paths.get(KrpcGeneratorTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Couldn't resolve build directory", e);
        }
    }
}
