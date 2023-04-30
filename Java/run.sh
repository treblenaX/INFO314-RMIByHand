javac *.java

java Server &
P1=$!
java Client
java Client
java Client
java UnitTest

kill $P1
