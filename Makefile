SOURCES=$(wildcard src/*.java)
.PHONY: default
default: run

.PHONY: compile
compile: bin/compiled

bin/compiled: $(SOURCES)
	mkdir -p bin
	javac -d bin src/*.java
	touch bin/compiled;

.PHONY: run
run: compile
	java -cp bin MVCMain

.PHONY: clean
clean:
	rm -rf bin
