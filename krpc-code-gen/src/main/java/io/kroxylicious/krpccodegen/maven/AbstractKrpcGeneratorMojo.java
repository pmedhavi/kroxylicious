/*
 * Copyright Kroxylicious Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.kroxylicious.krpccodegen.maven;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

import io.kroxylicious.krpccodegen.main.KrpcGenerator;

public abstract class AbstractKrpcGeneratorMojo extends AbstractMojo {

    /**
     * Gives access to the Maven project information.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(required = true)
    private File messageSpecDirectory;

    @Parameter(defaultValue = "*.json")
    private String messageSpecFilter;

    @Parameter(required = true)
    private File templateDirectory;

    @Parameter(required = true)
    private String templateNames;

    @Parameter(required = true)
    private String outputPackage;

    @Parameter(defaultValue = "${messageSpecName}.java")
    private String outputFilePattern;

    @Parameter(defaultValue = "${project.build.directory}${file.separator}generated-sources${file.separator}/krpc")
    private File outputDirectory;

    @Component
    private BuildContext buildContext;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (buildContext == null || !buildContext.isIncremental()) {
            List<String> templates = Stream.of(templateNames.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            KrpcGenerator gen = builder()
                    .withLogger(new MavenLogger(KrpcGenerator.class.getName(), getLog()))
                    .withMessageSpecDir(messageSpecDirectory)
                    .withMessageSpecFilter(messageSpecFilter)
                    .withTemplateDir(templateDirectory)
                    .withTemplateNames(templates)
                    .withOutputPackage(outputPackage)
                    .withOutputDir(outputDirectory)
                    .withOutputFilePattern(outputFilePattern)
                    .build();

            try {
                gen.generate();
            }
            catch (Exception e) {
                throw new MojoExecutionException("Couldn't generate messages", e);
            }

            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        }
    }

    protected abstract KrpcGenerator.Builder builder();
}
