/*
 * Copyright 2003-2012 FandianPF (Stephen Gaito).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fandianpf.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File

//TODO: Split this test into "pluginIsApplied" to "pluginIsApplied" and
// "taskArgsCorrect"
class Antlr4TestPluginTest {
    @Test
    void pluginIsApplied() {
        Project testProject = ProjectBuilder.builder().build()
        testProject.with {
          apply plugin: 'antlr4test'
          apply plugin: 'groovy'
          antlr4test {
            grammarName = 'CSV'
            grammarPackage = 'org.fandianpf.testParser'
            startRuleName = 'file'
            testDocs = fileTree(dir: 'src/test/resources/testDocs',
                                include: ['example1.csv'])         
          }
        }
        
        // Setup the testDocs in the testProject directory
        Files.createDirectories(Paths.get(testProject.file("src/test/resources/testDocs").getAbsolutePath()))
        Files.copy(Paths.get(        new File("src/test/resources/testDocs/example1.csv").getAbsolutePath()),
                   Paths.get(testProject.file("src/test/resources/testDocs/example1.csv").getAbsolutePath()))

        def task = testProject.tasks.findByName('antlr4test')
        assert task instanceof Antlr4TestTask
        assert task.testDocs.contains(testProject.file('src/test/resources/testDocs/example1.csv'))
        assert task.output == testProject.file("${testProject.buildDir}/testDocs-results")
        assert task.tree
        assert task.tokens
        assert task.encoding == 'utf-8'
        def args = task.antlr4TestArgs()
        assert args.get(0) == 'org.fandianpf.testParser.CSV'
        assert args.get(1) == 'file'
        assert args.contains('-tree')
        assert args.contains('-tokens')
        assert args.contains('-encoding')
        assert args.contains('utf-8')
        assert args.contains('-timings')
        assert args.contains(testProject.file('timingsTable.csv').getAbsolutePath())
        assert args.contains('-sourceDir')
        assert args.contains(testProject.file('src/test/resources/testDocs'))
        assert args.contains('-outputDir')
        assert args.contains(testProject.file('build/testDocs-results'))
        assert args.contains(testProject.file("src/test/resources/testDocs/example1.csv").getAbsolutePath())
    }
}
