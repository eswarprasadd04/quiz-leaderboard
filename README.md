# Quiz Leaderboard

Made this for the Bajaj Finserv Health qualifier task. Its a Java program that hits the quiz API, collects scores and builds a leaderboard.

## Basically what it does

- Hits the /quiz/messages endpoint 10 times (poll 0 to 9)
- 5 sec gap between each call
- Lots of duplicate data comes in the responses so i filter those out using roundId + participant combo
- Then just add up all the scores per person
- Sort by highest score and submit to /quiz/submit

## To run

Need Java 11+

    javac QuizLeaderboard.java
    java QuizLeaderboard

## Reg No

RA2311050010018
