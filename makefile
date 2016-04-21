all: main

main:
	javac -classpath weka.jar TimeSeriesPredictor.java
	java -classpath weka.jar:. TimeSeriesPredictor

clean:
	rm *.class
	