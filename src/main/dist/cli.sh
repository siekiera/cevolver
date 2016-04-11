#!/bin/bash
dir=`dirname $0`
$JAVA_HOME/bin/java -cp "$dir"/lib/Cevolver-0.1.0.jar:"$dir"/lib/* pl.edu.pw.elka.mtoporow.cevolver.cli.CevolverCli $@