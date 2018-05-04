SOURCES=$(shell find src -regex .*.java | grep -v Test.java)
JAVAFILES=$(shell find src -regex .*.java)

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
	javadoc -d doc -cp /usr/share/junit5/ $(JAVAFILES)
	touch doc/generated

.PHONY: viewdoc
viewdoc: doc
	xdg-open doc/index.html > /dev/null 2>&1 &

.PHONY: clean
clean:
	rm -rf bin
	rm -rf doc
