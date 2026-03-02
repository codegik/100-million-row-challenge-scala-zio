.PHONY: compile test generate run clean

compile:
	sbt compile

test: compile
	sbt "run test_small.txt"
	@echo "\nOutput:"
	@cat output.json | jq .

generate:
	./generate.sh 100000000

run: compile
	sbt "run measurements.txt"

clean:
	sbt clean
	rm -f measurements.txt output.json
