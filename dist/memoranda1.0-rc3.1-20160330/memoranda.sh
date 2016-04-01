#!/bin/sh

export MEMORANDA_HOME=./data

LCP="./build/memoranda.jar:./lib/xom-1.0.jar:./lib/xercesImpl.jar:./lib/xmlParserAPIs.jar:./lib/nekohtml.jar:./lib/nekohtmlXni.jar"

java -cp ${LCP} net.sf.memoranda.Start $1
