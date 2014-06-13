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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.file.FileTree

class Antlr4TestTask extends JavaExec {
  
    @Input
    String grammarName
    
    @Input
    String grammarPackage
  
    @Input
    String startRuleName
    
    @Input
    FileTree testDocs

    @InputDirectory
    @Optional
    File source = project.file('src/test/resources/testDocs')
    
    @OutputDirectory
    @Optional
    File output = project.file("${project.buildDir}/testDocs-results")

    @Input
    @Optional
    boolean tree = true

    @Input
    @Optional
    boolean tokens = true

    @Input
    @Optional
    boolean trace = false

    @Input
    @Optional
    boolean sll = false

    @Input
    @Optional
    boolean diagnostics = true
    
    @Input
    @Optional
    String encoding = 'utf-8'
    
    @Input
    @Optional
    String metricsTablePath = project.file('metricsTable.csv')    

    Antlr4TestTask() {
    	main = 'org.fandianpf.antlr4.regressionTestRig.RegressionTestRig'
      setClasspath(project.configurations.antlr4test)
    }

    @TaskAction
    void exec() {
        args = antlr4TestArgs()

        super.exec()
    }

    // This is a public method to allow for unit testing
    public List antlr4TestArgs() {
        def args = []
        args << "${grammarPackage}.${grammarName}"
        args << startRuleName
        args << (       tree ? '-tree'        : '')
        args << (     tokens ? '-tokens'      : '')
        args << (      trace ? '-trace'       : '')
        args << (        sll ? '-sll'         : '')
        args << (diagnostics ? '-diagnostics' : '')
        if (!encoding.isEmpty()) {
            args << '-encoding' << encoding
        }
        if (!metricsTablePath.isEmpty()) {
            args << '-metrics' << metricsTablePath
        }
        args << '-sourceDir' << testDocs.getDir()
        args << '-outputDir' << output
        args.addAll(testDocs.collect { it.path } )

        args
    }
}
