JAVAFILES=$(shell find src -regex .*.java)
SOURCES=$(shell find src -regex .*.java | grep -v Test.java)
TESTS=$(shell find src -regex .*Test.java)

.PHONY: default
default: run

.PHONY: all
all: compile doc

.PHONY: compile
compile: bin/compiled

bin/compiled: $(SOURCES)
	mkdir -p bin
	javac -d bin $(SOURCES)
	touch bin/compiled;

.PHONY: run
run: compile
	java -cp bin MVC/MVCMain

.PHONY: doc
doc: doc/generated

doc/generated: $(JAVAFILES)
	mkdir -p doc
	javadoc -private -d doc -cp /usr/share/junit5/ $(JAVAFILES)
	touch doc/generated

.PHONY: viewdoc
viewdoc: doc
	xdg-open doc/index.html > /dev/null 2>&1 &

.PHONY: compiletests
compiletests: bin/compiledtests

bin/compiledtests: $(JAVAFILES)
	mkdir -p bin
	javac -d bin -cp /usr/share/junit5/ $(JAVAFILES)
	touch bin/compiledtests

.PHONY: test
test: compiletests
	java -jar /usr/share/junit5/junit-platform-console-standalone.jar -cp bin -scan-classpath

.PHONY: clean
clean:
	rm -rf bin
	rm -rf doc
