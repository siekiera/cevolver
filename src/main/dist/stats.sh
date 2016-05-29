#!/bin/bash
if [ $# != 1 ]; then
	echo 'Podaj nazwę katalogu!'
	exit
fi
dir=`dirname $0`
java_cmd="$JAVA_HOME/bin/java -cp $dir/lib/Cevolver-0.1.0.jar:$dir/lib/*"
$java_cmd pl.edu.pw.elka.mtoporow.cevolver.ext.diag.aggregation.StatsAggregator "$1" 'stats_total.csv' 1
$java_cmd pl.edu.pw.elka.mtoporow.cevolver.ext.diag.aggregation.StatsAggregator "$1" 'funkcja_celu.csv' 0 t
$java_cmd pl.edu.pw.elka.mtoporow.cevolver.ext.diag.aggregation.FFAggregator "$1"
echo 'Zakończono.'
