# antlr4-gradle-regressionTestPlugin

A Gradle plugin for regression testing ANTLR4 grammars

# This is an experimental project.

This is an experimental project. The eventual aim is to merge the antlr4test
task into the
[antlr4-gradle-plugin](https://github.com/melix/antlr4-gradle-plugin) project.

We have this separate project to make it easier to design and test this plugin. 

# Description

This gradle plugin is meant to be used with the 
[antlr4-gradle-plugin](https://github.com/melix/antlr4-gradle-plugin) project.

The antlr4-gradle-plugin can be used to generate and compile an ANTLR4 grammar.

This antlr4-gradle-regressionTestPlugin can be used to regression test large
numbers of example "test" documents over the whole development cycle of a given
grammar.

To do this long term regression testing we use the antlr4regressionTestRig tool
in the
[antlr4-regressionTestRig](https://github.com/fandianpf/antlr4-regressionTestRig)
project. This regressionTestRig is based upon the TestRig tool in the ANTLR4
runtime release, but has been extended to provide better support for long term
regression testing of large numbers of test documents.  Please see the 
[antlr4-regressionTestRig](https://github.com/fandianpf/antlr4-regressionTestRig)
for more discussion on the differences.

# Installation

Add the following snippet to your build.gradle file:

```
buildscript {
  repositories {
    ivy {
      url 'http://fandianpf.github.io/ivyRepo/'
    }
    maven {
      name 'JFrog OSS snapshot repo'
      url 'https://oss.jfrog.org/oss-snapshot-local/'
    }
    mavenCentral()
  }

  dependencies { 
    classpath 'me.champeau.gradle:antlr4-gradle-plugin:0.1-SNAPSHOT'
    classpath 'org.fandianpf:antlr4-gradle-regressionTestPlugin:0.2'
  }
}

apply plugin: 'antlr4'
apply plugin: 'antlr4test'
```

This will tell gradle how to find, download and load *both* the antlr4 and
antlr4test plugins.

# Configuration

The antlr4test plugin adds a new task named antlr4test. This task exposes 5
properties as part of its configuration:

| Variable         | Meaning |
|------------------|---------|
| grammarName      | The name of the grammar. No default. Must be provided. |
| grammarPackage   | The fully qualified package name for the grammar. No default. Must be provided. |
| startRuleName    | The name of the start rule in the grammar to be parsed. No default. Must be provided. |
| testDocs         | A Gradle FileTree which includes all input files to be tested. No default. Must be provided. |
| source           | A Gradle File pointing to the source grammar which if changed should ensure re-execution of this task |
| output           | A Gradle File pointing to the directory in which the result files should be stored. Default: $buildDir/testDocs-results. |
| tree             | A boolean which if true will print the parse tree into the results file in a "python" format. Default: true |
| tokens           | A boolean which if true will print the token stream from the lexer into the results file. Default: true |
| trace            | A boolean which if true will print the trace of the parser rule building. Output currently goes to System.out. Default: false |
| sll              | A boolean which if true will tell the parser to use the faster SLL(*) parsing mode rather than the full ALL(*) mode. Default: false |
| diagnostics      | A boolean which if true will capture the parser warning messages to the results file. Default true |
| primaryIndent    | A string whose value is the primary indent used for each recursive level in the parse tree output. Default ". " |
| secondaryIndent  | A string whose value is the secondary indent used for each recursive level in the parse tree output. Default ", " |
| indentCycle      | An int whose value is used to determine when the secondary indent string should be used. The last indent of every indentCycle will use the seconary indent string. This makes it easier to determine the depth of the parse tree. Default: 5 |
| encoding         | A string whose value denotes the encoding used to read/lex the input files. Default: UTF-8 |
| metricsTablePath | (version 0.2) A Gradle File pointing to the metricsTable file to be updated. If no metricsTable file currently exists a new one will be created. Default: metricsTable.csv. NOTE: this file is not stored relative to the output directory. |
| timingsTablePath | (version 0.1) A Gradle File pointing to the timingsTable file to be updated. If no timingsTable file currently exists a new one will be created. Default: timingsTable.csv. NOTE: this file is not stored relative to the output directory. |

# Use

To use the antlr4test task, add the following to your build.gradle file (and
alter as appropriate for your needs):

```
antlr4test {
  
  grammarPackage = 'org.fandianpf.testParser'
  grammarName    = 'CSV'
  startRuleName  = 'file'
  testDocs       = fileTree(dir: 'src/test/resources/testDocs',
                            include: '**/*.csv')
  source = antlr4.source

  dependsOn compileJava
  
  antlr4test.classpath(files("$buildDir/classes/main"))
  
  configurations {
    compile.extendsFrom antlr4test
  }
}
```

Note that you *will* need to change the values used by the grammarPackage,
grammarName, startRuleName, and testDocs variables. These values have been taken
from the testSubProject.

To execute the antlr4test task type:

    gradle antlr4test

# Release process

To release, bump the versions of both antlr4-gradle-regressionTestPlugin and the
antlr4-regressionTestRig in both build.gradle files (in both the main and
testSubProject) as well as in the Antrl4TestPlugin.groovy file.

Then type:

    gradle publish

# License

Copyright 2003-2014 FandianPF (Stephen Gaito)

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
this file except in compliance with the License. You may obtain a copy of the
License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.

The files contained in src/main/groovy/org/fandianpf/gradle and
src/test/groovy/org/fandianpf/gradle are based on and hence similar to the
corresponding files in the https://github.com/melix/antlr4-gradle-plugin project
developed by Cédric Champeau.
 
The files contained in src/test/resources/testDocs and the enclosed
testSubProject have been taken from the ANTLR
https://github.com/antlr/grammars-v4/tree/master/csv project and are covered by
their original 3-Clause BSD license.
